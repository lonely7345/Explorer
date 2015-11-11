/**
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
package com.stratio.explorer.cassandra.models;

/**
 * bean with values of describe operation
 */
public class SHCQLOperation {

    private String [] describe;

    /**
     * Constructor of shCQL command .
     * @param shCQLcommand
     */
    public SHCQLOperation(String shCQLcommand){
        describe = shCQLcommand.split(" +");;
    }

    /**
     * name of shCql operation .
     * @return name of shCql operation
     */
    public String nameOperation(){
        return describe[0];
    }

    /**
     * identifier of Describe (table,Tables,KeySpace,KeySpaces) .
     * @return identifier of Describe
     */
    public String identifier(){
        return describe[1].replaceAll(";","").trim();
    }

    /**
     * Recovery optional value of Describe
     * @return optional Value
     */
    public String optionalValue(){
        String result ="";
        if (describe.length>2)
            result = describe[2];
        return result;
    }
}
