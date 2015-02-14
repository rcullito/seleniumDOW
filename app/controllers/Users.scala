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
  
  def getRequestValue(input: Request[JsValue], keyName: String): Option[String] = {
        return (input.body \ keyName).asOpt[String]
  }  

  
  def showtime() = Action(parse.json) {
    request =>


   val requestBodyParamNames: List[String] = List("email", "url")   

   // only proceed if each of these is a some
   val requestBodyParams: List[Option[String]] = requestBodyParamNames.map(getRequestValue(request, _))
    
    val validRequestBodyParams = for (i <- requestBodyParams if i.isDefined) yield  {
     i match {
       case Some(i) => i
     } 
    }
   
   if (requestBodyParamNames.length == validRequestBodyParams.length) {
        val endPageTitle = kickOffSelenium(validRequestBodyParams(0), validRequestBodyParams(1))
        Ok(endPageTitle)
   } else {
     BadRequest("Invalid request")
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