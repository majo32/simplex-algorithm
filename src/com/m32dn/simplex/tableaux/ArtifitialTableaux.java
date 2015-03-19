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
package com.m32dn.simplex.tableaux;

import com.m32dn.simplex.exception.SimplexException;
import com.m32dn.simplex.logger.SimplexLogger;
import java.util.Arrays;

/**
 *
 * @author majo32
 */
class ArtifitialTableaux extends CannonicalTableaux {

    private final int artifitialVarCount;

    protected ArtifitialTableaux(int artifitialVarCount, int varCount, double[] minFunction, double[]... constraints) throws SimplexException {
        super(varCount, minFunction, constraints);
        this.artifitialVarCount = artifitialVarCount;
    }

    public CannonicalTableaux renderCannonicalFormWithFunction(double[] minFunction) throws SimplexException {
        this.optimize();
        if (!this.allRowsInBase()) {
            throw new SimplexException("Ooops not basic result after optimization!!");
        }
        /*if(this.rows[0][this.getBIndex()] != 0){
            throw new SimplexException("Artifitial min-function result is not zero !! <"+ this.rows[0][this.getBIndex()] +">");
        }*/
        for (int i : this.inBase) {
            if (i != -1) {
                if (i >= (this.variableCount - this.artifitialVarCount) && i < this.variableCount) {
                    throw new SimplexException("Artifitial variable is not zero! <" + Arrays.toString(this.getSolution())  + ">");
                }
            }
        }
        double[][] newRows = new double[this.rowsCount - 1][this.columnsCount - this.artifitialVarCount];
        for (int i = 0; i < (this.rowsCount - 1); i++) {
            System.arraycopy(this.rows[i+1], 0, newRows[i], 0, this.columnsCount - this.artifitialVarCount -1);
            newRows[i][this.columnsCount - this.artifitialVarCount -1] = this.rows[i+1][this.getBIndex()];
        }
        SimplexLogger.log("Cannonical instance:");
        CannonicalTableaux c = new CannonicalTableaux(this.columnsCount - this.artifitialVarCount, minFunction, newRows);
        SimplexLogger.log("Simple corrections:");
        SimplexLogger.log(c.getPrintable());
        return c;
    }

}
