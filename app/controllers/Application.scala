package controllers

import play.api._
import play.api.mvc._

import models.User
import models.Charge

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
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


    // we have all the strings we need now
    // send these as arguments to a model though
    models.Charge.launch(url, prefix, firstname, lastname, email, address1, city, state, zip)
  
    Ok("clear")
  
  }
  

    


}