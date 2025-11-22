@echo off
del *.class
timeout 1
javac -Xlint:deprecation MyFrame.java
javac -Xlint:deprecation MyFrame.java 