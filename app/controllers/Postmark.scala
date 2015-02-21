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
  
  case class Location(lat: String, long: String)
  
  val json: JsValue = Json.parse("""
    {
        "lat" : "51.235685",
        "long" : "-1.309197"
    }
   """)
  
  implicit val locationReads: Reads[Location] = (
      (JsPath \ "lat").read[String] and
      (JsPath \ "long").read[String]
    )(Location.apply _)
    
  val locationResult: JsResult[Location] = json.validate[Location]
    
    
  
  def sample() = Action {
     request =>

     locationResult match {
        case s: JsSuccess[Location] => val result = s.get
          Ok(result.lat)
        //case e: JsError => println("Errors: " + JsError.toFlatJson(e).toString()) 
      }
    
     

  }
  

}