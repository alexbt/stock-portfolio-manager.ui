set db=jdbc:hsqldb:file:C:\Users\Alex\workspaces\workspaceStockPortfolio\stock-portfolio-manager.tests\src\test\resources\StockPortfolio_0.21Beta\data\db
set hsqldbjar=C:\Users\Alex\workspaces\workspaceStockPortfolio\stock-portfolio-manager.ui\target\lib\hsqldb-2.3.2.jar

java -jar %hsqldbjar% -user sa -url %db%