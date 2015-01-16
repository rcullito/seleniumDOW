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
    
    val querystring: Map[String, Seq[String]] = request.queryString;
    
    val lastname = querystring.get("lastname").mkString
    
    println(lastname)
    
    
    
        val statement= session.prepare("INSERT INTO users" + "(lastname, age, city, email, firstname)" + "VALUES (?,?,?,?,?);");
 
        val boundStatement = new BoundStatement(statement);
        
        val age: Int = 35
        session.execute(boundStatement.bind(lastname, 35, "Austin", "bob@example.com", "Bob"));
    
    
    
    session.execute("INSERT INTO users (lastname, age, city, email, firstname) VALUES ('Jones', 35, 'Austin', 'bob@example.com', 'Bob')");
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