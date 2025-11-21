@echo off
setlocal

set LOOPCOUNT=1
:loop
set A=0

:back
ping -n 1 www.google.com > nul
if %errorlevel% neq 0 (
    if %A% neq 1 (
        echo Wi-FI ‚ÉÚ‘±’†
        netsh wlan connect name=AA101248387S > nul
        set A=1
    )
    goto back
) else (
    if %A% equ 0 (
        echo Wi-FI ‚ÍŠù‚ÉÚ‘±‚³‚ê‚Ä‚¢‚Ü‚·
    ) ELSE (
        echo ÄÚ‘±‚µ‚Ü‚µ‚½
    )
)

timeout 2
set /a LOOPCOUNT+=1
if %LOOPCOUNT% leq 300000 (goto loop)

endlocal