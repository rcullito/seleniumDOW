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

      Logger.info("here man")  
        
      userForm.bindFromRequest.fold(
        formWithErrors => {
          println("this is not good")
          BadRequest(views.html.incoming(formWithErrors))
        },
        userData => {
          /* binding success, you get the actual value. */
          val newUser = models.User(userData.name, userData.email)
          models.User.create(newUser)
          Redirect(routes.Application.index())
        }
      )         
  }

  
  def index = Action {
     Ok(views.html.incoming(userForm)) 
  }

}