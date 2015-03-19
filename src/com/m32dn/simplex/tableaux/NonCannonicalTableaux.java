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

import com.m32dn.simplex.logger.SimplexLogger;

/**
 *
 * @author majo32
 */
public class NonCannonicalTableaux extends BaseTableaux {

    /**
     * Input matrix format example max 4a + 3b -3c -5d constraint 1a + 3c = 45
     * constraint 4b - 7d = 21 constraint a + b +c - 7d = 3
     *
     * input matrix: 
     * 4 3 -3 -5 0 
     * 1 0 3 0 45 
     * 0 4 0 -7 21 
     * 1 1 1 -7 3
     *
     * If &lt= OR &gt= in constraints use slack variables to get equal form
     *
     * @param constraints
     */
    public NonCannonicalTableaux(double[]... constraints) {
        super(constraints[0].length, constraints);
        log();
    }

    /**
     * Input matrix format example max 4a + 3b -3c -5d constraint 1a + 3c = 45
     * constraint 4b - 7d = 21 constraint a + b +c - 7d = 3 varCount: 5
     * maxFunction: 
     * 4 3 -3 -5 0 
     * constraints: 
     * 1 0 3 0 45
     * 0 4 0 -7 21 
     * 1 1 1 -7 3
     *
     * If &lt= OR &gt= in constraints use slack variables to get equal form
     *
     * @param maxFunction
     * @param constraints
     */
    public NonCannonicalTableaux(double[] maxFunction, double[]... constraints) {
        super(maxFunction.length, maxFunction, constraints);
        log();
    }

    /**
     * Input matrix format example max 4a + 3b -3c -5d constraint 1a + 3c = 45
     * constraint 4b - 7d = 21 constraint a + b +c - 7d = 3 varCount: 5
     * maxFunction: 4 3 -3 -5 0 
     * constraints: 
     * 1 0 3 0 45 
     * 0 4 0 -7 21 
     * 1 1 1 -7 3
     * columnsCount:
     * 5
     * If &lt= OR &gt= in constraints use slack variables to get equal form
     *
     * @param columnsCount
     * @param maxFunction
     * @param constraints
     */
    public NonCannonicalTableaux(int columnsCount, double[] maxFunction, double[]... constraints) {
        super(columnsCount, maxFunction, constraints);
        log();
    }
    
    private void log(){
        SimplexLogger.log("Simple corrections:");
        SimplexLogger.log(this.getPrintable());
    }

}
