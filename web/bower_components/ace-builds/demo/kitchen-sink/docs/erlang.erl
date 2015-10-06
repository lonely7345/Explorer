%%%
%%%  Licensed to STRATIO (C) under one or more contributor license agreements.
%%%  See the NOTICE file distributed with this work for additional information
%%%  regarding copyright ownership.  The STRATIO (C) licenses this file
%%%  to you under the Apache License, Version 2.0 (the
%%%  "License"); you may not use this file except in compliance
%%%  with the License.  You may obtain a copy of the License at
%%%
%%%    http://www.apache.org/licenses/LICENSE-2.0
%%%
%%%  Unless required by applicable law or agreed to in writing,
%%%  software distributed under the License is distributed on an
%%%  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
%%%  KIND, either express or implied.  See the License for the
%%%  specific language governing permissions and limitations
%%%  under the License.
%%%

  %% A process whose only job is to keep a counter.
  %% First version
  -module(counter).
  -export([start/0, codeswitch/1]).
 
  start() -> loop(0).
 
  loop(Sum) ->
    receive
       {increment, Count} ->
          loop(Sum+Count);
       {counter, Pid} ->
          Pid ! {counter, Sum},
          loop(Sum);
       code_switch ->
          ?MODULE:codeswitch(Sum)
          % Force the use of 'codeswitch/1' from the latest MODULE version
    end.
 
  codeswitch(Sum) -> loop(Sum).