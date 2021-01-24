package controllers

import javax.inject._
import models.{Association, User, UserForm, UserPerformance}
import play.api.Logging
import play.api.mvc._
import services.{AssociationService, UserPerformanceService, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.parser._
import io.circe.syntax._


@Singleton
class ApplicationController @Inject()(cc: ControllerComponents,
                                      userService: UserService,
                                      associationService: AssociationService,
                                      userPerformanceService: UserPerformanceService) extends AbstractController(cc) with Logging {

  def index() = Action.async { implicit request: Request[AnyContent] =>
    userService.listAllUsers map { users =>
      Ok(views.html.index(UserForm.form, users))
    }
  }

  def addUser() = Action.async { implicit request: Request[AnyContent] =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        logger.warn(s"Form submission with error: ${errorForm.errors}")
        Future.successful(Ok(views.html.index(errorForm, Seq.empty[User])))
      },
      data => {
        val newUser = User(0, data.firstName, data.lastName, data.mobile, data.email)
        userService.addUser(newUser).map( _ => Redirect(routes.ApplicationController.index()))
      })
  }

  def deleteUser(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    userService.deleteUser(id) map { res =>
      Redirect(routes.ApplicationController.index())
    }
  }


  def addAssociation() = Action.async { implicit request: Request[AnyContent] =>
    //val association = Association(0, "GAMA", "addr1", "addr2", "Atlanta", "GA", "30041", 6787029647L, "jpkakkassery@gmail.com")

    implicit val decoder: Decoder[Association] = deriveDecoder[Association]
    implicit val encoder: Encoder[Association] = deriveEncoder[Association]

    val bodyAsJson = request.body.asJson.get.toString()
    decode[Association](bodyAsJson) match {
      case Right(association) => {
        println(association)
        associationService.addAssociation(association).map( _ => Redirect(routes.ApplicationController.index()))
      }
      case Left(ex) => {
        println(s"Ooops some errror here ${ex}")
        Future(Redirect(routes.ApplicationController.index()))
      }
    }

   // associationService.addAssociation(association).map( _ => Redirect(routes.ApplicationController.index()))
  }

  def listAssociation() = Action.async { implicit request: Request[AnyContent] =>
    associationService.listAllAssociations map { associations =>
    val a = associations.asJson.toString()
      logger.warn(s"${a}")
      Ok(a)
    }
  }

  def addUserPerformance() = Action.async { implicit request: Request[AnyContent] =>

    implicit val decoder: Decoder[UserPerformance] = deriveDecoder[UserPerformance]
    implicit val encoder: Encoder[UserPerformance] = deriveEncoder[UserPerformance]

    val bodyAsJson = request.body.asJson.get.toString()
    decode[UserPerformance](bodyAsJson) match {
      case Right(userPerformance) => {
        println(userPerformance)
        userPerformanceService.addUserPerformance(userPerformance).map( _ => Redirect(routes.ApplicationController.index()))
      }
      case Left(ex) => {
        println(s"Ooops some errror here ${ex}")
        Future(Redirect(routes.ApplicationController.index()))
      }
    }

    // associationService.addAssociation(association).map( _ => Redirect(routes.ApplicationController.index()))
  }

}