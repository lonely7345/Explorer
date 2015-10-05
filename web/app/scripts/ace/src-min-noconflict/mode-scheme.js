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
ace.define("ace/mode/scheme_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(e,t,n){"use strict";var r=e("../lib/oop"),i=e("./text_highlight_rules").TextHighlightRules,s=function(){var e="case|do|let|loop|if|else|when",t="eq?|eqv?|equal?|and|or|not|null?",n="#t|#f",r="cons|car|cdr|cond|lambda|lambda*|syntax-rules|format|set!|quote|eval|append|list|list?|member?|load",i=this.createKeywordMapper({"keyword.control":e,"keyword.operator":t,"constant.language":n,"support.function":r},"identifier",!0);this.$rules={start:[{token:"comment",regex:";.*$"},{token:["storage.type.function-type.scheme","text","entity.name.function.scheme"],regex:"(?:\\b(?:(define|define-syntax|define-macro))\\b)(\\s+)((?:\\w|\\-|\\!|\\?)*)"},{token:"punctuation.definition.constant.character.scheme",regex:"#:\\S+"},{token:["punctuation.definition.variable.scheme","variable.other.global.scheme","punctuation.definition.variable.scheme"],regex:"(\\*)(\\S*)(\\*)"},{token:"constant.numeric",regex:"#[xXoObB][0-9a-fA-F]+"},{token:"constant.numeric",regex:"[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?"},{token:i,regex:"[a-zA-Z_#][a-zA-Z0-9_\\-\\?\\!\\*]*"},{token:"string",regex:'"(?=.)',next:"qqstring"}],qqstring:[{token:"constant.character.escape.scheme",regex:"\\\\."},{token:"string",regex:'[^"\\\\]+',merge:!0},{token:"string",regex:"\\\\$",next:"qqstring",merge:!0},{token:"string",regex:'"|$',next:"start",merge:!0}]}};r.inherits(s,i),t.SchemeHighlightRules=s}),ace.define("ace/mode/scheme",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/scheme_highlight_rules"],function(e,t,n){"use strict";var r=e("../lib/oop"),i=e("./text").Mode,s=e("./scheme_highlight_rules").SchemeHighlightRules,o=function(){this.HighlightRules=s};r.inherits(o,i),function(){this.lineCommentStart=";",this.$id="ace/mode/scheme"}.call(o.prototype),t.Mode=o})