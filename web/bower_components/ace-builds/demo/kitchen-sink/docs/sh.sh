#!/bin/sh
#
# Licensed to STRATIO (C) under one or more contributor license agreements.
# See the NOTICE file distributed with this work for additional information
# regarding copyright ownership.  The STRATIO (C) licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#


# Script to open a browser to current branch
# Repo formats:
# ssh   git@github.com:richo/gh_pr.git
# http  https://richoH@github.com/richo/gh_pr.git
# git   git://github.com/richo/gh_pr.git

username=`git config --get github.user`

get_repo() {
    git remote -v | grep ${@:-$username} | while read remote; do
      if repo=`echo $remote | grep -E -o "git@github.com:[^ ]*"`; then
          echo $repo | sed -e "s/^git@github\.com://" -e "s/\.git$//"
          exit 1
      fi
      if repo=`echo $remote | grep -E -o "https?://([^@]*@)?github.com/[^ ]*\.git"`; then
          echo $repo | sed -e "s|^https?://||" -e "s/^.*github\.com\///" -e "s/\.git$//"
          exit 1
      fi
      if repo=`echo $remote | grep -E -o "git://github.com/[^ ]*\.git"`; then
          echo $repo | sed -e "s|^git://github.com/||" -e "s/\.git$//"
          exit 1
      fi
    done

    if [ $? -eq 0 ]; then
        echo "Couldn't find a valid remote" >&2
        exit 1
    fi
}

echo ${#x[@]}

if repo=`get_repo $@`; then
    branch=`git symbolic-ref HEAD 2>/dev/null`
    echo "http://github.com/$repo/pull/new/${branch##refs/heads/}"
else
    exit 1
fi
