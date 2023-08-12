@echo off

echo.
echo -- h5 build script[ npm run build ]  --
echo start zip if can't work please run npm install archiver
echo.

call node zip.js

echo.
echo all done ,Good Good Good
echo.

echo.
echo please upload h5.zip to product server path /software/vue/h5/ also and run unzip command
echo.



pause