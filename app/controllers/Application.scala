package controllers

import play.api._
import play.api.mvc._
import com.datastax.driver.core.Cluster

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  
  def signup = Action { implicit request =>
    val cluster: Cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
    val session = cluster.connect("demo");
    session.execute("INSERT INTO users (lastname, age, city, email, firstname) VALUES ('Jones', 35, 'Austin', 'bob@example.com', 'Bob')");
    Ok("we just signed up a user")
  }
    
    
 def hack = Action {
   implicit request =>
   
   val querystring: Map[String, Seq[String]] = request.queryString;
   
   querystring foreach{ mapPair => 
     println(mapPair._1)
     println(mapPair._2.last)
   }
   
    Ok("for testing stuff out")
  }

}