package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.io.Source._


object Postmark {
  
// start of jar
  case class full(email: String, name: String, mailboxHash: String)
  
  case class header(name: String, value: String)
  
  case class attachment(name: String, content: String, contentType: String, contentID: String, contentLength: Long)
  
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
      tag: String,
      headers: Seq[header],
      attachments: Seq[attachment]
  ) 
   
  implicit val fullReads: Reads[full] = (
     (JsPath \ "Email").read[String] and
     (JsPath \ "Name").read[String] and
     (JsPath \ "MailboxHash").read[String]  
  )(full.apply _)
  
  
  implicit val headerReads: Reads[header] = (
   (JsPath \ "Name").read[String] and
   (JsPath \ "Value").read[String]  
  )(header.apply _)
  
  implicit val attachmentReads: Reads[attachment] = (
   (JsPath \ "Name").read[String] and
   (JsPath \ "Content").read[String] and
   (JsPath \ "ContentType").read[String] and
   (JsPath \ "ContentID").read[String] and
   (JsPath \ "ContentLength").read[Long]
  )(attachment.apply _)
  
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
      (JsPath \ "Tag").read[String] and
      (JsPath \ "Headers").read[Seq[header]] and
      (JsPath \ "Attachments").read[Seq[attachment]]
    )(postMarkMitt.apply _)
    
    
    def parseJson(webHook: String):JsResult[postMarkMitt] =  {
         val json: JsValue = Json.parse(webHook.mkString)
         json.validate[postMarkMitt]
    }
  
}