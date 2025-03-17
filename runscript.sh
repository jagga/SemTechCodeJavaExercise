
export JAVA_HOME='/c/Program Files/Java/jdk1.8.0_361'
mvn clean install test -Dtest=runTestSuite -DfailIfNoTests=false -Dsurefire.failIfNoSpecifiedTests=false
java   -Dfile.encoding=UTF-8 -classpath  'C:\Users\19082\SemTechProject\target\classes' org.semtech.Population $1 

