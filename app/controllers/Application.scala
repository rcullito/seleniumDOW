package controllers

import play.api._
import play.api.mvc._


object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def signup = Action { implicit request =>  
    Ok("we just signed up a user")
  }
    
 def hack = Action {
   implicit request =>
   
   val querystring: Map[String, Seq[String]] = request.queryString;
   
   querystring foreach { 
     case (key, value) => 
       println(key)
       println(value)
   }
   
    Ok("for testing stuff out")
  }

}