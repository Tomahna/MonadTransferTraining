package example

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserService(userRepository: UserRepository, profileRepository: ProfileRepository){

  def getUserAndProfile(id: User.Id): Future[Either[String, (User, Profile)]] =
    for {
      evUser <- userRepository.get(id)
      result <- evUser match {
        case None =>    Future.successful(Left("User not found"))
        case Some(user) =>
          for {
            evProfile <- profileRepository.get(user.profile)
            result = evProfile match {
              case Some(profile) => Right(user -> profile)
              case None => Left("Profile not found")
            }
          } yield result
      }
    } yield result

  def getUserOfLegalAge(id: User.Id): Future[Either[String, User]] =
    for {
      evUser <- userRepository.get(id)
      user = evUser match {
        case None => Left("User not found")
        case Some(user) =>
          if(user.age < 18)  Left("User is too young")
          else Right(user)
      }
    } yield user

  def createUserOfLegalAgeWithSponsorship(user: User, sponsor: User.Id): Future[Either[String, Unit]] =
    for {
      evSponsor <- userRepository.get(sponsor)
      result <- evSponsor match {
        case None => Future.successful(Left("Sponsor not found"))
        case Some(_) =>
          if(user.age < 18) Future.successful(Left("User is too young"))
          else userRepository.create(user)
      }
    } yield result

  def createUserWithBanCheck(user: User): Future[Either[String, Unit]] =
    ???

  private val banlist = Set("Insulting name")
  def nameIsNotBanned(user: User): Either[String, User] =
    if(banlist.contains(user.firstName) || banlist.contains(user.lastName)) Left("User is banned")
    else Right(user)

}
