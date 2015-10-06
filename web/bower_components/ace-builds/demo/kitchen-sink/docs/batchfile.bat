@REM
@REM  Licensed to STRATIO (C) under one or more contributor license agreements.
@REM  See the NOTICE file distributed with this work for additional information
@REM  regarding copyright ownership.  The STRATIO (C) licenses this file
@REM  to you under the Apache License, Version 2.0 (the
@REM  "License"); you may not use this file except in compliance
@REM  with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing,
@REM  software distributed under the License is distributed on an
@REM  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM  KIND, either express or implied.  See the License for the
@REM  specific language governing permissions and limitations
@REM  under the License.
@REM

:: batch file highlighting in Ace!
@echo off

CALL set var1=%cd%
echo unhide everything in %var1%!

:: FOR loop in bat is super strange!
FOR /f "tokens=*" %%G IN ('dir /A:D /b') DO (
echo %var1%%%G
attrib -r -a -h -s "%var1%%%G" /D /S
)

pause

REM that's all
