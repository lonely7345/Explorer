/*
 *  Licensed to STRATIO (C) under one or more contributor license agreements.
 *  See the NOTICE file distributed with this work for additional information
 *  regarding copyright ownership.  The STRATIO (C) licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
ace.define("ace/mode/gitignore_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(e,t,n){"use strict";var r=e("../lib/oop"),i=e("./text_highlight_rules").TextHighlightRules,s=function(){this.$rules={start:[{token:"comment",regex:/^\s*#.*$/},{token:"keyword",regex:/^\s*!.*$/}]},this.normalizeRules()};s.metaData={fileTypes:["gitignore"],name:"Gitignore"},r.inherits(s,i),t.GitignoreHighlightRules=s}),ace.define("ace/mode/gitignore",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/gitignore_highlight_rules"],function(e,t,n){"use strict";var r=e("../lib/oop"),i=e("./text").Mode,s=e("./gitignore_highlight_rules").GitignoreHighlightRules,o=function(){this.HighlightRules=s};r.inherits(o,i),function(){this.$id="ace/mode/gitignore"}.call(o.prototype),t.Mode=o})