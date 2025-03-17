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
            Map<Integer, Set<String>> minPopAllDept = new HashMap<Integer, Set<String>>();
            LinesStreamProcess linesStream = new LinesStreamProcess();

            String resourcepath =   linesStream.getResourcePath(args[0]);

            //System.out.println(resourcepath);
            List<String> lines = Files.readAllLines(Paths.get(resourcepath));
            String header = lines.remove(0);

            totalPopByDept = linesStream.getTotalPopulationByDept(lines);
            maxPopByDept = linesStream.getMaxPopulationByDept(lines);
            minPopAllDept = linesStream.getMinPopulationForAllDepts(lines);

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

