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
/**
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package code
{
    /*****************************************
	 * based on textmate actionscript bundle
	 ****************************************/
	 
	import fl.events.SliderEvent;
	
	public class Foo extends MovieClip
	{
		//*************************
		// Properties:
		
		public var activeSwatch:MovieClip;
		
		// Color offsets
		public var c1:Number = 0;	// R
		
		//*************************
		// Constructor:
		
		public function Foo()
		{
			// Respond to mouse events
			swatch1_btn.addEventListener(MouseEvent.CLICK,swatchHandler,false,0,false);
			previewBox_btn.addEventListener(MouseEvent.MOUSE_DOWN,dragPressHandler);
			
			// Respond to drag events
			red_slider.addEventListener(SliderEvent.THUMB_DRAG,sliderHandler);
			
			// Draw a frame later
			addEventListener(Event.ENTER_FRAME,draw);
		}
        
		protected function clickHandler(event:MouseEvent):void
		{
			car.transform.colorTransform = new ColorTransform(0,0,0,1,c1,c2,c3);
		}
		
		protected function changeRGBHandler(event:Event):void
		{
			c1 = Number(c1_txt.text);
            
			if(!(c1>=0)){
				c1 = 0;
			}			
			
			updateSliders();
		}
	}
}