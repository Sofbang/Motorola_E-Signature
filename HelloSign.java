package com.sfb.HellosignMoto;

import java.io.File;
import java.security.AllPermission;
import java.security.Signature;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;

import com.hellosign.sdk.HelloSignClient;
import com.hellosign.sdk.HelloSignException;
import com.hellosign.sdk.resource.SignatureRequest;
import com.hellosign.sdk.resource.support.Document;
import com.hellosign.sdk.resource.support.FormField;
import com.hellosign.sdk.resource.support.types.FieldType;
import com.hellosign.sdk.resource.support.types.ValidationType;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

//import com.hellosign.sdk.HelloSignClient;
//import com.hellosign.sdk.HelloSignException;
//import com.hellosign.sdk.resource.EmbeddedRequest;
//import com.hellosign.sdk.resource.SignatureRequest;
//import com.hellosign.sdk.resource.support.Document;
//import com.hellosign.sdk.resource.support.FormField;
//import com.hellosign.sdk.resource.support.Signer;
//import com.hellosign.sdk.resource.support.types.FieldType;
//import com.hellosign.sdk.resource.support.types.ValidationType;
//import com.sun.jersey.api.representation.Form;
//import com.hellosign.sdk.HelloSignException;
//import com.hellosign.sdk.resource.Event;

@Path("/motoSign/{signerName}/{signerEmail}")
public class Test {
@GET
@Produces(MediaType.TEXT_PLAIN )
public String getData(@PathParam("signerName") String signerName,@PathParam("signerEmail") String signerEmail) throws HelloSignException, JSONException, UnirestException, IOException  {		
	
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
     
//     String signermail ="sameer.jain@sofbang.com";	
//     String signerFullName ="sameer";	
//     String signermail ="khushboo.dhanani@sofbang.com";	
//     String signerFullName ="Khushboo";
//     String signermail ="parul.gupta@sofbang.com";	
//     String signerFullName ="Parul";
     
     String signermail ="amandeep.singh@sofbang.com";	
     String signerFullName ="Amandeep";
	
	HttpResponse<JsonNode> jsonResponse = Unirest.post("https://498d04708cde34ef641cf933e6ca79bb7c3d4202a647c259a425c0fa69e9d625:@api.hellosign.com/v3/signature_request/send") 
			.header("accept", "application/json") 
			.field("title", "Motorola Solution Document") 
			.field("subject", "The Motorola Solution Document we talked about") 
			.field("message", "Please sign this Motorola Solution Document and then we can discuss more. Let me know if you have any questions.") 
			.field("signers[0][email_address]", signermail) 
			.field("signers[0][name]", signerFullName) 
			//.field("cc_email_addresses", "amansandhotra4@gmail.com")
			.field("file[]", new File("E://SSOW-Example.pdf")) 
			.field("allow_reassign", "1") 
			.field("allow_decline", "1")
			.field("test_mode", "1") 			
			.field("form_fields_per_document", array)			
			.asJson(); 			
		      System.out.println("Query result."+jsonResponse.getBody());    
	          return "Motorola Send the Doccument for sign"; 
     }
}
