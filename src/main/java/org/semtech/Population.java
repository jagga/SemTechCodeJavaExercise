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

    // Driver code Main Entry point
    public static void main(String[] args) {
        // Creating a list of Strings

        try {
            Map<String, Integer> totalPopByDept;
            Map<String, Integer> maxPopByDept;
            Map<Integer, Set<String>> minPopAllDept;//= new HashMap<Integer, Set<String>>();
            LinesStreamProcess linesStream = new LinesStreamProcess();

            String resourcepath =   linesStream.getResourcePath(args[0]);

            //System.out.println(resourcepath);
            List<String> lines = Files.readAllLines(Paths.get(resourcepath));
            //System.out.println(lines.toString());
            String header = lines.remove(0);

            List<PopObj> objs =  linesStream.getAllPopObjs(lines);
            totalPopByDept = linesStream.getTotalPopulationByDept(objs);
            maxPopByDept = linesStream.getMaxPopulationByDept(objs);
            minPopAllDept = linesStream.getMinPopulationForAllDepts(objs);

            Integer minPopulation = Collections.min(minPopAllDept.keySet());
            Object[] strArr =  maxPopByDept.keySet().toArray();
            Arrays.sort(strArr);
            for (Object s : strArr) {
                Integer mp = maxPopByDept.get(s.toString());
                Integer tp = totalPopByDept.get(s.toString());
                System.out.println("Department " + s.toString() + ", Total Population " +  tp.toString() + ", Max Population " + mp.toString());
            };
            System.out.println("\n" + minPopAllDept.get(minPopulation).toString() + " found with  Mininum Population " +  minPopulation.toString() + " in all Departments");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

