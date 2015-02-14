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


object Users extends Controller {

  
  val userForm = Form(
    mapping(
      "prefix" -> text,
      "firstname" -> text,
      "lastname" -> text,
      "email" -> text,
      "address1" -> text,
      "city" -> text,
      "state" -> text,
      "zip" -> text
    )(models.User.apply)(models.User.unapply)
  )
  
  
  def submitForm = Action {
      implicit request =>
      userForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.users(formWithErrors))
        },
        userData => {
          /* binding success, you get the actual value. */
          val newUser = models.User(userData.prefix,  userData.firstname,  userData.lastname,  userData.email,  userData.address1, userData.city,  userData.state,  userData.zip)
          models.User.create(newUser)
          Redirect(routes.Application.index())
        }
      )         
  }
  
  def getRequestValues(input: Request[JsValue], keyNames: List[String]): Map[String,Option[String]] = {
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
  
  def getArrayVals(requestLookups: Map[String, Option[String]]): Array[String] = {
       val definedRequestValues = for {
          (key, value) <- requestLookups
          definedValue <- value
        } yield (definedValue)

        definedRequestValues.toArray
  }


  
  def showtime() = Action(parse.json) {
    request =>


   val requestBodyParamNames: List[String] = List("email", "url")  
   val requestLookups = getRequestValues(request, requestBodyParamNames)
   val missingParams = getMissingParams(requestLookups)  
    
    println(missingParams)

    missingParams match {
     
      case List() => 
        val arrayVals = getArrayVals(requestLookups)
        val endPageTitle = kickOffSelenium(arrayVals(0), arrayVals(1))
        Ok(endPageTitle)
        
      case _ =>
        // TODO display this more prettily
        BadRequest("missing parameters" + missingParams.toString)
    }
  }

  
  def index = Action {
     Ok(views.html.users(userForm)) 
  }
  
  def fetchEmails = Action {
     val accounts = models.User.fetchEmails()
     Ok(accounts);
  }
  
  def fetchByEmail(email: String) = Action {
     val account = models.User.fetchByEmail(email)
     Ok(account.toString());
  }
  

}