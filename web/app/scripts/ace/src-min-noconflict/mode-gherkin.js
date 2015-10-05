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
ace.define("ace/mode/gherkin_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(e,t,n){var r=e("../lib/oop"),i=e("./text_highlight_rules").TextHighlightRules,s="\\\\(x[0-9A-Fa-f]{2}|[0-7]{3}|[\\\\abfnrtv'\"]|U[0-9A-Fa-f]{8}|u[0-9A-Fa-f]{4})",o=function(){this.$rules={start:[{token:"constant.numeric",regex:"(?:(?:[1-9]\\d*)|(?:0))"},{token:"comment",regex:"#.*$"},{token:"keyword",regex:"Feature:|Background:|Scenario:|Scenario Outline:|Examples:|Given|When|Then|And|But|\\*"},{token:"string",regex:'"{3}',next:"qqstring3"},{token:"string",regex:'"',next:"qqstring"},{token:"comment",regex:"@[A-Za-z0-9]+",next:"start"},{token:"comment",regex:"<.+>"},{token:"comment",regex:"\\| ",next:"table-item"},{token:"comment",regex:"\\|$",next:"start"}],qqstring3:[{token:"constant.language.escape",regex:s},{token:"string",regex:'"{3}',next:"start"},{defaultToken:"string"}],qqstring:[{token:"constant.language.escape",regex:s},{token:"string",regex:"\\\\$",next:"qqstring"},{token:"string",regex:'"|$',next:"start"},{defaultToken:"string"}],"table-item":[{token:"string",regex:"[A-Za-z0-9 ]*",next:"start"}]}};r.inherits(o,i),t.GherkinHighlightRules=o}),ace.define("ace/mode/gherkin",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/gherkin_highlight_rules"],function(e,t,n){var r=e("../lib/oop"),i=e("./text").Mode,s=e("./gherkin_highlight_rules").GherkinHighlightRules,o=function(){this.HighlightRules=s};r.inherits(o,i),function(){this.lineCommentStart="#",this.$id="ace/mode/gherkin",this.getNextLineIndent=function(e,t,n){var r=this.$getIndent(t),i="  ",s=this.getTokenizer().getLineTokens(t,e),o=s.tokens;return console.log(e),t.match("[ ]*\\|")&&(r+="| "),o.length&&o[o.length-1].type=="comment"?r:(e=="start"&&(t.match("Scenario:|Feature:|Scenario Outline:|Background:")?r+=i:t.match("(Given|Then).+(:)$|Examples:")?r+=i:t.match("\\*.+")&&(r+="* ")),r)}}.call(o.prototype),t.Mode=o})