/* 
 Copyright (c) 2012 Shaya Potter <spotter@gmail.com>
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class BestBefore {
    static class ymd implements Comparable<ymd> {
        int day;
        int month;
        int year;
        
        public boolean valid() {
            int[] days = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            
            if (day <= 0 || day > 31) {
                return false;
            }
            if (month <= 0 || month > 12) {
                return false;
            }
            
            if (day > days[month]) {
                return false;
            }
            
            if (month == 2 && day == 29) {
                if ((year % 4) == 0) {
                    if ((year % 100) == 0 && (year % 400) != 0) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            
            if (! (obj instanceof ymd))
                return false;
            
            ymd other = (ymd) obj;
            
            return (year == other.year && month == other.month && day == other.day);
        }
        
        @Override
        public int hashCode() {
            String a = "" + year + month + day;
            return a.hashCode();
        }
        
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            
            b.append(year).append("-");

            if (month < 10) {
                b.append("0");
            }
            b.append(month).append("-");

            if (day < 10) {
                b.append("0");
            }
            b.append(day);
            
            return b.toString();
        }

        @Override
        public int compareTo(ymd other) {
            if (year < other.year)
                return -1;
            else if (year > other.year)
                return 1;
            else if (month < other.month)
                return -1;
            else if (month > other.month)
                return 1;
            else if (day < other.day)
                return -1;
            else if (day > other.day)
                return 1;
            
            return 0;
        }   
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        String input = in.readLine();
        
        ArrayList<String> tokens = new ArrayList<String>(3);
    
        StringTokenizer st = new StringTokenizer(input, "/");
        
        if (st.countTokens() != 3) {
            System.out.println(input + " is illegal");
            return;
        }
        
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        
        TreeSet<ymd> ymds = new TreeSet<ymd>();
        
        for(int i=0; i < 3; i++) {
            for(int j=0; j < 3; j++) {
                if (j == i) {
                    continue;
                }
                for(int k=0; k < 3; k++) {
                    if (k == i || k == j) {
                        continue;
                    }
                    
                    ymd date = new ymd();
                    date.year = Integer.parseInt(tokens.get(i));

                    if (date.year < 2000) {
                        date.year += 2000;   
                    }
                    
                    date.month = Integer.parseInt(tokens.get(j));
                    date.day = Integer.parseInt(tokens.get(k));
                    
                    if (date.valid()) {
                        ymds.add(date);
                    }
                }
            }
        }
        
        if (ymds.size() == 0) {
            System.out.println(input + " is illegal");
        } else {
            System.out.println(ymds.first());
        }
    }
}