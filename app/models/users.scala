package models
import com.datastax.driver.core._
import scala.collection.JavaConversions._
case class User(
    prefix: String,
    firstname: String,
    lastname: String,
    email: String,
    address1: String,
    city: String,
    state: String,
    zip: String
)

object User {
  
    val cluster: Cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
    val session = cluster.connect("demo");
  
  def create(user: User) {
    val statement= session.prepare("INSERT INTO accounts" + "(prefix, firstname, lastname, email, address1, city, state, zip)" + "VALUES (?,?,?,?,?,?,?,?);");
    val boundStatement = new BoundStatement(statement);
    session.execute(boundStatement.bind(user.prefix, user.firstname, user.lastname, user.email, user.address1, user.city, user.state, user.zip));    
  }
  
  
  def fetchEmails(): String = {
    val results: ResultSet = session.execute("SELECT * FROM accounts")
    
    val emails = results.map { x => x.getString("email") }
    
    emails.toString()
    
  }
  
}