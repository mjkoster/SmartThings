/**
 *  Cheerlights
 *
 *  Copyright 2015 Michael Koster
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Cheerlights",
    namespace: "mjkoster",
    author: "Michael Koster",
    description: "Control lights from twitter",
    category: "SmartThings Labs",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Lights to control") {
        input "lights", "capability.colorControl",
            title: "Lights", multiple: true
 	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	initialize()
}

def initialize() {
	state.lastColor = "#000000"
	getColor()
}

def getColor() {
	htmlGet("http://api.thingspeak.com/channels/1417/field/2/last.txt") {
    response ->
    color = response.body
	if (state.lastColor != color) {
		lights.setColor(color)
        state.lastColor = newColor
    	}
	runIn(5, getColor)
    }
}
