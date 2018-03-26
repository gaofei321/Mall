@echo off

setlocal

cd lib

:startservice

java   -Xmx512m -Xms512m -Xmn128m  -XX:ReservedCodeCacheSize=32m  -classpath flexjson-2.0.jar;EbayProductTools.jar;javassist-3.15.0-GA.jar;commons-logging-1.2.jar;slf4j-api-1.6.1.jar;antlr-2.7.7.jar;commons-beanutils-1.8.3.jar;commons-codec-1.5.jar;commons-dbcp-1.3.jar;commons-io-1.4.jar;commons-pool-1.5.4.jar;commons-httpclient-3.0.1.jar;hibernate-jpa-2.0-api-1.0.1.Final.jar;dom4j-1.6.1.jar;sqljdbc4.jar;mchange-commons-java-0.2.3.4.jar;taobao-sdk-java-auto_1444958838375-20151110.jar;taobao-sdk-java-auto_1444958838375-20151110-source.jar;json-20090211.jar;druid-1.0.2.jar;jtds-1.3.1.jar;123.jar;wishMessage.jar   com.allroot.wishMessage.WishMessageThread

if errorlevel 1 goto startservice

pause