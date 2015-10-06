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
import org.apache.spark.repl.SparkILoop
import org.apache.spark.repl.SparkIMain
import org.apache.spark.util.Utils
import java.io.BufferedReader
import scala.tools.nsc.util.{ ClassPath, Exceptional, stringFromWriter, stringFromStream }


class ReflectSparkILoop(in0: Option[BufferedReader], override protected val out: JPrintWriter, override val master: Option[String])
	  extends SparkILoop(in0, out, master) {
  def this(in0: BufferedReader, out: JPrintWriter, master: String) = this(Some(in0), out, Some(master))
  def this(in0: BufferedReader, out: JPrintWriter) = this(Some(in0), out, None)
  def this() = this(None, new JPrintWriter(Console.out, true), None)  
  

  class ReflectSparkILoopInterpreter extends ReflectSparkIMain(settings, out) {
    outer =>

    override lazy val formatting = new Formatting {
      def prompt = ReflectSparkILoop.this.prompt
    }
    override protected def parentClassLoader = SparkHelper.explicitParentLoader(settings).getOrElse(classOf[SparkILoop].getClassLoader)
  }

  /** Create a new interpreter. */
  override def createInterpreter() {
    require(settings != null)

    if (addedClasspath != "") settings.classpath.append(addedClasspath)
    // work around for Scala bug
    val totalClassPath = SparkILoop.getAddedJars.foldLeft(
      settings.classpath.value)((l, r) => ClassPath.join(l, r))
    this.settings.classpath.value = totalClassPath

    intp = new ReflectSparkILoopInterpreter
  }
  
  /** Create a new interpreter. */
  def createReflectInterpreter(settings : Settings) : SparkIMain = {
    require(settings != null)

    if (addedClasspath != "") settings.classpath.append(addedClasspath)
    // work around for Scala bug
    val totalClassPath = SparkILoop.getAddedJars.foldLeft(
      settings.classpath.value)((l, r) => ClassPath.join(l, r))
    this.settings.classpath.value = totalClassPath

    intp = new ReflectSparkILoopInterpreter
    intp
  }
}