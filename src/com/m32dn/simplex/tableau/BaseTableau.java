/*
 * Copyright (C) 2015 majo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.m32dn.simplex.tableau;

import com.m32dn.simplex.exception.SimplexException;
import com.m32dn.simplex.logger.SimplexLogger;
import java.math.BigDecimal;

/**
 *
 * @author majo32
 *
 */
abstract class BaseTableau {

    protected int variableCount;

    protected int columnsCount;
    protected int rowsCount;

    protected double[][] rows;

    protected double[][] defRows;

    protected int[] inBase;
    protected int inBaseCount = 0;

    public BaseTableau(int columnsCount, boolean inverse, double[] minFunction, double[]... constraints) {
        this.variableCount = columnsCount - 1;
        this.columnsCount = columnsCount;
        this.rowsCount = constraints.length + 1;

        this.rows = new double[this.rowsCount][columnsCount];
        this.defRows = new double[this.rowsCount][columnsCount];
        
        for (int i = 0; i < this.rowsCount; i++) {
            if (i == 0) {
                System.arraycopy(minFunction, 0, this.rows[i], 0, this.columnsCount);
            } else {
                System.arraycopy(constraints[i - 1], 0, this.rows[i], 0, this.columnsCount);
            }
        }
        for (int i = 0; i < this.rowsCount; i++) {
            if (i == 0) {
                System.arraycopy(minFunction, 0, this.defRows[i], 0, this.columnsCount);
            } else {
                System.arraycopy(constraints[i - 1], 0, this.defRows[i], 0, this.columnsCount);
            }

        }
        this.startupInit(inverse);
        this.recount(true);
        this.removeNegativeResults();
    }

    public BaseTableau(int columnsCount, boolean inverse, double[]... constraints) {
        this.variableCount = columnsCount - 1;
        this.columnsCount = columnsCount;
        this.rowsCount = constraints.length;

        this.rows = new double[this.rowsCount][columnsCount];
        this.defRows = new double[this.rowsCount][columnsCount];
        
        for (int i = 0; i < this.rowsCount; i++) {
            System.arraycopy(constraints[i], 0, this.rows[i], 0, this.columnsCount);
        }
        for (int i = 0; i < this.rowsCount; i++) {
            System.arraycopy(constraints[i], 0, this.defRows[i], 0, this.columnsCount);
        }
        this.startupInit(inverse);
        this.recount(true);
        this.removeNegativeResults();
    }

    protected void inverseDoubleArray(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = -arr[i];
        }
    }

    private void removeNegativeResults() {
        for (int i = 1; i < this.rowsCount; i++) {
            if (this.rows[i][this.getBIndex()] < 0) {
                this.multipleRow(i, -1);
            }
        }
    }

    private void startupInit(boolean inverse) {
        if(inverse){
            this.inverseDoubleArray(this.rows[0]);
        }
        SimplexLogger.log("New instance:");
        SimplexLogger.log(this.getPrintable());
    }
    
    protected final void recount(boolean force) {

        this.inBase = new int[this.rowsCount];
        this.inBaseCount = 0;
        int res;
        for (int i = 0; i < this.rowsCount; i++) {
            inBase[i] = -1;
        }
        for (int i = 0; i < this.variableCount; i++) {

            res = this.isBasicVariable(i, force);
            if (res >= 0) {
                inBase[res] = i;
                if (force) {
                    this.ensureBaseVariable(i, res);
                }
            }
        }
        for (int i = 0; i < this.rowsCount; i++) {
            if (inBase[i] >= 0) {
                this.inBaseCount++;
            }
        }
    }

    public int isBasicVariable(int var, boolean onlyConstraints) {
        int zeros = 0;
        int ones = 0;
        int oneIndex = 0;
        for (int i = onlyConstraints ? 1 : 0; i < this.rowsCount; i++) {
            if (this.rows[i][var] == 0) {
                zeros++;
            }
            if (this.rows[i][var] > 0) {
                ones++;
                oneIndex = i;
            }
        }
        int dif = onlyConstraints ? 2 : 1;
        if (zeros == this.rowsCount - dif && ones == 1) {
            return oneIndex;
        } else {
            return -1;
        }
    }

    public void multipleRow(int rowId, double multiplier) {
        for (int i = 0; i < this.columnsCount; i++) {
            this.rows[rowId][i] = this.rows[rowId][i] * multiplier;
        }
    }

    public void forceZeroOnRow(int srcRowId, int destRowId, int variable) {
        double multiplier = this.rows[destRowId][variable] / this.rows[srcRowId][variable];
        for (int i = 0; i < this.columnsCount; i++) {
            this.rows[destRowId][i] = this.rows[destRowId][i] - (this.rows[srcRowId][i] * multiplier);
            //if (i == variable) {
            //this.constraints[destRowId][i] = 0;
            //}
        }
    }

    public void ensureBaseVariable(int var, int row) {
        this.multipleRow(row, 1 / this.rows[row][var]);
        //this.constraints[row][var] = 1;
        this.forceZeroOnRow(row, 0, var);
    }

    public double get(int var, int row) {
        return this.rows[row][var];
    }

    public int getRowsCount() {
        return this.rowsCount;
    }

    public int getVariablesCount() {
        return this.variableCount;
    }

    public int getBIndex() {
        return this.variableCount;
    }

    private ArtifitialTableau prepareArtifitialTableaux() throws SimplexException {
        int additive = this.rowsCount - 1 - this.inBaseCount;
        int newColumnsCount = this.variableCount + additive + 1;
        int it = this.variableCount;
        double[][] newConstraints = new double[this.rowsCount - 1][newColumnsCount];

        for (int i = 0; i < this.rowsCount - 1; i++) {
            System.arraycopy(this.rows[i + 1], 0, newConstraints[i], 0, this.variableCount);
            newConstraints[i][newColumnsCount - 1] = this.rows[i + 1][this.variableCount];
        }
        for (int i = 0; i < this.rowsCount - 1; i++) {
            for (int j = this.variableCount; j < newColumnsCount - 1; j++) {
                newConstraints[i][j] = 0;
            }
            if (this.inBase[i + 1] == -1) {
                newConstraints[i][it] = 1;
                it++;
            }
        }

        double[] newFunction = new double[newColumnsCount];
        for (int j = 0; j < this.variableCount; j++) {
            newFunction[j] = 0;
        }
        for (int j = this.variableCount; j < newColumnsCount; j++) {
            newFunction[j] = -1;
        }
        newFunction[newColumnsCount - 1] = this.rows[0][this.variableCount];
        return new ArtifitialTableau(additive, newColumnsCount, newFunction, newConstraints);
    }

    public boolean allRowsInBase() {
        return this.inBaseCount == this.rowsCount - 1;
    }

    private CannonicalTableau copyCannonicalTableaux() throws SimplexException {
        CannonicalTableau c = new CannonicalTableau(this.columnsCount, this.rows);
        return c;
    }

    public CannonicalTableau getCannonical() throws SimplexException {
        SimplexLogger.log("Start rendering canonical form:");
        if (!this.allRowsInBase()) {
            SimplexLogger.log("Need artifitial variables:");
            ArtifitialTableau t = this.prepareArtifitialTableaux();
            SimplexLogger.log(t.getPrintable());
            return t.renderCannonicalFormWithFunction(this.rows[0]);
        } else {
            SimplexLogger.log("Already cannonical");
            return this.copyCannonicalTableaux();
        }
    }

    public String getPrintable(boolean colorize) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_BLUE = "\u001B[34m";
        StringBuilder b = new StringBuilder();
        b.append("Tableaux:\n");
        for (int i = 0; i < this.rowsCount; i++) {
            if (i == 0) {
                if (colorize) {
                    b.append(ANSI_BLUE);
                }
            }
            for (int j = 0; j < this.columnsCount; j++) {
                if (j == this.columnsCount - 1) {
                    if (colorize) {
                        b.append(ANSI_RED);
                    }
                }
                b.append("\t");
                b.append(new BigDecimal(this.rows[i][j]).setScale(2, BigDecimal.ROUND_HALF_UP));
                if (j == this.columnsCount - 1) {
                    if (colorize) {
                        b.append(ANSI_RESET);
                    }
                }
            }
            if (i == 0) {
                if (colorize) {
                    b.append(ANSI_RESET);
                }
            }
            b.append("\n");
        }
        return b.toString();
    }

    public String getPrintable() {
        return this.getPrintable(true);
    }
}
