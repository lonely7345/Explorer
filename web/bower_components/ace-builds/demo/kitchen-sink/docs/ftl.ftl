<#--

    Licensed to STRATIO (C) under one or more contributor license agreements.
    See the NOTICE file distributed with this work for additional information
    regarding copyright ownership.  The STRATIO (C) licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

-->
<#ftl encoding="utf-8" />
<#setting locale="en_US" />
<#import "library" as lib />
<#--
    FreeMarker comment
    ${abc} <#assign a=12 />
-->

<!DOCTYPE html>
<html lang="en-us">
    <head>
        <meta charset="utf-8" />
        
        <title>${title!"FreeMarker"}<title>
    </head>
    
    <body>
    
        <h1>Hello ${name!""}</h1>
        
        <p>Today is: ${.now?date}</p>
        
        <#assign x = 13>
        <#if x &gt; 12 && x lt 14>x equals 13: ${x}</#if>
        
        <ul>
            <#list items as item>
                <li>${item_index}: ${item.name!?split("\n")[0]}</li>
            </#list>
        </ul>
        
        User directive: <@lib.function attr1=true attr2='value' attr3=-42.12>Test</@lib.function>
        <@anotherOne />
        
        <#if variable?exists>
            Deprecated
        <#elseif variable??>
            Better
        <#else>
            Default
        </#if>
        
        <img src="images/${user.id}.png" />
        
    </body>
</html>
