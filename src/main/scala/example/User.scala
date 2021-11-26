package example

case class User(id: User.Id, profile: Profile.Id, firstName: String, lastName: String, age: Int)
object User {
  case class Id(value: Int) extends AnyVal
}
