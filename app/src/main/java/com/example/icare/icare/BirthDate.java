/*
 * Copyright (c) 2017.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.icare.icare;import java.util.Date;

/**
 * BirthDate - represents the Old Person birth date
 */

public final class BirthDate {
        private static BirthDate instanceOfBirthDate = null;
        private static int day;
        private static int month;
        private static int year;

        private BirthDate() {}

        public static void setInstance(int day, int month, int year) {
                if (instanceOfBirthDate == null) {
                        instanceOfBirthDate = new BirthDate();
                }
                instanceOfBirthDate.day = day;
                instanceOfBirthDate.month = month;
                instanceOfBirthDate.year = year;
        }

        public static BirthDate getInstance() {
                return instanceOfBirthDate;
        }

        public static int getDay() {
                return day;
        }

        public static int getMonth() {
                return month;
        }

        public static int getYear() {
                return year;
        }
}
