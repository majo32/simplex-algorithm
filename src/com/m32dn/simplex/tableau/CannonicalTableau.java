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

import com.m32dn.simplex.algorithm.SimplexStrategy;
import com.m32dn.simplex.algorithm.SimplexAlgorithm;
import com.m32dn.simplex.exception.SimplexException;
import com.m32dn.simplex.logger.SimplexLogger;

/**
 *
 * @author majo32
 */
public class CannonicalTableau extends BaseTableau {

    private final SimplexStrategy algo;
    private boolean optimized = false;
    private boolean unbounded = false;
    private SimplexException optimizationException = null;

    protected CannonicalTableau(int varCount, double[]... constraints) throws SimplexException {
        super(varCount, false,  constraints);
        if (!this.allRowsInBase()) {
            throw new SimplexException("Ooops this should be cannonical!!");
        }
        this.algo = new SimplexAlgorithm(this);
    }

    protected CannonicalTableau(int varCount, double[] minFunction, double[]... constraints) throws SimplexException {
        super(varCount, false, minFunction, constraints);
        if (!this.allRowsInBase()) {
            throw new SimplexException("Ooops this should be cannonical!!");
        }
        this.algo = new SimplexAlgorithm(this);
    }

    public CannonicalTableau optimize() throws SimplexException {
        try {
            SimplexLogger.log("Start optimizing:");
            this.unbounded = !this.algo.optimize();
            this.recount(false);
            this.optimized = true;
            SimplexLogger.log("Optimal : " + (this.unbounded ? "NO (unbounded) " : "OK"));
            SimplexLogger.log(this.getPrintable());
        } catch (SimplexException e) {
            this.optimizationException = e;
            throw e;
        }
        
        return this;
    }

    public boolean isOptimized() {
        return optimized;
    }
    
    public boolean isUnbounded(){
        return unbounded;
    }
    
    public boolean isOptimal(){
        return !unbounded;
    }
    public double getMinFunctionResult(){
        return this.rows[0][this.getBIndex()];
    }

    public double[] getSolution() throws SimplexException {
        double[] res = new double[this.variableCount];
        for (int i = 1; i < this.variableCount; i++) {
            res[i] = 0;
        }
        for (int i = 1; i < this.rowsCount; i++) {
            if (this.inBase[i] >= 0) {
                res[this.inBase[i]] = this.rows[i][this.getBIndex()];
            }
        }
        return res;
    }

}
