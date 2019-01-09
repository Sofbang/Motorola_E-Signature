package oracle.apps.fnd.cp.request;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;


public class Hello implements JavaConcurrentProgram {

public static final String RCS_ID = "$Header$";
String json = "\r\n" + 
"[\r\n" + 
"    [\r\n" + 
"        {\r\n" + 
"            \"api_id\": \"498d04708cde34ef641cf933e6ca79bb7c1\",\r\n" + 
"            \"name\": \"Yes or No\",\r\n" + 
"            \"type\": \"text\",\r\n" + 
"            \"x\": 497,\r\n" + 
"            \"y\": 381,\r\n" + 
"            \"width\": 70,\r\n" + 
"            \"height\": 15,\r\n" + 
"            \"required\": true,\r\n" + 
"            \"signer\": 0,\r\n" + 
"            \"page\": 1,\r\n" + 
"            \"validation_type\": \"letters_only\"\r\n" + 
"        },"
+ "         \r\n" + 
                "{\r\n" + 
"            \"api_id\": \"498d04708cde34ef641cf933e6ca79bb7c47c2\",\r\n" + 
"            \"name\": \"\",\r\n" + 
"            \"type\": \"date_signed\",\r\n" + 
"            \"x\": 360,\r\n" + 
"            \"y\": 520,\r\n" + 
"            \"width\": 150,\r\n" + 
"            \"height\": 25,\r\n" + 
"            \"required\": true,\r\n" + 
"            \"signer\": 0,\r\n" + 
"            \"page\": 1,\r\n" +  
"        },\r\n" +
                "{\r\n" + 
"            \"api_id\": \"498d04708cde34ef641cf933e6ca79bb7c3\",\r\n" + 
"            \"name\": \"Yes or No\",\r\n" + 
"            \"type\": \"text\",\r\n" + 
"            \"x\": 382,\r\n" + 
"            \"y\": 314,\r\n" + 
"            \"width\": 70,\r\n" + 
"            \"height\": 15,\r\n" + 
"            \"required\": true,\r\n" + 
"            \"signer\": 0,\r\n" + 
"            \"page\": 1,\r\n" + 
"            \"validation_type\": \"letters_only\"\r\n" + 
"        },\r\n" +
         "{\r\n" + 
"            \"api_id\": \"498d04708cde34ef641cf933e6ca79bb7c4\",\r\n" + 
"            \"name\": \"\",\r\n" + 
"            \"type\": \"text\",\r\n" + 
"            \"x\": 73,\r\n" + 
"            \"y\": 462,\r\n" + 
"            \"width\": 40,\r\n" + 
"            \"height\": 15,\r\n" + 
"            \"required\": true,\r\n" + 
"            \"signer\": 0,\r\n" + 
"            \"page\": 1,\r\n" + 
"            \"validation_type\": \"letters_only\"\r\n" + 
"        },\r\n" +
"        {\r\n" + 
"            \"api_id\": \"498d04708cde34ef641cf933e6ca79bb7c5\",\r\n" + 
"            \"name\": \"\",\r\n" + 
"            \"type\": \"signature\",\r\n" + 
"            \"x\": 75,\r\n" + 
"            \"y\": 518,\r\n" + 
"            \"width\": 120,\r\n" + 
"            \"height\": 30,\r\n" + 
"            \"required\": true,\r\n" + 
"            \"signer\": 0,\r\n" + 
"            \"page\": 1\r\n" + 
"        }\r\n" + 
"    ],\r\n" + 
"]";
     JSONArray array = new JSONArray(json);

       String signermail ="khushboo.dhanani@sofbang.com";
 
        String signerFullName ="Khushboo";



public void runProgram(CpContext ctx) {


ctx.getLogFile().writeln("-- Hello World! --", 0);
ctx.getOutFile().writeln("-- Hello World! --");


ctx.getReqCompletion().setCompletion(ReqCompletion.NORMAL, "");

}



public static void main(String args[]){
	HttpResponse<JsonNode> jsonResponse = Unirest.post("https://498d04708cde34ef641cf933e6ca79bb7c3d4202a647c259a425c0fa69e9d625:@api.hellosign.com/v3/signature_request/send") 
.header("accept", "application/json") 
.field("title", "Motorola Solution Document") 
.field("subject", "The Motorola Solution Document we talked about") 
.field("message", "Please sign this Motorola Solution Document and then we can discuss more. Let me know if you have any questions.") 
.field("signers[0][email_address]", signermail) 
.field("signers[0][name]", signerFullName) 
.field("file[]", new File("E://SSOW-Example.pdf")) 
.field("allow_reassign", "1") 
.field("allow_decline", "1")
.field("test_mode", "1") 
.field("form_fields_per_document", array)
 
.asJson(); 
      System.out.println("Query result."+jsonResponse.getBody());  

}

}























   
    

