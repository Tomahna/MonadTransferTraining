package example

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ProfileRepository {

  private val map = mutable.Map[Profile.Id, Profile](
    Profile.Id(1) -> Profile(Profile.Id(1), "rock"),
    Profile.Id(2) -> Profile(Profile.Id(2), "pop"),
    Profile.Id(3) -> Profile(Profile.Id(3), "quantum theory"),
    Profile.Id(4) -> Profile(Profile.Id(4), "beer")
  )

  def get(id: Profile.Id): Future[Option[Profile]] =
    Future(map.get(id))

  def create(user: Profile): Future[Either[String, Unit]] =
    Future {
      if(map.contains(user.id)) Left("Conflicting primary key")
      else Right(map.addOne(user.id -> user))
    }

  def update(user: Profile): Future[Either[String, Unit]] =
    Future {
      if(map.contains(user.id)) Right(map.addOne(user.id -> user))
      else Left("Profile does not exist")
    }

  def delete(id: Profile.Id): Future[Either[String, Unit]] =
    Future {
      if (map.contains(id)) Right(map.remove(id))
      else Left("Profile does not exist")
    }
}
