///
/// Licensed to STRATIO (C) under one or more contributor license agreements.
/// See the NOTICE file distributed with this work for additional information
/// regarding copyright ownership.  The STRATIO (C) licenses this file
/// to you under the Apache License, Version 2.0 (the
/// "License"); you may not use this file except in compliance
/// with the License.  You may obtain a copy of the License at
///
///   http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing,
/// software distributed under the License is distributed on an
/// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
/// KIND, either express or implied.  See the License for the
/// specific language governing permissions and limitations
/// under the License.
///

class Greeter {
	greeting: string;
	constructor (message: string) {
		this.greeting = message;
	}
	greet() {
		return "Hello, " + this.greeting;
	}
}   

var greeter = new Greeter("world");

var button = document.createElement('button')
button.innerText = "Say Hello"
button.onclick = function() {
	alert(greeter.greet())
}

document.body.appendChild(button)

class Snake extends Animal {
   move() {
       alert("Slithering...");
       super(5);
   }
}

class Horse extends Animal {
   move() {
       alert("Galloping...");
       super.move(45);
   }
}

module Sayings {
    export class Greeter {
        greeting: string;
        constructor (message: string) {
            this.greeting = message;
        }
        greet() {
            return "Hello, " + this.greeting;
        }
    }
}
module Mankala {
   export class Features {
       public turnContinues = false;
       public seedStoredCount = 0;
       public capturedCount = 0;
       public spaceCaptured = NoSpace;

       public clear() {
           this.turnContinues = false;
           this.seedStoredCount = 0;
           this.capturedCount = 0;
           this.spaceCaptured = NoSpace;
       }

       public toString() {
           var stringBuilder = "";
           if (this.turnContinues) {
               stringBuilder += " turn continues,";
           }
           stringBuilder += " stores " + this.seedStoredCount;
           if (this.capturedCount > 0) {
               stringBuilder += " captures " + this.capturedCount + " from space " + this.spaceCaptured;
           }
           return stringBuilder;
       }
   }
}