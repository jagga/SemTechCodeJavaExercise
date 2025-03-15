import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

public class SemTechPopulation {
    static class LPair {
        String k;
        Integer p;
        String c;
        List<LPair> v = new ArrayList<LPair>();

        public LPair() {
            this.k = "";
            this.c = "";
            this.p = 0;
        }
        public LPair(String[] di) {
            switch(di.length) {
                case 0:
                    this.k = "NONE";
                    this.p = 0;
                    this.c = "None";
                    break;
                case 1:
                    this.k = di[0];
                    this.c = "None";
                    this.p = 0;
                    break;
                case 2:
                    this.k = di[0];
                    this.c = "None";
                    this.p = 0;
                case 3:
                    this.k = di[0];
                    this.c = "None";
                    this.p = Integer.valueOf(di[2]);
                    break;
                case 4:
                    this.k = di[0];
                    this.c = di.length >= 4 ? di[3] : "None";
                    this.p = Integer.valueOf(di[2]);
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

        public  List<LPair> getV() {
            return this.v;
        }

        @Override
        public String toString() {
            return " (" + k + "," + v.toString() + ")";
        }
    }

    static class SPair {
        public SPair(String k, Integer v) {
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

    // Driver code
    public static void main(String[] args) {
        // Creating a list of Strings

        String CSV_FILE = "C:\\Users\\19082\\javaMavenProj\\src\\main\\resources\\population_2019.csv";

        Map<String, Integer>  totalPopByDept;
        Map<String, Integer>  maxPopByDept;
        Map<Integer, Set<String>> minPopAllDept = new HashMap<Integer, Set<String>>();
        try {

            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
            String header = lines.remove(0);
            totalPopByDept  = lines.stream()
                    .map(line ->  new LPair(line.split(";")))
                    .collect(groupingBy(LPair::getC, summingInt(LPair::getP)));

            maxPopByDept  = lines.stream()
                    .map(line ->  new LPair(line.split(";")))
                    .collect(groupingBy(LPair::getC, TreeMap::new, mapping(LPair::getP, toSet())))
                    .entrySet().stream().map(e -> new SPair(e.getKey(), Collections.max(e.getValue())))
                    .collect( Collectors.toMap(e -> e.getK(), e -> e.getV()));

            lines.stream()
                    .map(line ->  new LPair(line.split(";")))
                    .collect(groupingBy(LPair::getC, TreeMap::new, mapping(LPair::getP, toSet())))
                    .entrySet().stream().map(e -> new SPair(e.getKey(), Collections.min(e.getValue())))
                    .forEach(e -> {
                        if (!(e.getV() <= 0)) {
                            Set<String> val = minPopAllDept.get(e.getV());
                            if (val == null) {
                                Set<String> st = new HashSet<String>();
                                st.add(e.getK());
                                minPopAllDept.put(e.getV(), st);
                            } else {
                                val.add(e.getK());
                                minPopAllDept.put(e.getV(), val);
                            }
                        }
                    });
                    //.collect( Collectors.toMap(e -> e.getV(), e -> e.getK()));
                    Integer minPopulation = Collections.min(minPopAllDept.keySet());

            maxPopByDept.entrySet().stream().forEach( e -> {
                //System.out.println(e.getKey());
                String s = e.getKey();
                Integer mp = e.getValue();
                Integer tp = totalPopByDept.get(s);
                System.out.println("Department " + s + ", Total Population " +  tp.toString() + ", Max Population " + mp.toString());
            });
           System.out.println("In All Departments " + minPopAllDept.get(minPopulation).toString() + " with  Mininum Population " +  minPopulation.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

