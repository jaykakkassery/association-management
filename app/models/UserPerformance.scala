package models

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

case class UserPerformance (id: Long, userId: Long, associationId: Long, eventId: Long, performanceName: String, performanceOrder: Int)

class UserPerformanceTableDef(tag: Tag) extends Table[UserPerformance](tag, "user_performance") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def userId = column[Long]("user_id")
  def associationId = column[Long]("association_id")
  def eventId = column[Long]("event_id")
  def performanceName = column[String]("performance_name")
  def performanceOrder = column[Int]("performance_order")


  override def * =
    (id, userId, associationId, eventId, performanceName, performanceOrder) <>(UserPerformance.tupled, UserPerformance.unapply)
}

class UserPerformances @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val userPerformances = TableQuery[UserPerformanceTableDef]

  def add(userPerformance: UserPerformance): Future[String] = {
    dbConfig.db.run(userPerformances += userPerformance).map(res => "UserPerformance successfully added").recover {
      case ex: Exception => {
        println("exception is.."+ ex)
        ex.getCause.getMessage
      }
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(userPerformances.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[UserPerformance]] = {
    dbConfig.db.run(userPerformances.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[UserPerformance]] = {
    dbConfig.db.run(userPerformances.result)
  }

}
