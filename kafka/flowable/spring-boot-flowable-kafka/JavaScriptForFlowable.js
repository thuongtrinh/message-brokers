// Flowable Script Format: JavaScript

// Get the JSON string variable from execution
var jsonString = execution.getVariable("valueLogicCheck");

// Initialize variable to hold the value you want to fetch
var valueFromMap = null;

// Check if jsonString exists and is not null
if (jsonString !== null && jsonString !== undefined) {
    try {
        // Parse JSON string into object (map)
        var jsonObject = JSON.parse(jsonString);
        
        // Check if the object is valid and the key exists
        var keyName = "resource"; // Name of the key to fetch from the map
        if (jsonObject !== null && jsonObject !== undefined && jsonObject.hasOwnProperty(keyName)) {
            valueFromMap = jsonObject[keyName];
        }
    } catch (e) {
        // Handle JSON parse error (invalid JSON string)
        // Optional: log or handle errors if needed
        valueFromMap = null;
    }
}

// Finally, set the fetched value into execution if it's not null
if (valueFromMap !== null && valueFromMap !== undefined) {
    execution.setVariable("resourceNew", valueFromMap);
}

//-------------------------------------Change value for "Script"-------------------------------------------------

var jsonString = execution.getVariable("valueLogicCheck");
var valueFromMap = null;
if (jsonString !== null && jsonString !== undefined) {
    try {
        var jsonObject = JSON.parse(jsonString);
        var keyName = "resource";
        if (jsonObject !== null && jsonObject !== undefined && jsonObject.hasOwnProperty(keyName)) {
            valueFromMap = jsonObject[keyName];
        }
    } catch (e) {
        valueFromMap = null;
    }
}

if (valueFromMap !== null && valueFromMap !== undefined) {
    execution.setVariable("resourceNew", valueFromMap);
}

//--------------------------------------------------------------------------------------

// Solution: Add in order for script task 
// For Java 8-14: Ensure Nashorn is available. It's usually included, but if you're using a custom JRE, verify its presence.
<dependency>
    <groupId>org.openjdk.nashorn</groupId>
    <artifactId>nashorn-core</artifactId>
    <version>15.6</version>
</dependency>

// For Java 15+: Consider using GraalVM as your JVM, or explicitly add the GraalVM JavaScript engine dependency to your project if using a standard OpenJDK distribution.
<dependency>
    <groupId>org.graalvm.sdk</groupId>
    <artifactId>graal-sdk</artifactId>
    <version>22.3.0</version>
</dependency>
<dependency>
    <groupId>org.graalvm.js</groupId>
    <artifactId>js</artifactId>
    <version>22.3.0</version>
</dependency>

//--------------------------------------------------------------------------------------
