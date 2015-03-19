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
package com.m32dn.simplex.algorithm;

import com.m32dn.simplex.exception.SimplexException;
import com.m32dn.simplex.tableaux.CannonicalTableaux;

/**
 *
 * @author majo32
 */
public class SimplexAlgorithm implements SimplexStrategy {

    private final CannonicalTableaux table;
    private static final int __MAX_LOOPS = 10000;

    public SimplexAlgorithm(CannonicalTableaux table) {
        this.table = table;
    }

    private int selectVariablePivot(int[] except) {
        double max = 0;
        int maxIndex = -1;
        double tmp;
        for (int i = 0; i < table.getVariablesCount(); i++) {
            tmp = table.get(i, 0);
            if (tmp > 0 && tmp > max) {
                if (except != null && except[i] == 1) {

                } else {
                    max = tmp;
                    maxIndex = i;
                }
            }
        }
        return maxIndex;
    }

    private int selectRowPivot(int var) {
        int bIndex = table.getBIndex();
        double min = 0;
        int minIndex = -1;
        double tmp, tmpB;
        for (int i = 1; i < table.getRowsCount(); i++) {
            tmp = table.get(var, i);
            tmpB = table.get(bIndex, i);
            if (tmp > 0 && ((tmpB / tmp < min) || (minIndex == -1))) {
                min = tmpB / tmp;
                minIndex = i;
            }
        }
        return minIndex;
    }

    private void optimizeStep(int var, int row) {
        table.multipleRow(row, 1 / table.get(var, row));
        for (int i = 0; i < table.getRowsCount(); i++) {
            if (i != row) {
                table.forceZeroOnRow(row, i, var);
            }
        }
    }

    private boolean hasPositiveSolutions() {
        int bIndex = table.getBIndex();
        for (int i = 1; i < table.getRowsCount(); i++) {
            if (table.get(bIndex, i) < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean optimize() throws SimplexException {
        int var, row, counter = 0;
        boolean loop = true;
        boolean optimal = false;
        while (loop) {
            if (counter++ > table.getVariablesCount() + SimplexAlgorithm.__MAX_LOOPS) {
                throw new SimplexException("Maximum loops exceeded");
            }
            var = this.selectVariablePivot(null);
            if (var == -1) {
                loop = false;
                optimal = true;
            } else {
                row = this.selectRowPivot(var);
                if (row == -1) {
                    loop = false;
                    optimal = false;
                } else {
                    this.optimizeStep(var, row);
                }
            }
        }

        if (!this.hasPositiveSolutions()) {
            throw new SimplexException("Solution contains negative value");
        }
        
        return optimal;

    }
}
