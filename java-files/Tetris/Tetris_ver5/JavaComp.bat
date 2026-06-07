@echo off
timeout 1
del *.class
timeout 1
javac -Xlint:deprecation MyFrame.java
javac -Xlint:deprecation MyFrame.java
jar cvfm Game.jar Manifest.txt *.class
timeout 15