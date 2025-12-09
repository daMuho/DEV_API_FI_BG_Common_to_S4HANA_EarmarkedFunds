import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;
def Message processData(Message message) {
  def slurper = new JsonSlurper();
  //get body
  def body = message.getBody(String.class);
  //parse json
  def payload = slurper.parseText(body)

  switch(payload.root.value.getClass()){
    //if empty salesOrder skip this script
    case java.lang.String:
    break;
    //if multiple salesOrder
    case java.util.ArrayList:
    payload.root.value.each{ salesOrder ->
    //check if _Item is array
    if(salesOrder._Item.getClass() != java.util.ArrayList){
      //if not convert to array
      salesOrder._Item = [salesOrder._Item]
      body = JsonOutput.toJson(payload);
    }
  }
  //if single salesOrder
  case groovy.json.internal.LazyMap:
  //check if _Item is array
  if(payload.root.value._Item.getClass() != java.util.ArrayList){
    //if not convert to array
    payload.root.value._Item = [payload.root.value._Item]
    body = JsonOutput.toJson(payload);
  }
  default: break;
}

message.setBody(body);

return message;
}