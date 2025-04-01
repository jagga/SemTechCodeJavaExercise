package org.semtech;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PopulationTest {
    private static String testInputFile = "population_2019_500";

    private static LinesStreamProcess lineStrmObj = new LinesStreamProcess();
    //private static String[] lines;
    private static String result;

    private static Map<String, Integer> totalPopByDept;
    private static Map<String, Integer> maxPopByDept;
    private static Map<Integer, Set<String>> minPopAllDept; //= new HashMap<Integer, Set<String>>();

    private static Map<String, Integer> totalPopByDeptT;
    private static Map<String, Integer> maxPopByDeptT;
    private static Map<Integer, Set<String>> minPopAllDeptT; // = new HashMap<Integer, Set<String>>();

    //System.out.println(resourcepath);
    List<String> lines ; //= null;
    public List<String> getPopObjects() {
        try {
            lines = Files.readAllLines(Paths.get(testInputFile));
            //System.out.println(lines.toString());
            String header = lines.remove(0);
        } catch (Exception e) {
        }
        return lines;
    }

    public String getTestResourcePath(String file) {
        String resourcepath = this.getClass().getClassLoader().getResource(file).getPath().toString();
        resourcepath = resourcepath.replace("/C:", "C:");
        return resourcepath;
    }

    public String getMainResourcePath(String file) {
        String resourcepath = lineStrmObj.getClass().getClassLoader().getResource(file).getPath().toString();
        resourcepath = resourcepath.replace("/C:", "C:");
        return resourcepath;
    }

    List<String> setUpLines(String testInputFile, String restype) {
        try {
            String resourcepath = restype.equals("test") ? getTestResourcePath(testInputFile) : getMainResourcePath(testInputFile);
            System.out.println(resourcepath);
            lines = Files.readAllLines(Paths.get(resourcepath));
            String header = lines.remove(0);
            return lines;
        } catch (Exception e) {
            lines = Arrays.asList(";;;");
            return null;
        }
    }

    void getMaxPopulationResults() {
        List<String> lines = setUpLines("population_2019.csv", "main");
        List<PopObj> objs =  lineStrmObj.getAllPopObjs(lines);
        totalPopByDept = lineStrmObj.getTotalPopulationByDept(objs);
        maxPopByDept = lineStrmObj.getMaxPopulationByDept(objs);
        minPopAllDept = lineStrmObj.getMinPopulationForAllDepts(objs);
    }

    void setup() {
        getMaxPopulationResults();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getMaxPopulationByDeptTest() {
        getMaxPopulationResults();
        lines = setUpLines("maxpopbydept.csv","test");
        List<PopObj> objs =  lineStrmObj.getAllPopObjs(lines);
        maxPopByDeptT = lineStrmObj.getMaxPopulationByDept(objs);
        assertEquals(maxPopByDept.get("MANCHE"), maxPopByDeptT.get("MANCHE"));
        assertEquals(maxPopByDept.get("GIRONDE"), maxPopByDeptT.get("GIRONDE"));
        assertEquals(maxPopByDept.get("COTE-D'OR"), maxPopByDeptT.get("COTE-D'OR"));
        assertEquals(maxPopByDept.get("COTES-D'ARMOR"), maxPopByDeptT.get("COTES-D'ARMOR"));
    }

    @Test
    void getTotalPopulationByDeptTest() {
        getMaxPopulationResults();
        lines = setUpLines("totpopbydept.csv","test");
        List<PopObj> objs =  lineStrmObj.getAllPopObjs(lines);
        totalPopByDeptT = lineStrmObj.getTotalPopulationByDept(objs);

        assertEquals(totalPopByDept.get("MANCHE"), totalPopByDeptT.get("MANCHE"));  //        14	4811
        assertEquals(totalPopByDept.get("GIRONDE"), totalPopByDeptT.get("GIRONDE"));  //        15	1316
        assertEquals(totalPopByDept.get("COTE-D'OR"), totalPopByDeptT.get("COTE-D'OR"));  //        16	4619
        assertEquals(totalPopByDept.get("COTES-D'ARMOR"), totalPopByDeptT.get("COTES-D'ARMOR"));  //
        assertNotEquals(totalPopByDept.get("CALVADOS"), totalPopByDeptT.get("CALVADOS"));  //        14	4811
        assertNotEquals(totalPopByDept.get("CANTAL"), totalPopByDeptT.get("CANTAL"));  //        15	1316
        assertNotEquals(totalPopByDept.get("CHARENTE"), totalPopByDeptT.get("CHARENTE"));  //        16	4619
        assertNotEquals(totalPopByDept.get("CHARENTE-MARITIME"), totalPopByDeptT.get("CHARENTE-MARITIME"));  //        17	2849
    }

    @Test
    void getMinPopulationForAllDepts() {
        lines = setUpLines("minpopbyalldept.csv","test");
        Integer minExpectedPop = 19;
        List<PopObj> objs =  lineStrmObj.getAllPopObjs(lines);
        minPopAllDeptT = lineStrmObj.getMinPopulationForAllDepts(objs);
        Integer minPopulation = Collections.min(minPopAllDeptT.keySet());
        assertEquals(minPopulation.toString(), minExpectedPop.toString());
    }

    public static void main(String[] argv) {
        PopulationTest  poptest = new  PopulationTest();
    }

}
