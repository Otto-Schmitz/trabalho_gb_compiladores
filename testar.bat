@echo off
REM ========================================
REM   COMPILAR E TESTAR ANALISADOR LEXICO
REM ========================================

cd /d "%~dp0"

set JFLEX_JAR=lib\jflex-1.8.2.jar
set CLASSPATH=.;%JFLEX_JAR%;src;src\testes;src\lexico

echo ========================================
echo   COMPILACAO
echo ========================================
echo.

echo [1/2] Compilando arquivos...
javac -cp "%CLASSPATH%" src\lexico\*.java src\*.java src\testes\*.java

if errorlevel 1 (
    echo.
    echo ERRO: Falha na compilacao!
    pause
    exit /b 1
)

echo [2/2] Compilacao concluida com sucesso!
echo.

echo ========================================
echo   EXECUTANDO TESTES
echo ========================================
echo.

java -cp "%CLASSPATH%" TesteSuite

echo.
echo ========================================
echo   TESTES CONCLUIDOS
echo ========================================
echo.
pause
