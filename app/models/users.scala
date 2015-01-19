package models
import com.datastax.driver.core._
case class User(name: String, email: String)

object User {
  
  def create(user: User) {
    
    val cluster: Cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
    val session = cluster.connect("demo");

    val lastname = user.name;
    val email = user.email;
    
    println(lastname)
    println(email)
    
    val statement= session.prepare("INSERT INTO accounts" + "(lastname, email)" + "VALUES (?,?);");
    val boundStatement = new BoundStatement(statement);
    session.execute(boundStatement.bind(lastname, email));
    
    println("Hello, world!")
    
  }
  
  def delete(id: Long) {}
  
}