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
        "Tag": "",
        "Headers": [
          {
            "Name": "X-Spam-Checker-Version",
            "Value": "SpamAssassin 3.3.1 (2010-03-16) on sc-ord-inbound2"
          },
          {
            "Name": "X-Spam-Status",
            "Value": "No"
          },
          {
            "Name": "X-Spam-Score",
            "Value": "-0.1"
          },
          {
            "Name": "X-Spam-Tests",
            "Value": "DKIM_SIGNED,DKIM_VALID,DKIM_VALID_AU,FREEMAIL_FROM,HTML_MESSAGE,URIBL_BLOCKED"
          },
          {
            "Name": "Received-SPF",
            "Value": "None (no SPF record) identity=mailfrom; client-ip=209.85.216.176; helo=mail-qc0-f176.google.com; envelope-from=mail+caf_=58db53f3a8351b93ce07ff0343e70946=inbound.postmarkapp.com@wampum.io; receiver=58db53f3a8351b93ce07ff0343e70946@inbound.postmarkapp.com"
          },
          {
            "Name": "X-Google-DKIM-Signature",
            "Value": "v=1; a=rsa-sha256; c=relaxed\/relaxed;        d=1e100.net; s=20130820;        h=x-gm-message-state:delivered-to:dkim-signature:date:mime-version         :message-id:from:to:subject:content-type;        bh=pYrGrIZYHTqZRex2yYkN0eKgLm7XvrVaEla0Ufg2QCI=;        b=Pt9ZJmryAOXB5k8E6eu23LZ7vTWolrRqtXqbJ9e+1rLu3n\/FNDcs8n+TJ7ryv01m4a         n0uJXPqVYUSRbT7ylSJ8U7tmnijAyvsKoK4hTlX96+uOF1mdQWII0xaDMwEcljDf18Xo         YqPx4vXqYu\/6mKrFvWuEHhm+RovHnlYBIR+vmUzP5TP\/2q5uMPa9wJ+nAacekOfOzfMU         +uW+Pjei5SKT49TcmLZkDaI3+aQ4hBgAqe6D8gDMmGkl8NpnQfVyqAl5DnFD7Ta7n0JQ         uvi52\/yIOLGbFm64FWtSbks3YD4OUlCFvaJj3Evj\/DkCXlFZtlNl3gDjuqtqV9kdik1Y         Mb8A=="
          },
          {
            "Name": "X-Gm-Message-State",
            "Value": "ALoCoQkayTcRFkBOyq3KWJH0H5tj07sHBTj9FIrq285G168NRQfNWD7yDyroIVPC9SC\/9ERomY1o"
          },
          {
            "Name": "X-Received",
            "Value": "by 10.140.233.212 with SMTP id e203mr7464802qhc.83.1424538419018;        Sat, 21 Feb 2015 09:06:59 -0800 (PST)"
          },
          {
            "Name": "X-Forwarded-To",
            "Value": "58db53f3a8351b93ce07ff0343e70946@inbound.postmarkapp.com"
          },
          {
            "Name": "X-Forwarded-For",
            "Value": "mail@wampum.io 58db53f3a8351b93ce07ff0343e70946@inbound.postmarkapp.com"
          },
          {
            "Name": "Delivered-To",
            "Value": "mail@wampum.io"
          },
          {
            "Name": "X-Received",
            "Value": "by 10.141.18.146 with SMTP id u140mr7380752qhd.48.1424538418577;        Sat, 21 Feb 2015 09:06:58 -0800 (PST)"
          },
          {
            "Name": "Received-SPF",
            "Value": "pass (google.com: domain of rob.culliton@gmail.com designates 2607:f8b0:400d:c01::233 as permitted sender) client-ip=2607:f8b0:400d:c01::233;"
          },
          {
            "Name": "Authentication-Results",
            "Value": "mx.google.com;       spf=pass (google.com: domain of rob.culliton@gmail.com designates 2607:f8b0:400d:c01::233 as permitted sender) smtp.mail=rob.culliton@gmail.com;       dkim=pass header.i=@gmail.com;       dmarc=pass (p=NONE dis=NONE) header.from=gmail.com"
          },
          {
            "Name": "DKIM-Signature",
            "Value": "v=1; a=rsa-sha256; c=relaxed\/relaxed;        d=gmail.com; s=20120113;        h=date:mime-version:message-id:from:to:subject:content-type;        bh=pYrGrIZYHTqZRex2yYkN0eKgLm7XvrVaEla0Ufg2QCI=;        b=ElocF8p3RgcChgcV\/SicaMJFoWsyfpScmCFV9moxNl5cLRTLVvLvLC7rC9xCIpyKSv         4J47nCPW9QQsoDyUzFDbafs3OKkF5QqXERvbja6cvFU27Gqya7FwfTawRWGq4TqmG3Ck         HL0zdLPGXXFRdqfdJh3EP+hUJ6TPIvIyqjVHtuN4VhXOSiE3Vzeun3ccsPpJzU350o1X         WdZf4ne52eTjlu6\/PMRxnbQ0Xaa8CHoemEaRghprNg2AW+o37jhHWvewALivjIVqkSWY         YpeXI4ndaYG1Q+iScy\/nWHmShWqLmndb32tlAzYnE5HTcLmhQjKd9oTDPqrKjKNm8c1K         vxzQ=="
          },
          {
            "Name": "X-Received",
            "Value": "by 10.140.151.65 with SMTP id 62mr7226268qhx.73.1424538418292;        Sat, 21 Feb 2015 09:06:58 -0800 (PST)"
          },
          {
            "Name": "X-Google-Original-Date",
            "Value": "Sat, 21 Feb 2015 17:06:57 GMT"
          },
          {
            "Name": "MIME-Version",
            "Value": "1.0"
          },
          {
            "Name": "X-Mailer",
            "Value": "Nodemailer (0.5.0; +http:\/\/www.nodemailer.com\/)"
          },
          {
            "Name": "Message-Id",
            "Value": "<1424538417342.511370b5@Nodemailer>"
          },
          {
            "Name": "X-Orchestra-Oid",
            "Value": "FBE7F21B-AD9C-4FA8-9978-852D157286D5"
          },
          {
            "Name": "X-Orchestra-Sig",
            "Value": "58fd4690edeb173b7acc1f1e1cfe319cdf3204fa"
          },
          {
            "Name": "X-Orchestra-Thrid",
            "Value": "445050FF-A087-45D3-AFA9-3F78077A7408"
          },
          {
            "Name": "X-Orchestra-Thrid-Sig",
            "Value": "30b70acaeccacad5b67bd716a81a2a5f79849c87"
          },
          {
            "Name": "X-Orchestra-Account",
            "Value": "84690248f75635f69b867bd03083b094cc6b2769"
          }
        ],
        "Attachments": [
          {
            "Name": "2.21.15Notes.rtf",
            "Content": "e1xydGYxXGFuc2lcYW5zaWNwZzEyNTJcY29jb2FydGYxMzQzXGNvY29hc3VicnRmMTQwCntcZm9udHRibFxmMFxmc3dpc3NcZmNoYXJzZXQwIEhlbHZldGljYTtcZjFcZm1vZGVyblxmY2hhcnNldDAgQ291cmllcjtcZjJcZnJvbWFuXGZjaGFyc2V0MCBUaW1lcy1Sb21hbjsKXGYzXGZuaWxcZmNoYXJzZXQwIEhlbHZldGljYU5ldWU7fQp7XGNvbG9ydGJsO1xyZWQyNTVcZ3JlZW4yNTVcYmx1ZTI1NTtccmVkODJcZ3JlZW4wXGJsdWU4MztccmVkMjQ1XGdyZWVuMjQ1XGJsdWUyNDU7XHJlZDgzXGdyZWVuODVcYmx1ZTI7ClxyZWQzOFxncmVlbjM4XGJsdWUzODtccmVkODNcZ3JlZW44M1xibHVlODM7XHJlZDI0OVxncmVlbjI0OVxibHVlMjQ5O1xyZWQyNTVcZ3JlZW4yNTVcYmx1ZTI1NTt9ClxtYXJnbDE0NDBcbWFyZ3IxNDQwXHZpZXd3MTA4MDBcdmlld2g4NDAwXHZpZXdraW5kMApccGFyZFx0eDcyMFx0eDE0NDBcdHgyMTYwXHR4Mjg4MFx0eDM2MDBcdHg0MzIwXHR4NTA0MFx0eDU3NjBcdHg2NDgwXHR4NzIwMFx0eDc5MjBcdHg4NjQwXHBhcmRpcm5hdHVyYWwKClxmMFxmczI0IFxjZjAgMS4gcGFyc2Ugc29uIHN0cmluZyB3aXRoIHBsYXlcJzkycyBidWlsdCBpbiBqc29uIGxpYnJhcnksIHdoaWNoIHJldHVybnMgYSBqc1ZhbHVlXAoyLiB0cmF2ZXJzZSB0aGUgSnNWYWx1ZSB3aXRoIHRoZSBcJzkzXFxcJzk0IHN5bnRheC5cCjMuIGNvbnZlcnQganN2YWx1ZSB3aXRoIEpzVmFsdWUuYXNbVF1cClwKVGhlIGFzIG1ldGhvZCB3aWxsIHRocm93IGEgSnNSZXN1bHRFeGNlcHRpb24gaWYgdGhlIHBhdGggaXMgbm90IGZvdW5kIG9yIHRoZSBjb252ZXJzaW9uIGlzIG5vdCBwb3NzaWJsZS5cClwKeW91IGNhbiB2YWxpZGF0ZSBhIGpzdmFsdWUgd2hpY2ggc2VlbXMga2luZCBvZiB0ZWRpb3VzIG9yIHlvdSBjYW4gY29udmVydCBpdCB0byBhIG1vZGVsXApcCnJldHVybnMgYSAKXGYxXGZzMjggXGNmMiBcY2IzIFxleHBuZDBcZXhwbmR0dzBca2VybmluZzAKXG91dGwwXHN0cm9rZXdpZHRoMCBcc3Ryb2tlYzIgSnNSZXN1bHRcY2Y0IFxleHBuZDBcZXhwbmR0dzBca2VybmluZzAKXG91dGwwXHN0cm9rZXdpZHRoMCBcc3Ryb2tlYzQgW1xjZjIgXGV4cG5kMFxleHBuZHR3MFxrZXJuaW5nMApcb3V0bDBcc3Ryb2tld2lkdGgwIFxzdHJva2VjMiBQbGFjZVxjZjQgXGV4cG5kMFxleHBuZHR3MFxrZXJuaW5nMApcb3V0bDBcc3Ryb2tld2lkdGgwIFxzdHJva2VjNCBdClxmMlxmczMwIFxjZjUgXGV4cG5kMFxleHBuZHR3MFxrZXJuaW5nMApcb3V0bDBcc3Ryb2tld2lkdGgwIFxzdHJva2VjNSBcClxwYXJkXHBhcmRlZnRhYjcyMFxzbDM5MAoKXGYxIFxjZjYgXGNiNyBcZXhwbmQwXGV4cG5kdHcwXGtlcm5pbmcwClxvdXRsMFxzdHJva2V3aWR0aDAgXHN0cm9rZWM2IEpzUmVzdWx0W1RdClxmMyBcY2Y1IFxjYjggXGV4cG5kMFxleHBuZHR3MFxrZXJuaW5nMApcb3V0bDBcc3Ryb2tld2lkdGgwIFxzdHJva2VjNSAgY2FuIGhhdmUgMiB2YWx1ZXM6fQ==",
            "ContentType": "application/rtf",
            "ContentID": "",
            "ContentLength": 1492
          }
        ]         
    }
   """)
   
  // when nesting case classes have the argument name start with a lower case whereas the case class begins with an upper case
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
    
  val postMarkResult: JsResult[postMarkMitt] = json.validate[postMarkMitt]

  def sample() = Action {
     request =>

     postMarkResult match {
        case s: JsSuccess[postMarkMitt] => val result = s.get
          Ok(result.attachments.length.toString())
        //case e: JsError => println("Errors: " + JsError.toFlatJson(e).toString()) 
      }
    
     

  }
  

}