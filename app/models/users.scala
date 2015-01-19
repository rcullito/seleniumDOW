package models
import com.datastax.driver.core._
case class User(
    prefix: String,
    firstname: String,
    lastname: String,
    email: String,
    address1: String,
    address2: String,
    city: String,
    state: String,
    zip: String
)

object User {
  
  def create(user: User) {
    
    val cluster: Cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
    val session = cluster.connect("demo");

    
    val statement= session.prepare("INSERT INTO accounts" + "(prefix, firstname, lastname, email, address1, address2, city, state, zip)" + "VALUES (?,?,?,?,?,?,?,?,?);");
    val boundStatement = new BoundStatement(statement);
    session.execute(boundStatement.bind(user.prefix, user.firstname, user.lastname, user.email, user.address1, user.address2, user.city, user.state, user.zip));
    
    println("Hello, world!")
    
  }
  
  def delete(id: Long) {}
  
}