package services

import com.google.inject.Inject
import models.{Association, Associations}

import scala.concurrent.Future

class AssociationService @Inject() (associations: Associations) {

  def addAssociation(association: Association): Future[String] = {
    associations.add(association)
  }

  def deleteAssociation(id: Long): Future[Int] = {
    associations.delete(id)
  }

  def getAssociation(id: Long): Future[Option[Association]] = {
    associations.get(id)
  }

  def listAllAssociations: Future[Seq[Association]] = {
    associations.listAll
  }
}