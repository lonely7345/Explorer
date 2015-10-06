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

@echo off
copy src\intro.js /B + src\core.js /B + src\tooltip.js /B temp1.js /B
copy src\models\*.js /B temp2.js /B
copy temp1.js /B + temp2.js /B + src\outro.js /B nv.d3.js /B
del temp1.js
del temp2.js
