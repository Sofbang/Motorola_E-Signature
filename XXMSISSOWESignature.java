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
import oracle.apps.fnd.cp.request.CpContext;
import oracle.apps.fnd.cp.request.JavaConcurrentProgram;
import oracle.apps.fnd.cp.request.LogFile;
import oracle.apps.fnd.cp.request.OutFile;
import oracle.apps.fnd.cp.request.ReqCompletion;
import oracle.apps.fnd.util.*;

import oracle.apps.xdo.XDOException;
import oracle.apps.xdo.common.pdf.util.PDFDocMerger;



import oracle.jdbc.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;


public class XXMSISSOWESignature implements JavaConcurrentProgram {

 public static final String RCS_ID = "$Header$";
 OutFile outFile;
 LogFile logFile;
 Connection mConn = null;
 ReqCompletion lRC;

 //File Separator 
 private String mFileSeparator;
 // globals for template
 String XDOAppShortName = "XXSVC";
 String XDOtemplateCode = "XXSVCMSISERVCERSOW";
 // hard-wired constants for template addition
 final String XDOLanguage = "en";
 final String XDOTerritory = "US";
 final String XDOFinal_format = "PDF";
 final String XDOtemplateType = "TEMPLATE_SOURCE";
 String PDFFile = "";

 String outFilePath = "";
 String progShortName = "XXSVCMSISERVCERSOW";
 String progDesc = "MSI Servicer SOW Report";
 Integer iRequestID = 0;
 public static final String M_SUCCESS = "SUCCESS";
 public static final String M_ERROR = "ERROR";
 public static final String M_WARNING = "WARNING";
 String mGetCompleteStatus =
  "DECLARE R_VAL BOOLEAN; " + "b_phase VARCHAR2 (80) := NULL; " +
  "b_status VARCHAR2 (80) := NULL; " +
  "b_dev_phase VARCHAR2 (80) := NULL; " +
  "b_dev_status VARCHAR2 (80) := NULL; " +
  "b_message VARCHAR2 (240) := NULL; " + "BEGIN " +
  "r_val := fnd_concurrent.wait_for_request (:1,5,1000," +
  "b_phase,b_status,b_dev_phase,b_dev_status,b_message); " +
  ":2 := b_phase; " + ":3 := b_status; " + ":4 := b_message; " + "end;";

 public void runProgram(CpContext ctx) {


 
  logFile = ctx.getLogFile();
  boolean hasError = false;
  OracleCallableStatement lStmt = null;
  boolean bCompletion = true;
  String sPhase = "";
  String sStatus = "";
  String sMessage = "";

  // assign outfile
  outFile = ctx.getOutFile();



  outFilePath =
   ((new File(outFile.getFileName())).getParent() == null ? "" :
    (new File(outFile.getFileName())).getParent() +
    mFileSeparator);

  logFile.writeln("OutFile File Path: -> ",0);

  // assign fileseparator
  mFileSeparator = getFileSeparator();

  // get the JDBC connection object 
  mConn = ctx.getJDBCConnection();

  //get the parameters from concurrent
  ParameterList params = ctx.getParameterList();
  //create a temporary array and retrieve the parameters created by
  // the program. Currently limiting the number of parameters to 10 for now
  String pvals[] = new String[10];
  int pcount = 0;
  while (params.hasMoreElements()) {
  NameValueType aNVT = params.nextParameter();
  pvals[pcount] = aNVT.getValue();
  pcount++;
  }
  // send parameter values to the log file
  logFile.writeln("Arg 1: P_CONTRACT_NUMBER -> " + pvals[0], 0);
  logFile.writeln("Arg 2: P_CONTACT_NUM_MODIFIER -> " + pvals[1], 0);
  logFile.writeln("Arg 3: P_SERVICE_PROVIDER -> " + pvals[2], 0);
  logFile.writeln("Arg 4: p_eSig_Flag -> " + pvals[3], 0);
  logFile.writeln("Arg 5: p_csm_email -> " + pvals[4], 0);
  logFile.writeln("Arg 5: p_csm_name -> " + pvals[5], 0);
  
  while (params.hasMoreElements()) {
   NameValueType ntv = params.nextParameter();
    outFile.writeln(ntv.getName() + ":" + ntv.getValue());
    logFile.writeln("Unexpected Parameter!", LogFile.ERROR);
    hasError = true;
    break;
   }


 try{
  // create a concurrent request object
ConcurrentRequest cr = new ConcurrentRequest(mConn);
// use the parameters to call fnd_request.submit_request
cr.addLayout(XDOAppShortName, XDOtemplateCode, XDOLanguage, 
XDOTerritory, XDOFinal_format);
Vector param = new Vector();
param.add(pvals[0]); // 
param.add(pvals[1]); // 
param.add(pvals[2]); // 
iRequestID = 
cr.submitRequest(XDOAppShortName, progShortName, progDesc, 
null, false, param);
mConn.commit();

// send the request ID to the log file
logFile.writeln("-- Request ID: ->" + Integer.toString(iRequestID), 
0);

    // call fnd_concurrent.wait_for_request to wait until the request
    // has ended - use this to check the request status before proceeding
    lStmt =
     (OracleCallableStatement) mConn.prepareCall(mGetCompleteStatus);
    lStmt.setInt(1, iRequestID);
    lStmt.registerOutParameter(2, java.sql.Types.VARCHAR, 0, 255);
    lStmt.registerOutParameter(3, java.sql.Types.VARCHAR, 0, 255);
    lStmt.registerOutParameter(4, java.sql.Types.VARCHAR, 0, 255);
    lStmt.execute();

    // get the results of the completion
    sPhase = lStmt.getString(2);
    sStatus = lStmt.getString(3);
    sMessage = lStmt.getString(4);
    lStmt.close();

    // send the results of the request processing to the log file
    logFile.writeln("-- Phase: -> " + sPhase, 0);
    logFile.writeln("-- Status: -> " + sStatus, 0);
    logFile.writeln("-- Message: -> " + sMessage, 0);

    // test here to make sure it completed correctly
   if (sPhase.equals("Completed")) { //&& sStatus.equals("Normal")) {
  PDFFile = progShortName + "_" + iRequestID + "_1.PDF";
  
  outFile.setOutFile(outFilePath + PDFFile);
 
  ctx.getReqCompletion().setCompletion(ReqCompletion.NORMAL, 
  "");
  if ("y".equalsIgnoreCase(pvals[3])){

    callHelloSign(pvals[4],pvals[5],outFilePath + PDFFile);
  }
   else {
    lRC.setCompletion(ReqCompletion.WARNING, M_WARNING);
   }

   ctx.releaseJDBCConnection();

   }} catch (SQLException s) {
    logFile.writeln("SQL Error: Exception thrown w/ error message: " +
     s.getMessage(), 0);
    lRC.setCompletion(ReqCompletion.WARNING, M_WARNING);
   ctx.releaseJDBCConnection();
   } catch (IOException ioe) {
    logFile.writeln("IO Error: Exception thrown w/ error message: " +
     ioe.getMessage(), 0);
    lRC.setCompletion(ReqCompletion.WARNING, M_WARNING);
   ctx.releaseJDBCConnection();
   } catch (Exception e) {
    logFile.writeln("General Exception: " + e.getMessage(), 0);
    lRC.setCompletion(ReqCompletion.WARNING, M_WARNING);
   ctx.releaseJDBCConnection();
   } finally {
    try {
     if (lStmt != null)
      lStmt.close();
    ctx.releaseJDBCConnection();
    } catch (SQLException e) {
     logFile.writeln(e.getMessage(), 0);
     lRC.setCompletion(ReqCompletion.WARNING, M_WARNING);
    }
   }

  }


 private String getFileSeparator() {
  return (System.getProperty("file.separator"));
 }



 

 public void callHelloSign(String csmEmail, String csmName, String filePath) {
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
   "        }," +
   "         \r\n" +
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
   try {
  JSONArray array = new JSONArray(json);


   HttpResponse < JsonNode > jsonResponse = Unirest.post("https://498d04708cde34ef641cf933e6ca79bb7c3d4202a647c259a425c0fa69e9d625:@api.hellosign.com/v3/signature_request/send")
    .header("accept", "application/json")
    .field("title", "MSI SSOW Document")
    .field("subject", "Your MSI SSOW is ready for Sign")
    .field("message", "We are pleased to notify you that your document is ready and available to sign.")
    .field("signers[0][email_address]", csmEmail)
    .field("signers[0][name]", csmName)
    .field("file[]", filePath)
    .field("allow_reassign", "1")
    .field("allow_decline", "1")
    .field("form_fields_per_document", array)

    .asJson();
   outFile.writeln("Query result." + jsonResponse.getBody());
  } catch (Exception e) {
   throw new RuntimeException(e);
  }
 }

}