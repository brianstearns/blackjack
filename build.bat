@echo off
echo Cleaning old builds...
if exist blackjack.jar del blackjack.jar
rmdir /s /q out
rmdir /s /q temp
mkdir out

echo Generating list of Java files...
dir /s /b src\*.java > sources.txt

echo Compiling...
javac -cp "libs/*" -d out @sources.txt

mkdir temp
for %%l in (libs\*.jar) do (
    cd temp
    jar xf ..\%%l
    cd ..
)
xcopy out temp /E /I /Q
jar cfe blackjack.jar app.GameRunner -C temp .

:: --- Cleanup ---
del sources.txt
rmdir /s /q out
rmdir /s /q temp

echo Build complete! Run with:
echo java -jar blackjack.jar
pause