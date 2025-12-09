/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;
import groovy.json.JsonBuilder;

def Message processData(Message message) {
def input = message.getBody(java.lang.String) as String;

def json = new JsonSlurper().parseText(input)
def properties = message.getProperties();
nextLinkvalue = properties.get("odata.nextLink");
def builder = new groovy.json.JsonBuilder(json.root)

if(nextLinkvalue != null){
def request = new JsonSlurper().parseText(builder.toString())
request.putAt("@odata.nextLink", nextLinkvalue)
def jsonStr = new JsonBuilder(request).toPrettyString()
//def jsonSlr = new JsonSlurper().parseText(jsonStr)

message.setBody(jsonStr.toString())
}else{
message.setBody(builder.toString())
}

return message
}
