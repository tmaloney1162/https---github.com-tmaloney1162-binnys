@echo off

set JAVA_HOME=C:\GP\jre7\bin

set CLASSPATH=.;C:\GP\bin;
set RUN_DATE=%2%1
set FO_FILE=%3
set XML_NAME=%4
set PDF_FILE=%5
set LABEL_TYPE=%6

rem convert XML to FO (using XSLT)
echo Create FO file

rem echo %JAVA_HOME%\java -Xms128M -Xmx128M transformXSLT %4 C:\GP\xsl\binnys.xsl %FO_FILE% %1 %2
%JAVA_HOME%\java -Xms128M -Xmx128M transformXSLT %4 C:\GP\xsl\binnys-%LABEL_TYPE%.xsl %FO_FILE% %1 %2

rem Generate the PDF file
cd C:\GP\XEP
echo Gen PDF

echo call xep.bat -fo %FO_FILE% %PDF_FILE%
call xep.bat -fo %FO_FILE% %PDF_FILE%

rem Back to the bin directory
cd C:\GP\bin