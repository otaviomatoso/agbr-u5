@ECHO OFF

start cmd /c gradlew clean compileJava copyToLib ^& docker build -t agbr/u5:0.1 .
