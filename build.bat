@echo off
echo Cleaning old builds...
rmdir /s /q out
rmdir /s /q temp

echo Compiling...
javac -cp "libs/*" -d out src/*.java

echo Creating temp folder...
mkdir temp

echo Extracting libraries...
cd temp
jar xf ..\libs\gson-2.10.1.jar
cd ..

echo Copying classes...
xcopy out temp /E /I

echo Building jar...
jar cfe blackjack.jar GameRunner -C temp .

echo Done!
pause