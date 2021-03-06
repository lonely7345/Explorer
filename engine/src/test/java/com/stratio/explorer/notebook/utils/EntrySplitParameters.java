/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.explorer.notebook.utils;


public class EntrySplitParameters {


   public  String str = "";
   public  String escapeSeq = "";
   public  char escapeChar =' ';
   public  String [] blockStart = new String[]{};
   public  String [] blockEnd = new String[]{};
   public  String [] splitters = new String[]{};
   public  boolean includeSplitter = false;
}
