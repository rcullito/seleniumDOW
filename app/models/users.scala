package models

case class User(name: String, email: String)

object User {
  
  def create(user: User) {
    
    // this seems like a good place to update the database
    
  }
  
  def delete(id: Long) {}
  
}