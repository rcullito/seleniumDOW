package controllers

import play.api._
import play.api.mvc._
import com.datastax.driver.core._

import play.api.data._
import play.api.data.Forms._

import models.User
import models.Charges

import play.api.mvc.Request

import play.api.libs.json._
import play.api.libs.functional.syntax._

object Postmark extends Controller {
    
  val json: JsValue = Json.parse("""
    {
      "FromName": "Robert Culliton",
      "From": "rob.culliton@gmail.com",
      "FromFull": {
        "Email": "rob.culliton@gmail.com",
        "Name": "Robert Culliton",
        "MailboxHash": "#47584229"
      },
     "To": "mail@wampum.io",
     "ToFull": [
        {
          "Email": "mail@wampum.io",
          "Name": "",
          "MailboxHash": ""
        }
      ],
      "Cc": "",
      "CcFull": [],
        "Bcc": "58db53f3a8351b93ce07ff0343e70946@inbound.postmarkapp.com",
        "BccFull": [
          {
            "Email": "58db53f3a8351b93ce07ff0343e70946@inbound.postmarkapp.com",
            "Name": "",
            "MailboxHash": ""
          }
        ],
        "OriginalRecipient": "58db53f3a8351b93ce07ff0343e70946@inbound.postmarkapp.com",
        "Subject": "test2",
        "MessageID": "996c1fa8-3232-4beb-86f5-2396677b1d2a",
        "ReplyTo": "",
        "MailboxHash": "",
        "Date": "Sat, 21 Feb 2015 09:06:57 -0800 (PST)",
        "TextBody": "test2\r\n\r\n\u2014\r\nSent from Mailbox",
        "HtmlBody": "<!DOCTYPE html PUBLIC \"-\/\/W3C\/\/DTD HTML 4.0 Transitional\/\/EN\" \"http:\/\/www.w3.org\/TR\/REC-html40\/loose.dtd\">\r\n<html><body>\r\n<span id=\"mailbox-conversation\"><div>test2<\/div><\/span><div class=\"mailbox_signature\">\r\n<br>&mdash;<br>Sent from <a href=\"https:\/\/www.dropbox.com\/mailbox\">Mailbox<\/a>\r\n<\/div>\r\n<\/body><\/html>\r\n",
        "StrippedTextReply": "",
        "Tag": ""   
    }
   """)
   
  // when nesting case classes have the argument name start with a lower case whereas the case class begins with an upper case
  case class full(email: String, name: String, mailboxHash: String)
  case class postMarkMitt(
      fromName: String, 
      from: String, 
      fromFull: full,
      to: String,
      toFull: Seq[full],
      cc: String,
      // TODO need to figure out this type, maybe ask Leah or compare with other languages?
      ccFull: Seq[String],
      bcc: String,
      bccFull: Seq[full],
      originalRecipient: String,
      subject: String,
      messageID: String,
      replyTo: String,
      mailboxHash: String,
      date: String,
      textBody: String,
      htmlBody: String,
      strippedTextReply: String,
      tag: String
      // TODO headers array and attachments
  ) 
   
  implicit val fullReads: Reads[full] = (
     (JsPath \ "Email").read[String] and
     (JsPath \ "Name").read[String] and
     (JsPath \ "MailboxHash").read[String]  
  )(full.apply _)

  
  implicit val postMarkReads: Reads[postMarkMitt] = (
      (JsPath \ "FromName").read[String] and
      (JsPath \ "From").read[String] and
      (JsPath \ "FromFull").read[full] and
      (JsPath \ "To").read[String] and
      (JsPath \ "ToFull").read[Seq[full]] and
      (JsPath \ "Cc").read[String] and
      (JsPath \ "CcFull").read[Seq[String]] and
      (JsPath \ "Bcc").read[String] and 
      (JsPath \ "BccFull").read[Seq[full]] and
      (JsPath \ "OriginalRecipient").read[String] and
      (JsPath \ "Subject").read[String] and
      (JsPath \ "MessageID").read[String] and
      (JsPath \ "ReplyTo").read[String] and
      (JsPath \ "MailboxHash").read[String] and 
      (JsPath \ "Date").read[String] and
      (JsPath \ "TextBody").read[String] and 
      (JsPath \ "HtmlBody").read[String] and
      (JsPath \ "StrippedTextReply").read[String] and
      (JsPath \ "Tag").read[String]       
    )(postMarkMitt.apply _)
    
  val postMarkResult: JsResult[postMarkMitt] = json.validate[postMarkMitt]

  def sample() = Action {
     request =>

     postMarkResult match {
        case s: JsSuccess[postMarkMitt] => val result = s.get
          Ok(result.originalRecipient)
        //case e: JsError => println("Errors: " + JsError.toFlatJson(e).toString()) 
      }
    
     

  }
  

}