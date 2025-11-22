@echo off
if "%1"=="" (
    echo 引数が不足しています。Tex ファイルの名前を指定してください。
    exit /b 1
)

set texFileName=%1
set dviFileName=%texFileName:.tex=.dvi%
set pdfFileName=%texFileName:.tex=.pdf%

set "counter=0"
:loop
if %counter% lss 1000 (
platex %texFileName%
platex %texFileName%
dvipdfmx %dviFileName%
pdfopen --file %pdfFileName%
timeout /t 60 /nobreak >nul
pdfclose --file %pdfFileName%
    set /a "counter+=1"
    goto :loop
)
exit /b 0
