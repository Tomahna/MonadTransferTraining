package example

import munit._
import scala.concurrent.ExecutionContext.Implicits.global

class UserServiceSpec extends FunSuite {

  test("userService.getUserAndProfile should fail on user with unknown user"){
    val userService = buildUserService()
    userService.getUserAndProfile(User.Id(-5))
      .map(assertEquals(_, Left("User not found")))
  }
  test("userService.getUserAndProfile should fail on user with no profile"){
    val userService = buildUserService()
    userService.getUserAndProfile(User.Id(5))
      .map(assertEquals(_, Left("Profile not found")))
  }
  test("userService.getUserAndProfile should work on user with a profile"){
    val userService = buildUserService()
    userService.getUserAndProfile(User.Id(1))
      .map(assertEquals(_, Right(User(User.Id(1), Profile.Id(1), "bob", "dylan", 58) -> Profile(Profile.Id(1), "rock"))))
  }

  test("userService.getUserOfLegalAge should fail on stew young") {
    val userService = buildUserService()
    userService.getUserOfLegalAge(User.Id(4))
      .map(assertEquals(_, Left("User is too young")))
  }


  private def buildUserService(): UserService =
    new UserService(new UserRepository, new ProfileRepository)

}
