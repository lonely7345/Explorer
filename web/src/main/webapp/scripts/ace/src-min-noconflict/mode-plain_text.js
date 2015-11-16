/*
 * Copyright (C) 2013 Stratio (http://stratio.com)
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
ace.define("ace/mode/plain_text",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/text_highlight_rules","ace/mode/behaviour"],function(e,t,n){"use strict";var r=e("../lib/oop"),i=e("./text").Mode,s=e("./text_highlight_rules").TextHighlightRules,o=e("./behaviour").Behaviour,u=function(){this.HighlightRules=s,this.$behaviour=new o};r.inherits(u,i),function(){this.type="text",this.getNextLineIndent=function(e,t,n){return""},this.$id="ace/mode/plain_text"}.call(u.prototype),t.Mode=u})