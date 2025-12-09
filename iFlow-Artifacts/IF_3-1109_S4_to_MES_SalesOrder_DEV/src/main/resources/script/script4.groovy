/* Developed with https://ide.contiva.com */
/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.XmlUtil;

def Message processData(Message message) {
    
    def xmlparser = new XmlParser();
    // 1. Parse the main XML body and the property
    def body = message.getBody(java.lang.String) as String;
    def xmlBody = xmlparser.parseText(body);
    
    def POItemPayload = message.getProperty("POItemPayload")
    def xmlPOItem = xmlparser.parseText(POItemPayload)
    
    // 2. Extract the required values
    def POItem = xmlPOItem.entry.content['m:properties']['d:UnderlyingPurchaseOrderItem'].text()
    def correspRef = xmlBody.root.value._Item[0].CorrespncExternalReference.text()
    
    // 3. Conditionally create the concatenated string
    def concatenatedValue
    // This logic now adds spaces around the slash
    if (POItem?.trim() && correspRef?.trim()) { 
        concatenatedValue = POItem + " / " + correspRef
    } else if (POItem?.trim()) {
        concatenatedValue = POItem
    } else {
        concatenatedValue = correspRef
    }

    
    // 4. Append the new nodes to the _Item section
    xmlBody.root.value._Item[0].appendNode( "POItem",[:], POItem)
    xmlBody.root.value._Item[0].appendNode( "POZCON",[:], concatenatedValue)
    
   message.setBody(XmlUtil.serialize(xmlBody))
    return message;
}
