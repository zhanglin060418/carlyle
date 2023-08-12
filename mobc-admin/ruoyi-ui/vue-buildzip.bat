@echo off

echo.
echo -- mgt build script[ npm run build:prod ]  --
echo start zip if can't work please run npm install archiver
echo.

call node zip.js

echo.
echo all done ,Good Good Good
echo.

echo.
echo please upload mgt.zip to product server path /software/vue/mgt/ also and run unzip command
echo.

pause