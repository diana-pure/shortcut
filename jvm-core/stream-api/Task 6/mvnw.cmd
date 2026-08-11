@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF).
@REM Maven Wrapper bootstrap script for Windows.
@REM При первом запуске автоматически скачивает Maven 3.9.6.
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET "BASE_DIR=%~dp0") ELSE (SET "BASE_DIR=%__MVNW_ARG0_NAME__%")

@SET MAVEN_WRAPPER_PROPERTIES=%BASE_DIR%.mvn\wrapper\maven-wrapper.properties
@SET MAVEN_WRAPPER_JAR=%BASE_DIR%.mvn\wrapper\maven-wrapper.jar
@SET MAVEN_USER_HOME=%USERPROFILE%\.m2

@FOR /F "usebackq tokens=1,* delims==" %%A IN ("%MAVEN_WRAPPER_PROPERTIES%") DO (
  @IF "%%A"=="distributionUrl" SET "DISTRIBUTION_URL=%%B"
  @IF "%%A"=="wrapperUrl" SET "WRAPPER_URL=%%B"
)

@FOR %%F IN ("%DISTRIBUTION_URL%") DO SET "DIST_FULL=%%~nF"
@SET "DIST_NAME=%DIST_FULL:-bin=%"
@SET "MAVEN_HOME=%MAVEN_USER_HOME%\wrapper\dists\%DIST_NAME%"
@SET "MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd"

@IF NOT EXIST "%MAVEN_WRAPPER_JAR%" (
  @ECHO Downloading Maven Wrapper JAR...
  @powershell -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%MAVEN_WRAPPER_JAR%' }" || GOTO error
)

@IF NOT EXIST "%MAVEN_CMD%" (
  @ECHO Downloading Maven...
  @MKDIR "%MAVEN_HOME%" 2>NUL
  @SET "MAVEN_ZIP=%MAVEN_HOME%\maven.zip"
  @powershell -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile '%MAVEN_ZIP%' }" || GOTO error
  @powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%MAVEN_HOME%'" || GOTO error
  @DEL "%MAVEN_ZIP%"
  @FOR /D %%D IN ("%MAVEN_HOME%\apache-maven-*") DO (
    @XCOPY "%%D\*" "%MAVEN_HOME%\" /E /I /Q >NUL
    @RMDIR /S /Q "%%D"
  )
  @ECHO Maven installed to %MAVEN_HOME%
)

@IF "%JAVA_HOME%"=="" (
  @WHERE java >NUL 2>&1 || (ECHO ERROR: JAVA_HOME not set and java not in PATH & GOTO error)
)

@"%MAVEN_CMD%" %*
@GOTO end

:error
@EXIT /B 1

:end
@EXIT /B %ERRORLEVEL%

