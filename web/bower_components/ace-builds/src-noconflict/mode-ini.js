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
ace.define("ace/mode/ini_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var escapeRe = "\\\\(?:[\\\\0abtrn;#=:]|x[a-fA-F\\d]{4})";

var IniHighlightRules = function() {
    this.$rules = {
        start: [{
            token: 'punctuation.definition.comment.ini',
            regex: '#.*',
            push_: [{
                token: 'comment.line.number-sign.ini',
                regex: '$|^',
                next: 'pop'
            }, {
                defaultToken: 'comment.line.number-sign.ini'
            }]
        }, {
            token: 'punctuation.definition.comment.ini',
            regex: ';.*',
            push_: [{
                token: 'comment.line.semicolon.ini',
                regex: '$|^',
                next: 'pop'
            }, {
                defaultToken: 'comment.line.semicolon.ini'
            }]
        }, {
            token: ['keyword.other.definition.ini', 'text', 'punctuation.separator.key-value.ini'],
            regex: '\\b([a-zA-Z0-9_.-]+)\\b(\\s*)(=)'
        }, {
            token: ['punctuation.definition.entity.ini', 'constant.section.group-title.ini', 'punctuation.definition.entity.ini'],
            regex: '^(\\[)(.*?)(\\])'
        }, {
            token: 'punctuation.definition.string.begin.ini',
            regex: "'",
            push: [{
                token: 'punctuation.definition.string.end.ini',
                regex: "'",
                next: 'pop'
            }, {
                token: "constant.language.escape",
                regex: escapeRe
            }, {
                defaultToken: 'string.quoted.single.ini'
            }]
        }, {
            token: 'punctuation.definition.string.begin.ini',
            regex: '"',
            push: [{
                token: "constant.language.escape",
                regex: escapeRe
            }, {
                token: 'punctuation.definition.string.end.ini',
                regex: '"',
                next: 'pop'
            }, {
                defaultToken: 'string.quoted.double.ini'
            }]
        }]
    };

    this.normalizeRules();
};

IniHighlightRules.metaData = {
    fileTypes: ['ini', 'conf'],
    keyEquivalent: '^~I',
    name: 'Ini',
    scopeName: 'source.ini'
};


oop.inherits(IniHighlightRules, TextHighlightRules);

exports.IniHighlightRules = IniHighlightRules;
});

ace.define("ace/mode/folding/ini",["require","exports","module","ace/lib/oop","ace/range","ace/mode/folding/fold_mode"], function(require, exports, module) {
"use strict";

var oop = require("../../lib/oop");
var Range = require("../../range").Range;
var BaseFoldMode = require("./fold_mode").FoldMode;

var FoldMode = exports.FoldMode = function() {
};
oop.inherits(FoldMode, BaseFoldMode);

(function() {

    this.foldingStartMarker = /^\s*\[([^\])]*)]\s*(?:$|[;#])/;

    this.getFoldWidgetRange = function(session, foldStyle, row) {
        var re = this.foldingStartMarker;
        var line = session.getLine(row);
        
        var m = line.match(re);
        
        if (!m) return;
        
        var startName = m[1] + ".";
        
        var startColumn = line.length;
        var maxRow = session.getLength();
        var startRow = row;
        var endRow = row;

        while (++row < maxRow) {
            line = session.getLine(row);
            if (/^\s*$/.test(line))
                continue;
            m = line.match(re);
            if (m && m[1].lastIndexOf(startName, 0) !== 0)
                break;

            endRow = row;
        }

        if (endRow > startRow) {
            var endColumn = session.getLine(endRow).length;
            return new Range(startRow, startColumn, endRow, endColumn);
        }
    };

}).call(FoldMode.prototype);

});

ace.define("ace/mode/ini",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/ini_highlight_rules","ace/mode/folding/ini"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextMode = require("./text").Mode;
var IniHighlightRules = require("./ini_highlight_rules").IniHighlightRules;
var FoldMode = require("./folding/ini").FoldMode;

var Mode = function() {
    this.HighlightRules = IniHighlightRules;
    this.foldingRules = new FoldMode();
};
oop.inherits(Mode, TextMode);

(function() {
    this.lineCommentStart = ";";
    this.blockComment = {start: "/*", end: "*/"};
    this.$id = "ace/mode/ini";
}).call(Mode.prototype);

exports.Mode = Mode;
});
