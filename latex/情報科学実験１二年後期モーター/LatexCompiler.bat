@echo off
for %%f in (*.tex) do (
    platex "%%~nf.tex"
    platex "%%~nf.tex"
    dvipdfmx "%%~nf.dvi"        
)