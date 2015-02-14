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
  
  def index = Action {
     Ok(views.html.users(userForm)) 
  }
  
  def thanks = Action {
    Ok(views.html.thanks())
  }
  
  
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
          Redirect(routes.Users.thanks())
        }
      )         
  }
}