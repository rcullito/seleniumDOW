package controllers

import play.api._
import play.api.mvc._

import play.api.mvc.Request

import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.io.Source._

import models.Postmark

object Mail extends Controller {
  
  val source = scala.io.Source.fromFile("exampleWebHookAllFields.json")
  val postMarkResult = Postmark.parseJson(source.mkString)
  source.close()

  def sample() = Action {
     request =>

     postMarkResult match {
        case s: JsSuccess[postMarkMitt] => val result = s.get
          Ok(result.attachments.length.toString())
        //case e: JsError => println("Errors: " + JsError.toFlatJson(e).toString()) 
      }
    
     

  }
  

}