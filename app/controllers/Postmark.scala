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
      }
    }
   """)
   
  // when nesting case classes have the argument name start with a lower case whereas the case class begins with an upper case
  case class FromFull(email: String, name: String, mailboxHash: String)
  case class postMarkMitt(fromName: String, from: String, fromFull: FromFull) 
   
  implicit val fromFullReads: Reads[FromFull] = (
     (JsPath \ "Email").read[String] and
     (JsPath \ "Name").read[String] and
     (JsPath \ "MailboxHash").read[String]  
  )(FromFull.apply _)

  
  implicit val postMarkReads: Reads[postMarkMitt] = (
      (JsPath \ "FromName").read[String] and
      (JsPath \ "From").read[String] and
      (JsPath \ "FromFull").read[FromFull]
    )(postMarkMitt.apply _)
    
  val locationResult: JsResult[postMarkMitt] = json.validate[postMarkMitt]
    
    
  
  def sample() = Action {
     request =>

     locationResult match {
        case s: JsSuccess[postMarkMitt] => val result = s.get
          Ok(result.fromFull.mailboxHash)
        //case e: JsError => println("Errors: " + JsError.toFlatJson(e).toString()) 
      }
    
     

  }
  

}