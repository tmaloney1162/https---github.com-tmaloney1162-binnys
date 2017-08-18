@echo off

set JAVA_HOME=C:\GP\jre7\bin
set CLASSPATH=.;C:\GP\bin;C:\GP\lib\pdfbox-app-2.0.6.jar;
set RUN_DATE=%2%1
set LABEL_TYPE=%3

REM Split, create XML, call FO bat file all in 1 java program
rem echo %JAVA_HOME%\java -Xms128M -Xmx128M CreateXml %RUN_DATE% %LABEL_TYPE%
%JAVA_HOME%\java -Xms128M -Xmx128M CreateXml %RUN_DATE% %LABEL_TYPE%

rem Back to the bin directory
cd C:\GP\bin
