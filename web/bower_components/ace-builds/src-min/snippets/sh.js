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
define("ace/snippets/sh",["require","exports","module"],function(e,t,n){"use strict";t.snippetText='# Shebang. Executing bash via /usr/bin/env makes scripts more portable.\nsnippet #!\n	#!/usr/bin/env bash\n	\nsnippet if\n	if [[ ${1:condition} ]]; then\n		${2:#statements}\n	fi\nsnippet elif\n	elif [[ ${1:condition} ]]; then\n		${2:#statements}\nsnippet for\n	for (( ${2:i} = 0; $2 < ${1:count}; $2++ )); do\n		${3:#statements}\n	done\nsnippet fori\n	for ${1:needle} in ${2:haystack} ; do\n		${3:#statements}\n	done\nsnippet wh\n	while [[ ${1:condition} ]]; do\n		${2:#statements}\n	done\nsnippet until\n	until [[ ${1:condition} ]]; do\n		${2:#statements}\n	done\nsnippet case\n	case ${1:word} in\n		${2:pattern})\n			${3};;\n	esac\nsnippet go \n	while getopts \'${1:o}\' ${2:opts} \n	do \n		case $$2 in\n		${3:o0})\n			${4:#staments};;\n		esac\n	done\n# Set SCRIPT_DIR variable to directory script is located.\nsnippet sdir\n	SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"\n# getopt\nsnippet getopt\n	__ScriptVersion="${1:version}"\n\n	#===  FUNCTION  ================================================================\n	#         NAME:  usage\n	#  DESCRIPTION:  Display usage information.\n	#===============================================================================\n	function usage ()\n	{\n			cat <<- EOT\n\n	  Usage :  $${0:0} [options] [--] \n\n	  Options: \n	  -h|help       Display this message\n	  -v|version    Display script version\n\n			EOT\n	}    # ----------  end of function usage  ----------\n\n	#-----------------------------------------------------------------------\n	#  Handle command line arguments\n	#-----------------------------------------------------------------------\n\n	while getopts ":hv" opt\n	do\n	  case $opt in\n\n		h|help     )  usage; exit 0   ;;\n\n		v|version  )  echo "$${0:0} -- Version $__ScriptVersion"; exit 0   ;;\n\n		\\? )  echo -e "\\n  Option does not exist : $OPTARG\\n"\n			  usage; exit 1   ;;\n\n	  esac    # --- end of case ---\n	done\n	shift $(($OPTIND-1))\n\n',t.scope="sh"})