@echo off

set texFileName=rep-motor-2022311042-2.tex
set dviFileName=%texFileName:.tex=.dvi%
set pdfFileName=%texFileName:.tex=.pdf%

platex %texFileName%
platex %texFileName%
dvipdfmx %dviFileName%
pdfopen --file %pdfFileName%
timeout /t 15 /nobreak >nul
pdfclose --file %pdfFileName%
taskkill /im Acrobat.exe /f
exit /b 0
