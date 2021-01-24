package services

import com.google.inject.Inject
import models.{UserPerformance, UserPerformances}

import scala.concurrent.Future

class UserPerformanceService @Inject() (userPerformances: UserPerformances) {

  def addUserPerformance(userPerformance: UserPerformance): Future[String] = {
    userPerformances.add(userPerformance)
  }

  def deleteAssociation(id: Long): Future[Int] = {
    userPerformances.delete(id)
  }

  def getAssociation(id: Long): Future[Option[UserPerformance]] = {
    userPerformances.get(id)
  }

  def listAllAssociations: Future[Seq[UserPerformance]] = {
    userPerformances.listAll
  }
}