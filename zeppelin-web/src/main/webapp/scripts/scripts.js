"use strict";function getPort(){var a=Number(location.port);return("undifined"===a||0===a)&&(a=80,"https:"===location.protocol&&(a=443)),(3333===a||9e3===a)&&(a=8080),a+1}function getWebsocketProtocol(){var a="ws";return"https:"===location.protocol&&(a="wss"),a}angular.module("zeppelinWebApp",["ngAnimate","ngCookies","ngRoute","ngSanitize","angular-websocket","ui.ace","ui.bootstrap","ngTouch","ngDragDrop"]).config(["$routeProvider","WebSocketProvider",function(a,b){b.prefix("").uri(getWebsocketProtocol()+"://"+location.hostname+":"+getPort()),a.when("/",{templateUrl:"views/main.html"}).when("/notebook/:noteId",{templateUrl:"views/notebooks.html",controller:"NotebookCtrl"}).when("/notebook/:noteId/paragraph/:paragraphId?",{templateUrl:"views/notebooks.html",controller:"NotebookCtrl"}).otherwise({redirectTo:"/"})}]),angular.module("zeppelinWebApp").controller("MainCtrl",["$scope","WebSocket","$rootScope","$window",function(a,b,c,d){a.WebSocketWaitingList=[],a.connected=!1,a.looknfeel="default";var e=function(){a.asIframe=d.location.href.indexOf("asIframe")>-1?!0:!1};e(),b.onopen(function(){if(console.log("Websocket created"),a.connected=!0,a.WebSocketWaitingList.length>0)for(var c in a.WebSocketWaitingList)b.send(JSON.stringify(a.WebSocketWaitingList[c]))}),b.onmessage(function(a){var b;a.data&&(b=angular.fromJson(a.data)),console.log("Receive << %o, %o",b.op,b);var d=b.op,e=b.data;"NOTE"===d?c.$emit("setNoteContent",e.note):"NOTES_INFO"===d?c.$emit("setNoteMenu",e.notes):"PARAGRAPH"===d?c.$emit("updateParagraph",e):"PROGRESS"===d?c.$emit("updateProgress",e):"COMPLETION_LIST"===d&&c.$emit("completionList",e)}),b.onerror(function(b){console.log("error message: ",b),a.connected=!1}),b.onclose(function(b){console.log("close message: ",b),a.connected=!1});var f=function(c){"OPEN"!==b.currentState()?a.WebSocketWaitingList.push(c):(console.log("Send >> %o, %o",c.op,c),b.send(JSON.stringify(c)))};c.$on("sendNewEvent",function(a,b){a.defaultPrevented||(f(b),a.preventDefault())}),c.$on("setIframe",function(b,c){b.defaultPrevented||(a.asIframe=c,b.preventDefault())}),c.$on("setLookAndFeel",function(b,c){!b.defaultPrevented&&c&&""!==c&&(a.looknfeel=c,b.preventDefault())})}]),angular.module("zeppelinWebApp").controller("NotebookCtrl",["$scope","$route","$routeParams","$location","$rootScope",function(a,b,c,d,e){a.note=null,a.showEditor=!1,a.editorToggled=!1,a.tableToggled=!1,a.looknfeelOption=["default","simple"],a.cronOption=[{name:"None",value:void 0},{name:"1m",value:"0 0/1 * * * ?"},{name:"5m",value:"0 0/5 * * * ?"},{name:"1h",value:"0 0 0/1 * * ?"},{name:"3h",value:"0 0 0/3 * * ?"},{name:"6h",value:"0 0 0/6 * * ?"},{name:"12h",value:"0 0 0/12 * * ?"},{name:"1d",value:"0 0 0 * * ?"}],a.getCronOptionNameFromValue=function(b){if(!b)return"";for(var c in a.cronOption)if(a.cronOption[c].value===b)return a.cronOption[c].name;return b};var f=function(){e.$emit("sendNewEvent",{op:"GET_NOTE",data:{id:c.noteId}})};f(),a.removeNote=function(a){var b=confirm("Do you want to delete this notebook?");b&&(e.$emit("sendNewEvent",{op:"DEL_NOTE",data:{id:a}}),d.path("/#"))},a.runNote=function(){var a=confirm("Run all paragraphs?");a&&e.$emit("runParagraph")},a.toggleAllEditor=function(){e.$emit(a.editorToggled?"closeEditor":"openEditor"),a.editorToggled=!a.editorToggled},a.showAllEditor=function(){e.$emit("openEditor")},a.hideAllEditor=function(){e.$emit("closeEditor")},a.toggleAllTable=function(){e.$emit(a.tableToggled?"closeTable":"openTable"),a.tableToggled=!a.tableToggled},a.showAllTable=function(){e.$emit("openTable")},a.hideAllTable=function(){e.$emit("closeTable")},a.isNoteRunning=function(){var b=!1;if(!a.note)return!1;for(var c=0;c<a.note.paragraphs.length;c++)if("PENDING"===a.note.paragraphs[c].status||"REFRESH_RESULT"===a.note.paragraphs[c].status||"RUNNING"===a.note.paragraphs[c].status){b=!0;break}return b},a.setLookAndFeel=function(b){a.note.config.looknfeel=b,a.setConfig(),e.$emit("setLookAndFeel",a.note.config.looknfeel)},a.setCronScheduler=function(b){a.note.config.cron=b,a.setConfig()},a.setConfig=function(b){b&&(a.note.config=b),e.$emit("sendNewEvent",{op:"NOTE_UPDATE",data:{id:a.note.id,name:a.note.name,config:a.note.config}})},a.sendNewName=function(){a.showEditor=!1,a.note.name&&e.$emit("sendNewEvent",{op:"NOTE_UPDATE",data:{id:a.note.id,name:a.note.name,config:a.note.config}})},e.$on("setNoteContent",function(b,d){a.paragraphUrl=c.paragraphId,a.asIframe=c.asIframe,a.paragraphUrl&&(d=h(a.paragraphUrl,d),e.$emit("setIframe",a.asIframe)),null===a.note?(a.note=d,g()):i(d),e.$emit("setLookAndFeel",d.config.looknfeel)});var g=function(){a.note.config.looknfeel||(a.note.config.looknfeel="default")},h=function(a,b){var c={};c.id=b.id,c.name=b.name,c.config=b.config,c.info=b.info,c.paragraphs=[];for(var d=0;d<b.paragraphs.length;d++)if(b.paragraphs[d].id===a){c.paragraphs[0]=b.paragraphs[d],c.paragraphs[0].config||(c.paragraphs[0].config={}),c.paragraphs[0].config.editorHide=!0,c.paragraphs[0].config.tableHide=!1;break}return c};e.$on("moveParagraphUp",function(b,c){for(var d=-1,f=0;f<a.note.paragraphs.length;f++)if(a.note.paragraphs[f].id===c){d=f-1;break}0>d||d>=a.note.paragraphs.length||e.$emit("sendNewEvent",{op:"MOVE_PARAGRAPH",data:{id:c,index:d}})}),e.$on("split",function(b,c){for(var d=-1,f=c.id,g=c.paragraph,h=0;h<a.note.paragraphs.length;h++)if(a.note.paragraphs[h].id===f){d=h+1;break}return d===a.note.paragraphs.length?void alert("Cannot insert after the last paragraph."):void(0>d||d>a.note.paragraphs.length||e.$emit("sendNewEvent",{op:"SPLIT_INTO_PARAGRAPHS",data:{index:d,paragraph:g}}))}),e.$on("insertParagraph",function(b,c){for(var d=-1,f=0;f<a.note.paragraphs.length;f++)if(a.note.paragraphs[f].id===c){d=f+1;break}return d===a.note.paragraphs.length?void alert("Cannot insert after the last paragraph."):void(0>d||d>a.note.paragraphs.length||e.$emit("sendNewEvent",{op:"INSERT_PARAGRAPH",data:{index:d}}))}),e.$on("moveParagraphDown",function(b,c){for(var d=-1,f=0;f<a.note.paragraphs.length;f++)if(a.note.paragraphs[f].id===c){d=f+1;break}0>d||d>=a.note.paragraphs.length||e.$emit("sendNewEvent",{op:"MOVE_PARAGRAPH",data:{id:c,index:d}})}),e.$on("moveFocusToPreviousParagraph",function(b,c){for(var d=!1,f=a.note.paragraphs.length-1;f>=0;f--)if(d===!1){if(a.note.paragraphs[f].id===c){d=!0;continue}}else{var g=a.note.paragraphs[f];if(!g.config.hide&&!g.config.editorHide&&!g.config.tableHide){e.$emit("focusParagraph",a.note.paragraphs[f].id);break}}}),e.$on("moveFocusToNextParagraph",function(b,c){for(var d=!1,f=0;f<a.note.paragraphs.length;f++)if(d===!1){if(a.note.paragraphs[f].id===c){d=!0;continue}}else{var g=a.note.paragraphs[f];if(!g.config.hide&&!g.config.editorHide&&!g.config.tableHide){e.$emit("focusParagraph",a.note.paragraphs[f].id);break}}});var i=function(b){b.name!==a.note.name&&(console.log("change note name: %o to %o",a.note.name,b.name),a.note.name=b.name),a.note.config=b.config,a.note.info=b.info;var c=b.paragraphs.map(function(a){return a.id}),d=a.note.paragraphs.map(function(a){return a.id}),f=c.length,g=d.length;if(f>g)for(var h in c)if(d[h]!==c[h]){a.note.paragraphs.splice(h,0,b.paragraphs[h]);break}if(f===g)for(var i in c){var j=b.paragraphs[i];if(d[i]===c[i])e.$emit("updateParagraph",{paragraph:j});else{var k=d.indexOf(c[i]);a.note.paragraphs.splice(k,1),a.note.paragraphs.splice(i,0,j),d=a.note.paragraphs.map(function(a){return a.id})}}if(g>f)for(var l in d)if(d[l]!==c[l]){a.note.paragraphs.splice(l,1);break}}}]),angular.module("zeppelinWebApp").directive("ngEnter",function(){return function(a,b,c){b.bind("keydown keypress",function(b){13===b.which&&(a.$apply(function(){a.$eval(c.ngEnter)}),b.preventDefault())})}}),angular.module("zeppelinWebApp").directive("dropdownInput",function(){return{restrict:"A",link:function(a,b){b.bind("click",function(a){a.stopPropagation()})}}}),angular.module("zeppelinWebApp").directive("resizable",function(){var a={autoHide:!0,handles:"se",minHeight:100,grid:[1e4,10]};return{restrict:"A",scope:{callback:"&onResize"},link:function(b,c){c.resizable(a),c.on("resizestop",function(){b.callback&&b.callback()})}}}),angular.module("zeppelinWebApp").controller("ParagraphCtrl",["$scope","$rootScope","$route","$window","$element","$routeParams","$location","$timeout",function(a,b,c,d,e,f,g,h){a.paragraph=null,a.editor=null;var i={scala:"ace/mode/scala",sql:"ace/mode/sql",markdown:"ace/mode/markdown"};a.forms={},a.init=function(b){a.paragraph=b,a.chart={},a.colWidthOption=[1,2,3,4,5,6,7,8,9,10,11,12],a.showTitleEditor=!1,a.paragraph.config||(a.paragraph.config={}),j(),a.lastData||(a.lastData={}),"TABLE"===a.getResultType()?(a.lastData.settings=jQuery.extend(!0,{},a.paragraph.settings),a.lastData.config=jQuery.extend(!0,{},a.paragraph.config),a.loadTableData(a.paragraph.result),a.setGraphMode(a.getGraphMode(),!1,!1)):"HTML"===a.getResultType()&&a.renderHtml()},a.renderHtml=function(){var b=function(){if($("#p"+a.paragraph.id+"_html").length)try{$("#p"+a.paragraph.id+"_html").html(a.paragraph.result.msg)}catch(c){console.log("HTML rendering error %o",c)}else h(b,10)};h(b)};var j=function(){a.paragraph.config.looknfeel||(a.paragraph.config.looknfeel="default"),a.paragraph.config.colWidth||(a.paragraph.config.colWidth=12),a.paragraph.config.graph||(a.paragraph.config.graph={}),a.paragraph.config.graph.mode||(a.paragraph.config.graph.mode="table"),a.paragraph.config.graph.height||(a.paragraph.config.graph.height=300),a.paragraph.config.graph.optionOpen||(a.paragraph.config.graph.optionOpen=!1),a.paragraph.config.graph.keys||(a.paragraph.config.graph.keys=[]),a.paragraph.config.graph.values||(a.paragraph.config.graph.values=[]),a.paragraph.config.graph.groups||(a.paragraph.config.graph.groups=[])};a.getIframeDimensions=function(){if(a.asIframe){var b="#"+f.paragraphId+"_container",c=$(b).height();return c}return 0},a.$watch(a.getIframeDimensions,function(b){if(a.asIframe&&b){var c={};c.height=b,c.url=g.$$absUrl,d.parent.postMessage(angular.toJson(c),"*")}}),b.$on("updateParagraph",function(b,c){if(!(c.paragraph.id!==a.paragraph.id||c.paragraph.dateCreated===a.paragraph.dateCreated&&c.paragraph.dateFinished===a.paragraph.dateFinished&&c.paragraph.dateStarted===a.paragraph.dateStarted&&c.paragraph.status===a.paragraph.status&&c.paragraph.jobName===a.paragraph.jobName&&c.paragraph.title===a.paragraph.title&&c.paragraph.errorMessage===a.paragraph.errorMessage&&angular.equals(c.paragraph.settings,a.lastData.settings)&&angular.equals(c.paragraph.config,a.lastData.config))){a.lastData.settings=jQuery.extend(!0,{},c.paragraph.settings),a.lastData.config=jQuery.extend(!0,{},c.paragraph.config);var d=a.getResultType(),e=a.getResultType(c.paragraph),f=a.getGraphMode(),g=a.getGraphMode(c.paragraph),h=c.paragraph.dateFinished!==a.paragraph.dateFinished;if(console.log("updateParagraph oldData %o, newData %o. type %o -> %o, mode %o -> %o",a.paragraph,c,d,e,f,g),a.paragraph.text!==c.paragraph.text&&(a.dirtyText?a.dirtyText===c.paragraph.text?(a.paragraph.text=c.paragraph.text,a.dirtyText=void 0):a.paragraph.text=a.dirtyText:a.paragraph.text=c.paragraph.text),a.paragraph.aborted=c.paragraph.aborted,a.paragraph.dateCreated=c.paragraph.dateCreated,a.paragraph.dateFinished=c.paragraph.dateFinished,a.paragraph.dateStarted=c.paragraph.dateStarted,a.paragraph.errorMessage=c.paragraph.errorMessage,a.paragraph.jobName=c.paragraph.jobName,a.paragraph.title=c.paragraph.title,a.paragraph.status="REFRESH_RESULT"===c.paragraph.status?"RUNNING":c.paragraph.status,a.paragraph.result=c.paragraph.result,a.paragraph.settings=c.paragraph.settings,a.asIframe)c.paragraph.config.editorHide=!0,c.paragraph.config.tableHide=!1,a.paragraph.config=c.paragraph.config;else{a.paragraph.config=c.paragraph.config,j();var i=$("#"+a.paragraph.id+"_paragraphColumn"),k=$("#"+a.paragraph.id+"_paragraphColumn_main");k.removeClass(k.attr("class")),k.addClass("paragraph-col col-md-"+a.paragraph.config.colWidth),i.removeClass(i.attr("class")),i.addClass("paragraph-space box paragraph-margin")}"TABLE"===e?(a.loadTableData(a.paragraph.result),("TABLE"!==d||h)&&(p(),q()),f!==g?a.setGraphMode(g,!1,!1):a.setGraphMode(g,!1,!0)):"HTML"===e&&a.renderHtml()}}),a.isRunning=function(){return"RUNNING"===a.paragraph.status||"REFRESH_RESULT"===a.paragraph.status||"PENDING"===a.paragraph.status?!0:!1},a.cancelParagraph=function(){console.log("Cancel %o",a.paragraph.id);var c={op:"CANCEL_PARAGRAPH",data:{id:a.paragraph.id}};b.$emit("sendNewEvent",c)},a.runParagraph=function(c){var d={op:"RUN_PARAGRAPH",data:{id:a.paragraph.id,title:a.paragraph.title,paragraph:c,config:a.paragraph.config,params:a.paragraph.settings.params}};b.$emit("sendNewEvent",d)},a.moveUp=function(){b.$emit("moveParagraphUp",a.paragraph.id)},a.moveDown=function(){b.$emit("moveParagraphDown",a.paragraph.id)},a.insertNew=function(){b.$emit("insertParagraph",a.paragraph.id)},a.split=function(c){var d={id:a.paragraph.id,paragraph:c};b.$emit("split",d)},a.removeParagraph=function(){var c=confirm("Do you want to delete this paragraph?");if(c){console.log("Remove paragraph");var d={op:"PARAGRAPH_REMOVE",data:{id:a.paragraph.id}};b.$emit("sendNewEvent",d)}},a.toggleEditor=function(){a.paragraph.config.editorHide?a.openEditor():a.closeEditor()},a.closeEditor=function(){console.log("close the note");var b=jQuery.extend(!0,{},a.paragraph.settings.params),c=jQuery.extend(!0,{},a.paragraph.config);c.editorHide=!0,m(a.paragraph.title,a.paragraph.text,c,b)},a.openEditor=function(){console.log("open the note");var b=jQuery.extend(!0,{},a.paragraph.settings.params),c=jQuery.extend(!0,{},a.paragraph.config);c.editorHide=!1,m(a.paragraph.title,a.paragraph.text,c,b)},a.closeTable=function(){console.log("close the output");var b=jQuery.extend(!0,{},a.paragraph.settings.params),c=jQuery.extend(!0,{},a.paragraph.config);c.tableHide=!0,m(a.paragraph.title,a.paragraph.text,c,b)},a.openTable=function(){console.log("open the output");var b=jQuery.extend(!0,{},a.paragraph.settings.params),c=jQuery.extend(!0,{},a.paragraph.config);c.tableHide=!1,m(a.paragraph.title,a.paragraph.text,c,b)},a.showTitle=function(){var b=jQuery.extend(!0,{},a.paragraph.settings.params),c=jQuery.extend(!0,{},a.paragraph.config);c.title=!0,m(a.paragraph.title,a.paragraph.text,c,b)},a.hideTitle=function(){var b=jQuery.extend(!0,{},a.paragraph.settings.params),c=jQuery.extend(!0,{},a.paragraph.config);c.title=!1,m(a.paragraph.title,a.paragraph.text,c,b)},a.setTitle=function(){var b=jQuery.extend(!0,{},a.paragraph.settings.params),c=jQuery.extend(!0,{},a.paragraph.config);m(a.paragraph.title,a.paragraph.text,c,b)},a.changeColWidth=function(){var b=jQuery.extend(!0,{},a.paragraph.settings.params),c=jQuery.extend(!0,{},a.paragraph.config);m(a.paragraph.title,a.paragraph.text,c,b)},a.toggleGraphOption=function(){var b=jQuery.extend(!0,{},a.paragraph.config);b.graph.optionOpen=b.graph.optionOpen?!1:!0;var c=jQuery.extend(!0,{},a.paragraph.settings.params);m(a.paragraph.title,a.paragraph.text,b,c)},a.toggleOutput=function(){var b=jQuery.extend(!0,{},a.paragraph.config);b.tableHide=!b.tableHide;var c=jQuery.extend(!0,{},a.paragraph.settings.params);m(a.paragraph.title,a.paragraph.text,b,c)},a.loadForm=function(b,c){var d=b.defaultValue;c[b.name]&&(d=c[b.name]),a.forms[b.name]=d},a.aceChanged=function(){a.dirtyText=a.editor.getSession().getValue()},a.aceLoaded=function(c){var d=ace.require("ace/ext/language_tools"),e=ace.require("ace/range").Range;if(a.editor=c,"{{paragraph.id}}_editor"!==c.container.id){a.editor.renderer.setShowGutter(!1),a.editor.setHighlightActiveLine(!1),a.editor.focus();var f=a.editor.getSession().getScreenLength()*a.editor.renderer.lineHeight+a.editor.renderer.scrollBar.getWidth();k(c.container.id,f),a.editor.getSession().setUseWrapMode(!0),-1!==navigator.appVersion.indexOf("Mac")?a.editor.setKeyboardHandler("ace/keyboard/emacs"):-1!==navigator.appVersion.indexOf("Win")||-1!==navigator.appVersion.indexOf("X11")||-1!==navigator.appVersion.indexOf("Linux"),a.editor.setOptions({enableBasicAutocompletion:!0,enableSnippets:!1,enableLiveAutocompletion:!1});var g={getCompletions:function(c,d,f,g,h){if(a.editor.isFocused()){var i=d.getTextRange(new e(0,0,f.row,f.column));b.$emit("sendNewEvent",{op:"COMPLETION",data:{id:a.paragraph.id,buf:i,cursor:i.length}}),b.$on("completionList",function(a,b){if(b.completions){var c=[];for(var d in b.completions){var e=b.completions[d];c.push({name:e,value:e,score:300})}h(null,c)}})}}};d.addCompleter(g),a.editor.on("focus",function(){var b=$("#"+a.paragraph.id+"_paragraphColumn");b.addClass("focused")}),a.editor.on("blur",function(){var b=$("#"+a.paragraph.id+"_paragraphColumn");b.removeClass("focused")}),a.editor.getSession().on("change",function(b,d){f=d.getScreenLength()*a.editor.renderer.lineHeight+a.editor.renderer.scrollBar.getWidth(),k(c.container.id,f),a.editor.resize()});var h=a.editor.getSession().getValue();a.editor.getSession().setMode(String(h).startsWith("%sql")?i.sql:String(h).startsWith("%md")?i.markdown:i.scala),a.editor.commands.addCommand({name:"run",bindKey:{win:"Shift-Enter",mac:"Shift-Enter"},exec:function(b){var c=b.getValue();c&&a.runParagraph(c)},readOnly:!1}),a.editor.commands.bindKey("ctrl-.","startAutocomplete"),a.editor.commands.bindKey("ctrl-space",null),a.editor.keyBinding.origOnCommandKey=a.editor.keyBinding.onCommandKey,a.editor.keyBinding.onCommandKey=function(c,d,e){if(a.editor.completer&&a.editor.completer.activated);else{var f,g;38===e||80===e&&c.ctrlKey?(f=a.editor.getSession().getLength(),g=a.editor.getCursorPosition().row,0===g&&b.$emit("moveFocusToPreviousParagraph",a.paragraph.id)):(40===e||78===e&&c.ctrlKey)&&(f=a.editor.getSession().getLength(),g=a.editor.getCursorPosition().row,g===f-1&&b.$emit("moveFocusToNextParagraph",a.paragraph.id))}this.origOnCommandKey(c,d,e)}}};var k=function(a,b){$("#"+a).height(b.toString()+"px")};a.getEditorValue=function(){return a.editor.getValue()},a.getProgress=function(){return a.currentProgress?a.currentProgress:0},a.getExecutionTime=function(){var b=a.paragraph,c=Date.parse(b.dateFinished)-Date.parse(b.dateStarted);return"Took "+c/1e3+" seconds"},b.$on("updateProgress",function(b,c){c.id===a.paragraph.id&&(a.currentProgress=c.progress)}),b.$on("focusParagraph",function(b,c){a.paragraph.id===c&&(a.editor.focus(),$("body").scrollTo("#"+c+"_editor",300,{offset:-60}))}),b.$on("runParagraph",function(){a.runParagraph(a.editor.getValue())}),b.$on("openEditor",function(){a.openEditor()}),b.$on("closeEditor",function(){a.closeEditor()}),b.$on("openTable",function(){a.openTable()}),b.$on("closeTable",function(){a.closeTable()}),a.getResultType=function(b){var c=b?b:a.paragraph;return c.result&&c.result.type?c.result.type:"TEXT"},a.getBase64ImageSrc=function(a){return"data:image/png;base64,"+a},a.getGraphMode=function(b){var c=b?b:a.paragraph;return c.config.graph&&c.config.graph.mode?c.config.graph.mode:"table"},a.loadTableData=function(a){if(a&&"TABLE"===a.type){var b=[],c=[],d=[],e=a.msg.split("\n");a.comment="";for(var f=!1,g=0;g<e.length;g++){var h=e[g];if(f)a.comment+=h;else if(""!==h){for(var i=h.split("	"),j=[],k=[],l=0;l<i.length;l++){var m=i[l];0===g?b.push({name:m,index:l,aggr:"sum"}):(j.push(m),k.push({key:b[g]?b[g].name:void 0,value:m}))}0!==g&&(c.push(j),d.push(k))}else c.length>0&&(f=!0)}a.msgTable=d,a.columnNames=b,a.rows=c}},a.setGraphMode=function(b,c,d){if(c)l(b);else{p();var e=a.paragraph.config.graph.height;$("#p"+a.paragraph.id+"_graph").height(e),b&&"table"!==b?"multiBarChart"===b?o(b,a.paragraph.result,d):"pieChart"===b?o(b,a.paragraph.result,d):"stackedAreaChart"===b?o(b,a.paragraph.result,d):"lineChart"===b&&o(b,a.paragraph.result,d):n(a.paragraph.result,d)}};var l=function(b){var c=jQuery.extend(!0,{},a.paragraph.config),d=jQuery.extend(!0,{},a.paragraph.settings.params);c.graph.mode=b,m(a.paragraph.title,a.paragraph.text,c,d)},m=function(c,d,e,f){var g={op:"COMMIT_PARAGRAPH",data:{id:a.paragraph.id,title:c,paragraph:d,params:f,config:e}};b.$emit("sendNewEvent",g)},n=function(){var b=function(a){return isNaN(a)&&a.length>"%html".length&&"%html "===a.substring(0,"%html ".length)?"html":""},c=function(a){if(isNaN(a)){var c=b(a);return""!==c?a.substring(c.length+2):a}var d=a.toString(),e=d.split("."),f=e[0].replace(/(\d)(?=(\d{3})+(?!\d))/g,"$1,");return e.length>1&&(f+="."+e[1]),f},d=function(){var d="";d+='<table class="table table-hover table-condensed">',d+="  <thead>",d+='    <tr style="background-color: #F6F6F6; font-weight: bold;">';for(var e in a.paragraph.result.columnNames)d+="<th>"+a.paragraph.result.columnNames[e].name+"</th>";d+="    </tr>",d+="  </thead>";for(var f in a.paragraph.result.msgTable){var g=a.paragraph.result.msgTable[f];d+="    <tr>";for(var h in g){var i=g[h].value;"html"!==b(i)&&(i=i.replace(/[\u00A0-\u9999<>\&]/gim,function(a){return"&#"+a.charCodeAt(0)+";"})),d+="      <td>"+c(i)+"</td>"}d+="    </tr>"}d+="</table>",$("#p"+a.paragraph.id+"_table").html(d),$("#p"+a.paragraph.id+"_table").perfectScrollbar();var j=a.paragraph.config.graph.height;$("#p"+a.paragraph.id+"_table").height(j)},e=function(){if($("#p"+a.paragraph.id+"_table").length)try{d()}catch(b){console.log("Chart drawing error %o",b)}else h(e,10)};h(e)},o=function(b,c,d){if(!a.chart[b]){var e=nv.models[b]();a.chart[b]=e}var f=r(c),g=(a.paragraph.config.graph.keys,a.paragraph.config.graph.values,[]);if("pieChart"===b){var i=s(f,!0).d3g;if(a.chart[b].x(function(a){return a.label}).y(function(a){return a.value}),i.length>0)for(var j=0;j<i[0].values.length;j++){var k=i[0].values[j];g.push({label:k.x,value:k.y})}}else if("multiBarChart"===b)g=s(f,!0).d3g,a.chart[b].yAxis.axisLabelDistance(50);else{var l=s(f),m=l.xLabels;g=l.d3g,a.chart[b].xAxis.tickFormat(function(a){return m[a]?m[a]:a}),a.chart[b].yAxis.axisLabelDistance(50),a.chart[b].useInteractiveGuideline(!0),a.chart[b].forceY([0])}var n=function(){var c=a.paragraph.config.graph.height,d=300,e=150;try{g[0].values.length>e&&(d=0)}catch(f){}d3.select("#p"+a.paragraph.id+"_"+b+" svg").attr("height",a.paragraph.config.graph.height).datum(g).transition().duration(d).call(a.chart[b]);d3.select("#p"+a.paragraph.id+"_"+b+" svg").style.height=c+"px",nv.utils.windowResize(a.chart[b].update)},o=function(){if(0!==$("#p"+a.paragraph.id+"_"+b+" svg").length)try{n()}catch(c){console.log("Chart drawing error %o",c)}else h(o,10)};h(o)};a.isGraphMode=function(b){return"TABLE"===a.getResultType()&&a.getGraphMode()===b?!0:!1},a.onGraphOptionChange=function(){p(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeGraphOptionKeys=function(b){a.paragraph.config.graph.keys.splice(b,1),p(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeGraphOptionValues=function(b){a.paragraph.config.graph.values.splice(b,1),p(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeGraphOptionGroups=function(b){a.paragraph.config.graph.groups.splice(b,1),p(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.setGraphOptionValueAggr=function(b,c){a.paragraph.config.graph.values[b].aggr=c,p(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)};var p=function(){var b=function(a){for(var b=0;b<a.length;b++)for(var c=b+1;c<a.length;c++)angular.equals(a[b],a[c])&&a.splice(c,1)},c=function(b){for(var c=0;c<b.length;c++){for(var d=!1,e=0;e<a.paragraph.result.columnNames.length;e++){var f=b[c],g=a.paragraph.result.columnNames[e];if(f.index===g.index&&f.name===g.name){d=!0;break}}d||b.splice(c,1)}};b(a.paragraph.config.graph.keys),c(a.paragraph.config.graph.keys),c(a.paragraph.config.graph.values),b(a.paragraph.config.graph.groups),c(a.paragraph.config.graph.groups)},q=function(){0===a.paragraph.config.graph.keys.length&&a.paragraph.result.columnNames.length>0&&a.paragraph.config.graph.keys.push(a.paragraph.result.columnNames[0]),0===a.paragraph.config.graph.values.length&&a.paragraph.result.columnNames.length>1&&a.paragraph.config.graph.values.push(a.paragraph.result.columnNames[1])},r=function(b){for(var c=a.paragraph.config.graph.keys,d=a.paragraph.config.graph.groups,e=a.paragraph.config.graph.values,f={sum:function(a,b){var c=void 0!==a?isNaN(a)?1:parseFloat(a):0,d=void 0!==b?isNaN(b)?1:parseFloat(b):0;return c+d},count:function(a,b){var c=void 0!==a?a:0,d=void 0!==b?1:0;return c+d},min:function(a,b){var c=void 0!==a?isNaN(a)?1:parseFloat(a):0,d=void 0!==b?isNaN(b)?1:parseFloat(b):0;return Math.min(c,d)},max:function(a,b){var c=void 0!==a?isNaN(a)?1:parseFloat(a):0,d=void 0!==b?isNaN(b)?1:parseFloat(b):0;return Math.max(c,d)},avg:function(a,b){var c=void 0!==a?isNaN(a)?1:parseFloat(a):0,d=void 0!==b?isNaN(b)?1:parseFloat(b):0;return c+d}},g={sum:!1,count:!1,min:!1,max:!1,avg:!0},h={},i={},j=0;j<b.rows.length;j++){for(var k=b.rows[j],l=h,m=i,n=0;n<c.length;n++){var o=c[n];l[o.name]||(l[o.name]={order:n,index:o.index,type:"key",children:{}}),l=l[o.name].children;var p=k[o.index];m[p]||(m[p]={}),m=m[p]}for(var q=0;q<d.length;q++){var r=d[q],s=k[r.index];l[s]||(l[s]={order:q,index:r.index,type:"group",children:{}}),l=l[s].children,m[s]||(m[s]={}),m=m[s]}for(var t=0;t<e.length;t++){var u=e[t],v=u.name+"("+u.aggr+")";l[v]||(l[v]={type:"value",order:t,index:u.index}),m[v]=m[v]?{value:f[u.aggr](m[v].value,k[u.index],m[v].count+1),count:g[u.aggr]?m[v].count+1:m[v].count}:{value:k[u.index],count:1}}}return{schema:h,rows:i}},s=function(b,c){var d=[],e=b.schema,f=b.rows,g=a.paragraph.config.graph.values,h=function(a,b){return a?a+"."+b:b},i=function(a,b,c,d,e,f,g,j){"key"===b.type?(f=h(f,a),g=h(g,c)):"group"===b.type?j=h(j,a):"value"===b.type&&(j=h(j,c),e(f,g,j,d));for(var k in b.children)if("group"!==b.type||a===c)for(var l in d)i(k,b.children[k],l,d[l],e,f,g,j);else i(k,b.children[k],k,void 0,e,f,g,j)},j=a.paragraph.config.graph.keys,k=a.paragraph.config.graph.groups,g=a.paragraph.config.graph.values,l=0===j.length&&0===k.length&&g.length>0,m=Object.keys(e)[0],n={},o=0,p={},q=0,r={};for(var s in f)i(m,e[m],s,f[s],function(a,b,e,f){void 0===n[b]&&(r[o]=b,n[b]=o++),void 0===p[e]&&(p[e]=q++);var g=p[e];l&&(g=0),d[g]||(d[g]={values:[],key:l?"values":e});var h=isNaN(b)?c?b:n[b]:parseFloat(b),i=0;void 0===h&&(h=e),void 0!==f&&(i=isNaN(f.value)?0:parseFloat(f.value)/parseFloat(f.count)),d[g].values.push({x:h,y:i})});var t={};for(var u in p){var v=u.substring(0,u.lastIndexOf("("));t[v]?t[v]++:t[v]=1}if(l)for(var w=0;w<d[0].values.length;w++){var u=d[0].values[w].x;if(u){var v=u.substring(0,u.lastIndexOf("("));t[v]<=1&&(d[0].values[w].x=v)}}else{for(var x=0;x<d.length;x++){var u=d[x].key,v=u.substring(0,u.lastIndexOf("("));t[v]<=1&&(d[x].key=v)}if(1===k.length&&1===g.length)for(x=0;x<d.length;x++){var u=d[x].key;u=u.split(".")[0],d[x].key=u}}return{xLabels:r,d3g:d}};a.setGraphHeight=function(){var b=$("#p"+a.paragraph.id+"_graph").height(),c=jQuery.extend(!0,{},a.paragraph.settings.params),d=jQuery.extend(!0,{},a.paragraph.config);d.graph.height=b,m(a.paragraph.title,a.paragraph.text,d,c)},"function"!=typeof String.prototype.startsWith&&(String.prototype.startsWith=function(a){return this.slice(0,a.length)===a}),a.goToSingleParagraph=function(){var b=c.current.pathParams.noteId,e=location.protocol+"//"+location.host+"/#/notebook/"+b+"/paragraph/"+a.paragraph.id+"?asIframe";d.open(e)}}]),angular.module("zeppelinWebApp").controller("NavCtrl",["$scope","$rootScope","$routeParams",function(a,b,c){a.notes=[],b.$on("setNoteMenu",function(b,c){a.notes=c});var d=function(){b.$emit("sendNewEvent",{op:"LIST_NOTES"})};d(),a.createNewNote=function(){b.$emit("sendNewEvent",{op:"NEW_NOTE"})},a.isActive=function(a){return c.noteId===a?!0:!1}}]),angular.module("zeppelinWebApp").directive("ngDelete",function(){return function(a,b,c){b.bind("keydown keypress",function(b){(27===b.which||46===b.which)&&(a.$apply(function(){a.$eval(c.ngEnter)}),b.preventDefault())})}});