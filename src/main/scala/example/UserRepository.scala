package example

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserRepository {

  private val map = mutable.Map[User.Id, User](
    User.Id(1) -> User(User.Id(1), Profile.Id(1), "bob", "dylan", 58),
    User.Id(2) -> User(User.Id(2), Profile.Id(2), "kurt", "cobain", 45),
    User.Id(3) -> User(User.Id(3), Profile.Id(3), "albert", "einstein", 128),
    User.Id(4) -> User(User.Id(4), Profile.Id(4), "stew", "young", 15),
    User.Id(5) -> User(User.Id(5), Profile.Id(5), "famous", "unknown", 28)
  )

  def get(id: User.Id): Future[Option[User]] =
    Future(map.get(id))

  def create(user: User): Future[Either[String, Unit]] =
    Future {
      if(map.contains(user.id)) Left("Conflicting primary key")
      else Right(map.addOne(user.id -> user))
    }

  def update(user: User): Future[Either[String, Unit]] =
    Future {
      if(map.contains(user.id)) Right(map.addOne(user.id -> user))
      else Left("User does not exist")
    }

  def delete(id: User.Id): Future[Either[String, Unit]] =
    Future {
      if (map.contains(id)) Right(map.remove(id))
      else Left("User does not exist")
    }
}
