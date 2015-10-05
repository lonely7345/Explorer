/*
*Licensed to STRATIO (C) under one or more contributor license agreements.
*See the NOTICE file distributed with this work for additional information
*regarding copyright ownership.  The STRATIO (C) licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
//http://groovy.codehaus.org/Martin+Fowler%27s+closure+examples+in+Groovy

class Employee {
    def name, salary
    boolean manager
    String toString() { return name }
}

def emps = [new Employee(name:'Guillaume', manager:true, salary:200),
    new Employee(name:'Graeme', manager:true, salary:200),
    new Employee(name:'Dierk', manager:false, salary:151),
    new Employee(name:'Bernd', manager:false, salary:50)]

def managers(emps) {
    emps.findAll { e -> e.isManager() }
}

assert emps[0..1] == managers(emps) // [Guillaume, Graeme]

def highPaid(emps) {
    threshold = 150
    emps.findAll { e -> e.salary > threshold }
}

assert emps[0..2] == highPaid(emps) // [Guillaume, Graeme, Dierk]

def paidMore(amount) {
    { e -> e.salary > amount}
}
def highPaid = paidMore(150)

assert highPaid(emps[0]) // true
assert emps[0..2] == emps.findAll(highPaid)

def filename = 'test.txt'
new File(filename).withReader{ reader -> doSomethingWith(reader) }

def readersText
def doSomethingWith(reader) { readersText = reader.text }

assert new File(filename).text == readersText