
rmdir /s /q d:\somethingsomething\webapps\ias
del d:\somethingsomething\webapps\ias.war
rmdir /s /q d:\somethingsomethingwhere\webapps\ias_middleware
del d:\somethingsomethingwhere\webapps\ias_middleware

move ias.war D:\something\
move ias_middleware.war D:\somethingwhere\

call D:\somebin\shutdown.bat
call D:\somebin\shutdown.bat

start d:\somebin\startup.bat
start d:\somebin\startup.bat