package controllers

import play.api._
import play.api.mvc._
import com.datastax.driver.core._

import play.api.data._
import play.api.data.Forms._

import models.User
import models.Charges

import play.api.mvc.Request
import play.api.libs.json.JsValue

import scala.util.{Try, Success, Failure}


object Showtime extends Controller {
  
  def buildRequestMap(input: Request[JsValue], keyNames: List[String]): Map[String,Option[String]] = {
    var requestInputs:Map[String,Option[String]] = Map()
    keyNames.foreach { keyName => requestInputs += (keyName -> (input.body \ keyName).asOpt[String]) }
    return requestInputs
  }
  
  def getMissingParams(requestLookups: Map[String, Option[String]]): Iterable[String] = {
    for {
      (key, value) <- requestLookups
      if !value.isDefined
    } yield (key)  
  }   
  
  def getValidRequestParams(requestLookups: Map[String, Option[String]]): Array[String] = {
       val definedRequestValues = for {
          (key, value) <- requestLookups
          definedValue <- value
        } yield (definedValue)

        definedRequestValues.toArray
  }
  
  
  def kickOffSelenium(emailInput: String, url: String): String = {
        val account = models.User.fetchByEmail(emailInput)
        val prefix: String = account.getString("prefix")    
        val firstname: String = account.getString("firstname")
        val lastname: String = account.getString("lastname")    
        val email: String = account.getString("email")
        val address1: String = account.getString("address1")
        val city: String = account.getString("city")
        val state: String = account.getString("state")
        val zip: String = account.getString("zip")
        val charge = new Charges
        val endPageTitle = charge.launch(url, prefix, firstname, lastname, email, address1, city, state, zip)
        return endPageTitle
  }  

  def index() = Action(parse.json) {
    request =>

   val requestBodyParamNames: List[String] = List("email", "url")  
   val requestLookups = buildRequestMap(request, requestBodyParamNames)
   val missingParams = getMissingParams(requestLookups)  

    missingParams match {
     
      case List() => 
        val validRequestParams = getValidRequestParams(requestLookups)
        val endPageTitle = kickOffSelenium(validRequestParams(0), validRequestParams(1))
        Ok(endPageTitle)
        
      case _ =>
        // TODO display this more prettily
        BadRequest("missing parameters" + missingParams.toString)
    }
  }
  

}