
Set objWShell1 = CreateObject("Wscript.Shell") 
objWShell1.Run "cmd /c WifiConnect.bat", vbHide

WScript.Sleep 1000

Set objWShell2 = CreateObject("Wscript.Shell") 
objWShell2.Run "cmd /c WifiConnect.bat", vbHide
