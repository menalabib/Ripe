@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  RipePoster startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and RIPE_POSTER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\RipePoster-1.2.1.jar;%APP_HOME%\lib\converter-gson-2.6.0.jar;%APP_HOME%\lib\azure-client-runtime-1.6.10.jar;%APP_HOME%\lib\client-runtime-1.6.10.jar;%APP_HOME%\lib\converter-jackson-2.5.0.jar;%APP_HOME%\lib\adapter-rxjava-2.4.0.jar;%APP_HOME%\lib\retrofit-2.6.0.jar;%APP_HOME%\lib\logging-interceptor-3.11.0.jar;%APP_HOME%\lib\okhttp-urlconnection-3.11.0.jar;%APP_HOME%\lib\okhttp-4.0.1.jar;%APP_HOME%\lib\okio-2.2.2.jar;%APP_HOME%\lib\azure-client-authentication-2.0.0-java7-beta1.jar;%APP_HOME%\lib\adal4j-1.1.2.jar;%APP_HOME%\lib\gson-2.8.5.jar;%APP_HOME%\lib\azure-storage-blob-11.0.1.jar;%APP_HOME%\lib\slf4j-simple-2.0.0-alpha0.jar;%APP_HOME%\lib\azure-client-runtime-2.0.0-java7-beta1.jar;%APP_HOME%\lib\client-runtime-2.1.1.jar;%APP_HOME%\lib\slf4j-api-2.0.0-alpha0.jar;%APP_HOME%\lib\rxjava-3.0.0-RC1.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.9.6.jar;%APP_HOME%\lib\jackson-dataformat-xml-2.9.6.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.9.6.jar;%APP_HOME%\lib\jackson-datatype-joda-2.9.8.jar;%APP_HOME%\lib\jackson-databind-2.9.8.jar;%APP_HOME%\lib\jackson-core-2.9.9.jar;%APP_HOME%\lib\kotlin-stdlib-1.3.40.jar;%APP_HOME%\lib\netty-handler-4.1.28.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.28.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.28.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.28.Final.jar;%APP_HOME%\lib\netty-codec-4.1.28.Final.jar;%APP_HOME%\lib\netty-transport-4.1.28.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.28.Final.jar;%APP_HOME%\lib\rxjava-2.2.0.jar;%APP_HOME%\lib\azure-annotations-1.7.0.jar;%APP_HOME%\lib\reactive-streams-1.0.2.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.3.40.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\netty-resolver-4.1.28.Final.jar;%APP_HOME%\lib\netty-common-4.1.28.Final.jar;%APP_HOME%\lib\jackson-annotations-2.9.0.jar;%APP_HOME%\lib\woodstox-core-5.0.3.jar;%APP_HOME%\lib\stax2-api-3.1.4.jar;%APP_HOME%\lib\guava-20.0.jar;%APP_HOME%\lib\oauth2-oidc-sdk-4.5.jar;%APP_HOME%\lib\commons-lang3-3.4.jar;%APP_HOME%\lib\rxjava-1.3.8.jar;%APP_HOME%\lib\commons-codec-1.10.jar;%APP_HOME%\lib\joda-time-2.7.jar;%APP_HOME%\lib\mail-1.4.7.jar;%APP_HOME%\lib\nimbus-jose-jwt-3.1.2.jar;%APP_HOME%\lib\jcip-annotations-1.0.jar;%APP_HOME%\lib\lang-tag-1.4.jar;%APP_HOME%\lib\json-smart-1.1.1.jar;%APP_HOME%\lib\activation-1.1.jar;%APP_HOME%\lib\bcprov-jdk15on-1.51.jar

@rem Execute RipePoster
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %RIPE_POSTER_OPTS%  -classpath "%CLASSPATH%" Ripe.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable RIPE_POSTER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%RIPE_POSTER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
