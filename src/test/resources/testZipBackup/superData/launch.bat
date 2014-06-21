echo off

set logfile=launch.log
echo -- >> %logfile%
echo %date%, %time% >> %logfile%
echo set classpath=.;StockPortfolioLauncher.jar >> %logfile%

echo java -jar StockPortfolioLauncher.jar >> %logfile%


echo on
set classpath=.;StockPortfolioLauncher.jar 2>> %logfile%

set path=C:\Program Files\Java\jdk1.6.0_45\bin;%path%
java -jar StockPortfolioLauncher.jar 2>> %logfile%


pause