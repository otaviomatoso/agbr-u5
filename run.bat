@ECHO OFF

REM START NODE-RED
start cmd /c cd .\node-red ^& call "node-red_run.bat"

REM START JACAMO
start cmd /c cd .\jacamo ^& mas_run.bat
