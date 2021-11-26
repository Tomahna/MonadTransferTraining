package example

case class Profile(id: Profile.Id, preference: String)
object Profile {
  case class Id(value: Int) extends AnyVal
}
