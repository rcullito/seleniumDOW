package controllers

import play.api._
import play.api.mvc._
import com.datastax.driver.core._

import play.api.data._
import play.api.data.Forms._

object Incoming extends Controller {


  
  
  def index = Action {
    // this should ideally just have the email of the person signed up and the url
    
    // read from the database
     Ok(views.html.incoming()) 
  }

}