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
'use strict';

module.exports = function (grunt) {
  require('load-grunt-tasks')(grunt);

  // Project configuration.
  grunt.initConfig({
    // Metadata.
    pkg: grunt.file.readJSON('perfect-scrollbar.jquery.json'),
    banner: '/*! <%= pkg.title || pkg.name %> - v<%= pkg.version %>\n' +
      '<%= pkg.homepage ? "* " + pkg.homepage + "\\n" : "" %>' +
      '* Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author.name %>;' +
      ' Licensed <%= _.pluck(pkg.licenses, "type").join(", ") %> */\n',
    clean: {
      files: ['min']
    },
    // Task configuration.
    uglify: {
      options: {
        banner: '<%= banner %>'
      },
      min: {
        files: {
          'min/perfect-scrollbar.min.js': ['src/perfect-scrollbar.js']
        }
      }
    },
    jshint: {
      gruntfile: {
        options: {
          jshintrc: '.jshintrc'
        },
        src: 'Gruntfile.js'
      },
      src: {
        options: {
          jshintrc: '.jshintrc'
        },
        src: 'src/perfect-scrollbar.js'
      }
    },
    cssmin: {
      options: {
        banner: '<%= banner %>'
      },
      minify: {
        expand: true,
        cwd: 'src/',
        src: ['perfect-scrollbar.css'],
        dest: 'min/',
        ext: '.min.css'
      }
    },
    bump: {
      options: {
        files: ['package.json', 'bower.json', 'perfect-scrollbar.jquery.json'],
        updateConfigs: ['pkg'],
        commit: false,
        createTag: false,
        push: false
      }
    },
    sass: {
      dist: {
        files: {
          'src/perfect-scrollbar.css': 'src/perfect-scrollbar.scss'
        }
      }
    }
  });

  grunt.registerTask('default', 'List commands', function () {
    grunt.log.writeln("");

    grunt.log.writeln("Run 'grunt lint' to lint the source files");
    grunt.log.writeln("Run 'grunt build' to minify the source files");
  });

  grunt.registerTask('lint', ['jshint']);
  grunt.registerTask('build', ['clean', 'uglify', 'sass', 'cssmin']);
  grunt.registerTask('travis', ['lint']);
  grunt.registerTask('release', 'Release a new version', function (arg) {
    var bumpType = arg ? ':' + arg : '';
    grunt.task.run(['lint', 'bump' + bumpType, 'build']);
  });
};
