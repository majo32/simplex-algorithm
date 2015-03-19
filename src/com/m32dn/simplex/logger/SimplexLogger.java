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
package com.m32dn.simplex.logger;

/**
 *
 * @author majo
 */
public class SimplexLogger {

    private static StringBuilder loggedText = new StringBuilder();
    private static boolean active = false;

    public static void enable() {
        active = true;
    }

    public static void disable() {
        active = false;
    }

    public static void log(String msg) {
        if (active) {
            loggedText.append(msg);
            loggedText.append("\n");
        }
    }

    public static String getLoggedText() {
        return loggedText.toString();
    }

    public static void clear() {
        loggedText = new StringBuilder();
    }
}
