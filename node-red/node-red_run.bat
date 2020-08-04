@ECHO OFF

REM START NODE-RED
start cmd /c node-red flows\otm2.json
REM TIMEOUT 3
rem start cmd /c cd .\flows ^& curl -v -X POST http://localhost:1880/flows -H "Content-Type: application/json"  --data "@otm.json"
