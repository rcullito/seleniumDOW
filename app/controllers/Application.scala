package controllers

import play.api._
import play.api.mvc._
import com.datastax.driver.core._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def signup = Action { implicit request =>  
    val cluster: Cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
    val session = cluster.connect("demo");

    val querystring = request.queryString.map { case (k,v) => 
      k -> v.mkString 
    }
    
    val lastname = querystring.apply("lastname")
    val firstname = querystring.apply("firstname")
    val email = querystring.apply("email")
    
    val statement= session.prepare("INSERT INTO accounts" + "(lastname, firstname, email)" + "VALUES (?,?,?);");
    val boundStatement = new BoundStatement(statement);
    session.execute(boundStatement.bind(lastname, firstname, email));

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