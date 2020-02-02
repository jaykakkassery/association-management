package controllers

import javax.inject._
import models.{Association, User, UserForm}
import play.api.Logging
import play.api.mvc._
import services.{AssociationService, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._


@Singleton
class ApplicationController @Inject()(cc: ControllerComponents, userService: UserService, associationService: AssociationService) extends AbstractController(cc) with Logging {

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
    val association = Association(0, "GAMA", "addr1", "addr2", "Atlanta", "GA", "30041", 6787029647L, "jpkakkassery@gmail.com")
    associationService.addAssociation(association).map( _ => Redirect(routes.ApplicationController.index()))
  }
  def listAssociation() = Action.async { implicit request: Request[AnyContent] =>
    associationService.listAllAssociations map { associations =>
    val a = associations.asJson.toString()
      logger.warn(s"${a}")
      Ok(a)
    }
  }

}