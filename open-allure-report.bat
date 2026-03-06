@echo off
cd /d "%~dp0"
.allure\allure-2.20.1\bin\allure.bat open target\allure-report
