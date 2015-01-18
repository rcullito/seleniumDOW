package controllers

import play.api._
import play.api.mvc._
import com.datastax.driver.core._

import play.api.data._
import play.api.data.Forms._

import models.User


object Incoming extends Controller {

//  case class UserData(name: String, email: String)
  
  val userForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> email
    )(models.User.apply)(models.User.unapply)
  )
  
  val anyData = Map("name" -> "bob", "age" -> "21")
  val userData = userForm.bind(anyData).get
  
//  val userData = userForm.bindFromRequest.get
  


  def submitForm = Action {
      implicit request =>
        
      userForm.bindFromRequest.fold(
        formWithErrors => {
          // binding failure, you retrieve the form containing errors:
          
          // TODO include formWithErrors as argument here
          BadRequest(views.html.incoming())
        },
        userData => {
          /* binding success, you get the actual value. */
          val newUser = models.User(userData.name, userData.email)
          val id = models.User.create(newUser)
          Redirect(routes.Application.index())
        }
      )        
        
        
      Ok("whoo")
  }

  
  def index = Action {
    // this should ideally just have the email of the person signed up and the url
    
    // read from the database
     Ok(views.html.incoming()) 
  }

}