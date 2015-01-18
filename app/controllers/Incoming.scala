package controllers

import play.api._
import play.api.mvc._
import com.datastax.driver.core._

import play.api.data._
import play.api.data.Forms._

import models.User


object Incoming extends Controller {

  
  val userForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> email
    )(models.User.apply)(models.User.unapply)
  )
  
  
  def submitForm = Action {
      implicit request =>

      userForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.incoming(formWithErrors))
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
     Ok(views.html.incoming(userForm)) 
  }

}