package org.semtech;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

//import static com.sun.org.apache.xml.internal.security.Init.getResource;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toSet;
import java.io.IOException;


public class Population {

    private static Integer NOPOPULATION = -1;
    private static String NOCITY = "";
     static class PopObj {
        String k;
        Integer p;
        String c;
        List<PopObj> v = new ArrayList<PopObj>();


        public PopObj() {
            this.k = "";
            this.c = "";
            this.p = 0;
        }
        public PopObj(String[] di) {
            switch(di.length) {
                case 0:
                    this.k = NOCITY;
                    this.p = NOPOPULATION;
                    this.c = NOCITY;
                    break;
                case 1:
                    this.k = di[0];
                    this.c = NOCITY;
                    this.p = NOPOPULATION;
                    break;
                case 2:
                    this.k = di[0];
                    this.c = NOCITY;
                    this.p = NOPOPULATION;
                case 3:
                    this.k = di[0];
                    this.c = NOCITY;
                    this.p = (di[2] == null || di[2].equals(""))?NOPOPULATION:Integer.valueOf(di[2]);
                    break;
                case 4:
                    this.k = di[0];
                    this.c =  (di[3] == null || di[3].equals(""))?NOCITY:di[3];
                    this.p = (di[2] == null || di[2].equals(""))?NOPOPULATION:Integer.valueOf(di[2]);
                default:
                    break;
            }
//             System.out.println(this.k +":" + this.c +":" + this.p );
//             System.out.flush();
        }


        public String getK() {
            return this.k;
        }

        public Integer  getP() {
            return this.p;
        }

        public String  getC() {
            return this.c;
        }

        public  List<PopObj> getV() {
            return this.v;
        }

        @Override
        public String toString() {
            return " (" + k + "," + v.toString() + ")";
        }

        public String getResourcePath(String file) {
            String resourcepath = this.getClass().getClassLoader().getResource(file).getPath().toString();
            resourcepath = resourcepath.replace("/C:","C:");
            return resourcepath;
        }
    }

    static class CityPop {
        public CityPop(String k, Integer v) {
            this.k = k;
            this.v = v;
        }

        String k;
        Integer v;

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
            return " (" + k + "," + v + ")";
        }
    }

    private static Map<String, Integer> totalPopByDept;
    private static  Map<String, Integer>  maxPopByDept;
    private static Map<Integer, Set<String>> minPopAllDept = new HashMap<Integer, Set<String>>();

    // Employubg Stream of lines from input file get Max Population for Each Department
    public static Map<String,Integer>  getMaxPopulationByDept(List<String> lines) {
        maxPopByDept = lines.stream()
                .map(line -> new PopObj(line.split(";")))
                .filter(e -> e.getP() != NOPOPULATION)
                .collect(groupingBy(PopObj::getC, TreeMap::new, mapping(PopObj::getP, toSet())))
                .entrySet().stream().map(e -> new CityPop(e.getKey(), Collections.max(e.getValue())))
                .collect(Collectors.toMap(e -> e.getK(), e -> e.getV()));
        return maxPopByDept;
    }

    // Employubg Stream of lines from input file get Total Population for Each Department
    public static Map<String,Integer>  getTotalPopulationByDept(List<String> lines) {
        totalPopByDept = lines.stream()
                .map(line -> new PopObj(line.split(";")))
                .filter(e -> e.getP() != NOPOPULATION)
                .collect(groupingBy(PopObj::getC, summingInt(PopObj::getP)));

        return  totalPopByDept;
    }

    // Employubg Stream of lines from input file get Minimum Population for All Departments
    public static void getMinPopulationForAllDepts(List<String> lines) {
        lines.stream()
                .map(line -> new PopObj(line.split(";")))
                .filter(e -> e.getP() != NOPOPULATION)
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
    }


    // Driver code Main Entry point
    public static void main(String[] args) {
        // Creating a list of Strings

        try {

            String resourcepath = new PopObj().getResourcePath(args[0]);

            //System.out.println(resourcepath);
            List<String> lines = Files.readAllLines(Paths.get(resourcepath));
            String header = lines.remove(0);

            totalPopByDept = getTotalPopulationByDept(lines);
            maxPopByDept = getMaxPopulationByDept(lines);
            getMinPopulationForAllDepts(lines);

            Integer minPopulation = Collections.min(minPopAllDept.keySet());

            maxPopByDept.entrySet().stream().forEach( e -> {
                String s = e.getKey();
                Integer mp = e.getValue();
                Integer tp = totalPopByDept.get(s);
                System.out.println("Department " + s + ", Total Population " +  tp.toString() + ", Max Population " + mp.toString());
            });
            System.out.println("\n" + minPopAllDept.get(minPopulation).toString() + " found with  Mininum Population " +  minPopulation.toString() + " in all Departments");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

