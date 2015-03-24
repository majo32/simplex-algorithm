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
package com.m32dn.simplex.example;

import com.m32dn.simplex.exception.SimplexException;
import com.m32dn.simplex.io.SimplexFileReader;
import com.m32dn.simplex.logger.SimplexLogger;
import com.m32dn.simplex.tableau.CannonicalTableau;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author majo
 */
public class SimplexResolver {
     public static void main(String[] args) {
        try {
            SimplexLogger.enable();
            
            SimplexFileReader r = new SimplexFileReader("input.txt");
            CannonicalTableau t = r.getTableaux().getCannonical();
            double [] res = t.optimize().getSolution();
            
            System.out.println(SimplexLogger.getLoggedText());
            SimplexLogger.clear();
            
            System.out.println("RESULT:");
            System.out.println("Optimal: " + (t.isUnbounded() ? "No (unbounded)" : "OK"));
            System.out.println(Arrays.toString(res));
            System.out.println("Function result: " + t.getMinFunctionResult());
            
        } catch (SimplexException | IOException ex) {
            Logger.getLogger(SimplexResolver.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(SimplexLogger.getLoggedText());
        }

    }
}
