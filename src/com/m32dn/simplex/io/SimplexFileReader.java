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
package com.m32dn.simplex.io;

import com.m32dn.simplex.tableaux.NonCannonicalTableaux;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author majo
 */
public class SimplexFileReader {

    private final BufferedReader file;
    private double[][] rows;

    public SimplexFileReader(String filename) throws FileNotFoundException, IOException {
        this.file = new BufferedReader(new FileReader(filename));
        read();

    }

    private void read() throws IOException {
        List<List<Double>> arr = new ArrayList();
        try {
            String line = file.readLine();
            String[] parts;
            while (line != null) {
                
                parts = line.split(" ");
                arr.add(new ArrayList());
                for (String p : parts) {
                    if("".equals(p)){
                        continue;
                    }
                    arr.get(arr.size() - 1).add(Double.parseDouble(p));
                }
                line = file.readLine();
            }
            this.rows = new double[arr.size()][arr.get(0).size()];
            for (int i = 0; i< arr.size() ;i++) {
                for (int j = 0; j< arr.get(0).size() ;j++) {
                    this.rows[i][j] = arr.get(i).get(j);
                }
            }
        } finally {
            file.close();
        }
    }

    public NonCannonicalTableaux getTableaux() {
        NonCannonicalTableaux t = new NonCannonicalTableaux(this.rows);
        return t;
    }

}
