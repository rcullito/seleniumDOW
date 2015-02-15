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
  
  case class UserNotFoundException(message: String) extends Exception(message)
  
  def buildRequestMap(input: Request[JsValue], keyNames: List[String]): Map[String,Option[String]] = {
    var requestInputs:Map[String,Option[String]] = Map()
    keyNames.foreach { keyName => requestInputs += (keyName -> (input.body \ keyName).asOpt[String]) }
    requestInputs
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
        if (account == null) {
          throw UserNotFoundException("User not found!")
        }
        
        val ghostProtocol = true
        val prefix: String = account.getString("prefix")    
        val firstname: String = account.getString("firstname")
        val lastname: String = account.getString("lastname")    
        val email: String = account.getString("email")
        val address1: String = account.getString("address1")
        val city: String = account.getString("city")
        val state: String = account.getString("state")
        val zip: String = account.getString("zip")
        val charge = new Charges
        charge.launch(ghostProtocol, url, prefix, firstname, lastname, email, address1, city, state, zip)
  }  

  def index() = Action(parse.json) {
     request =>

     val requestBodyParamNames: List[String] = List("email", "url")  
     val requestLookups: Map[String, Option[String]] = buildRequestMap(request, requestBodyParamNames)
     val missingParams = getMissingParams(requestLookups)  
  
      missingParams match {
       
        // if there are no missing parameters then query for user
        case List() => 
          val validRequestParams = getValidRequestParams(requestLookups)
          val endPageTitle: Try[String] = Try(kickOffSelenium(validRequestParams(0), validRequestParams(1)))
          
          // if there was an exception retrieving the user then return an error message with a 400
          endPageTitle match {
            case Success(endPageTitle) => Ok(endPageTitle)
            case Failure(ex) => BadRequest(ex.getMessage)            
          }

        case _ =>
          BadRequest("missing parameters: " + missingParams.mkString(", "))
      }
  }
  

}