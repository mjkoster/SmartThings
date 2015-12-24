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

	state.hsColorMap = [
	"off": [level:1, hue:0, saturation:0],
    "white": [level:50, hue:100, saturation:20],
    "red": [level:100, hue:100, saturation:100],
    "green": [level:100, hue:33, saturation:100],
    "blue": [level:100, hue:67, saturation:100],
    "yellow": [level:100, hue:12, saturation:100],
    "orange": [level:100, hue:3, saturation:100],
    "cyan": [level:100, hue:50, saturation:100],
    "pink": [level:100, hue:100, saturation:90],
    "purple": [level:100, hue:75, saturation:100],
    "magenta": [level:100, hue:95, saturation:100],
    "warmwhite": [level:50, hue:100, saturation:50],
    "oldlace": [level:50, hue:100, saturation:50]
    ]

	state.lastColor = "off"
    lights.setColor(state.hsColorMap[state.lastColor])
    log.debug "Initializing... $state.lastColor"
	getColor()
}

def getColor() {
	log.debug "Color: $state.lastColor"
	try {
		httpGet("http://api.thingspeak.com/channels/1417/field/1/last.json") { resp ->
    		state.newColor = resp.data["field1"]
			if (state.lastColor != state.newColor) {
            	log.debug "setting new color"
				lights.setColor(state.hsColorMap[state.newColor])
        		state.lastColor = state.newColor
    		}
			runIn(5, getColor)
    	}
	} catch (e) {
    	log.error "oops: $e"
	}
}
