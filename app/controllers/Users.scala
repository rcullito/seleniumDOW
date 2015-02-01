package controllers

import play.api._
import play.api.mvc._
import com.datastax.driver.core._

import play.api.data._
import play.api.data.Forms._

import models.User
import models.Charges


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
  
  def showtime(emailInput: String, url: String) = Action {
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
    // we have all the strings we need now
    // send these as arguments to a model though
    //models.Charge.launch(url, prefix, firstname, lastname, email, address1, city, state, zip)
    // we need to call teh charge file
    charge.launch()
    
    
    Ok("clear")
  
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