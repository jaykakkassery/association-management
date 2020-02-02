package models

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

case class Association(id: Long, name: String, address1: String, address2: String,  city: String, state: String, zip: String, contactNo: Long, email: String)

case class AssociationFormData(name: String, address1: String, address2: String,  city: String, state: String, zip: String, contactNo: Long, email: String)

object AssociationForm {

  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "address1" -> nonEmptyText,
      "address2" -> text,
      "city" -> nonEmptyText,
      "state" -> nonEmptyText,
      "zip" -> nonEmptyText,
      "contactNo" -> longNumber,
      "email" -> email
    )(AssociationFormData.apply)(AssociationFormData.unapply)
  )
}

import slick.jdbc.MySQLProfile.api._

class AssociationTableDef(tag: Tag) extends Table[Association](tag, "association") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  def address1 = column[String]("address1")
  def address2 = column[String]("address2")
  def city = column[String]("city")
  def state = column[String]("state")
  def zip = column[String]("zip")
  def contactNo = column[Long]("contact_no")
  def email = column[String]("email")

  override def * =
    (id, name, address1, address2,  city, state, zip, contactNo, email) <>(Association.tupled, Association.unapply)
}

class Associations @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val associations = TableQuery[AssociationTableDef]

  def add(association: Association): Future[String] = {
    dbConfig.db.run(associations += association).map(res => "Association successfully added").recover {
      case ex: Exception => {
        println("exception is.."+ ex)
        ex.getCause.getMessage
      }
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(associations.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[Association]] = {
    dbConfig.db.run(associations.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Association]] = {
    dbConfig.db.run(associations.result)
  }

}
