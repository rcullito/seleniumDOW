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
  
  case class ExpectedRequest(email: String, url: String, ghostProtocol: Boolean)
  
  def kickOffSelenium(emailInput: String, url: String, ghostProtocol: Boolean): String = {
        val account = models.User.fetchByEmail(emailInput)
        if (account == null) {
          throw UserNotFoundException("User not found!")
        }
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
  
  
  def parseInputAndLaunch(request: Request[JsValue]): String = {
      val incomingRequest = ExpectedRequest((request.body \ "email").as[String], (request.body \ "url").as[String], (request.body \ "ghostProtocol").as[Boolean])
      kickOffSelenium(incomingRequest.email, incomingRequest.url, incomingRequest.ghostProtocol)
  }
  
  def index() = Action(parse.json) {
     request =>

     val endPageTitle: Try[String] = Try(parseInputAndLaunch(request))
    
    // if there was an exception retrieving the user then return an error message with a 400
    endPageTitle match {
      case Success(endPageTitle) => Ok(endPageTitle)
      case Failure(ex) => BadRequest(ex.getMessage)            
    }

  }
  

}