// Flowable Script Format: groovy

def jsonString = execution.getVariable("valueLogicCheck")
def valueFromMap = null
if (jsonString != null) {
    try {
        def jsonObject = new groovy.json.JsonSlurper().parseText(jsonString)
        def keyName = "resource"
        if (jsonObject != null && jsonObject.containsKey(keyName)) {
            valueFromMap = jsonObject[keyName]
        }
    } catch (e) {
        // handle parse error
        valueFromMap = null
    }
}
if (valueFromMap != null) {
    execution.setVariable("resourceNew", valueFromMap)
}

