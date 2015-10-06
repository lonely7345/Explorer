;
;  Licensed to STRATIO (C) under one or more contributor license agreements.
;  See the NOTICE file distributed with this work for additional information
;  regarding copyright ownership.  The STRATIO (C) licenses this file
;  to you under the Apache License, Version 2.0 (the
;  "License"); you may not use this file except in compliance
;  with the License.  You may obtain a copy of the License at
;
;    http://www.apache.org/licenses/LICENSE-2.0
;
;  Unless required by applicable law or agreed to in writing,
;  software distributed under the License is distributed on an
;  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
;  KIND, either express or implied.  See the License for the
;  specific language governing permissions and limitations
;  under the License.
;

section	.text
    global main         ;must be declared for using gcc

main:	                ;tell linker entry point

	mov	edx, len	    ;message length
	mov	ecx, msg	    ;message to write
	mov	ebx, 1	        ;file descriptor (stdout)
	mov	eax, 4	        ;system call number (sys_write)
	int	0x80	        ;call kernel

	mov	eax, 1	        ;system call number (sys_exit)
	int	0x80	        ;call kernel

section	.data

msg	db	'Hello, world!',0xa	;our dear string
len	equ	$ - msg			;length of our dear string
