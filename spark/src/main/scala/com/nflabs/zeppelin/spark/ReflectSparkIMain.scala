/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.nflabs.zeppelin.spark

import scala.tools.nsc._
import scala.tools.nsc.interpreter._
import reporters._
import org.apache.spark.repl.SparkIMain
import scala.tools.reflect._
class ReflectSparkIMain(initialSettings: Settings, override val out: JPrintWriter) extends SparkIMain(initialSettings, out) {
	
  override def newCompiler(settings: Settings, reporter: Reporter): ReplGlobal = {
    settings.outputDirs setSingleOutput virtualDirectory
    settings.exposeEmptyPackage.value = true
    new ReflectGlobal(settings, reporter, classLoader) with ReplGlobal {
      override def toString: String = "<global>"
    }
  }
}