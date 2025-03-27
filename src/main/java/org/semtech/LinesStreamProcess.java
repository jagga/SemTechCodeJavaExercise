package org.semtech;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toSet;

public class LinesStreamProcess {
        private static final Integer NOPOPULATION = -1;
        private static final String NOCITY = "";
        private static List<String> lines = null;

        public void setLines(List<String> lines) {
            LinesStreamProcess.lines = lines;
        }

         class PopObj {
            String k;    // Department Code
            Integer p;   // Population
            String c;    // City
            String[] di = null;

            PopObj(String indi) {
                 di = indi.split(";");
                 init();
            }
            public void init() {
                String sdi[] = {"", "", "", ""};
                for (int i=0; i<di.length; i++)  sdi[i] = di[i];
                k = (sdi[0]==null||sdi[0].equals(""))?"":sdi[0].trim();
                c = sdi[3] == null || sdi[3].equals("") ? "" : sdi[3].trim();
                p = (sdi[2] == null) ? -1 : (sdi[2].equals("")) ? 0 : Integer.valueOf(sdi[2].trim());
            }

            public String getK() {
                return this.k;
            }

            public Integer getP() {
                return this.p;
            }

            public String getC() {
                return this.c;
            }

            @Override
            public String toString() {
                return " (" + this.getC() + "," + this.getP().toString() + ", " + this.getK() + ")";
            }
        }

        static class CityPop {
            public CityPop(String k, Integer v) {
                this.k = k;
                this.v = v;
            }

            String k;  // City
            Integer v;  // Population

            public String getK() {
                return this.k;
            }

            public Integer getV() {
                return this.v;
            }

            public int getIP() {
                return this.v.intValue();
            }

            @Override
            public String toString() {
                return (" (" + this.getK() + "," + this.getV().toString() + ")");
            }
        }

        private static Map<String, Integer> totalPopByDept;
        private static Map<String, Integer> maxPopByDept;
        private static final Map<Integer, Set<String>> minPopAllDept = new HashMap<Integer, Set<String>>();

        public String getResourcePath(String file) {
            String resourcepath = this.getClass().getClassLoader().getResource(file).getPath();
            resourcepath = resourcepath.replace("/C:", "C:");
            return resourcepath;
        }
        // Employubg Stream of lines from input file get Max Population for Each Department
        public  Map<String, Integer> getMaxPopulationByDept(List<String> lines) {
            maxPopByDept = lines.stream()
                    .map(line -> new PopObj(line))
                    .filter(e -> e.getP() > NOPOPULATION)
                    .collect(groupingBy(PopObj::getC, TreeMap::new, mapping(PopObj::getP, toSet())))
                    .entrySet().stream().map(e -> new CityPop(e.getKey(), Collections.max(e.getValue())))
                    .collect(Collectors.toMap(e -> e.getK(), e -> e.getV()));
            return maxPopByDept;
        }

        // Employubg Stream of lines from input file get Total Population for Each Department
        public  Map<String, Integer> getTotalPopulationByDept(List<String> lines) {
            totalPopByDept = lines.stream()
                    .map(line -> new PopObj(line))
                    .filter(e -> e.getP() > NOPOPULATION)
                    .collect(groupingBy(PopObj::getC, summingInt(PopObj::getP)));

            return totalPopByDept;
        }

        // Employubg Stream of lines from input file get Minimum Population for All Departments
        public  Map<Integer, Set<String>> getMinPopulationForAllDepts(List<String> lines) {
            lines.stream()
                    .map(line -> new PopObj(line))
                    .filter(e -> e.getP() > NOPOPULATION)
                    .collect(groupingBy(PopObj::getC, TreeMap::new, mapping(PopObj::getP, toSet())))
                    .entrySet().stream().map(e -> new CityPop(e.getKey(), Collections.min(e.getValue())))
                    .forEach(e -> {
                        Set<String> val = minPopAllDept.get(e.getV());
                        if (val == null) {
                            Set<String> st = new HashSet<String>();
                            st.add(e.getK());
                            minPopAllDept.put(e.getV(), st);
                        } else {
                            val.add(e.getK());
                            minPopAllDept.put(e.getV(), val);
                        }
                    });
            return minPopAllDept;
        }
    }

