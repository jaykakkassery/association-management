package models

import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._
import play.api.Logging


case class UserPerformance (id: Long, userId: Long, associationId: Long, eventId: Long, performanceName: String, performanceOrder: Int)
case class UserPerformanceDetail (id: Long, userName: String, associationName: String, eventId: Long, performanceName: String, performanceOrder: Int)


class UserPerformanceTableDef(tag: Tag) extends Table[UserPerformance](tag, "user_performance") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def userId = column[Long]("user_id")
  def associationId = column[Long]("association_id")
  def eventId = column[Long]("event_id")
  def performanceName = column[String]("performance_name")
  def performanceOrder = column[Int]("performance_order")

  def userFK = foreignKey("userFK" , userId, TableQuery[UserTableDef]) (_.id ,onDelete= ForeignKeyAction.Cascade)
  def associationFK = foreignKey("associationFK" , associationId, TableQuery[AssociationTableDef]) (_.id ,onDelete= ForeignKeyAction.Cascade)

  override def * =
    (id, userId, associationId, eventId, performanceName, performanceOrder) <>(UserPerformance.tupled, UserPerformance.unapply)
}

class UserPerformances @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with Logging {

  val userPerformances = TableQuery[UserPerformanceTableDef]

  def add(userPerformance: UserPerformance): Future[String] = {
    dbConfig.db.run(userPerformances += userPerformance).map(res => "UserPerformance successfully added").recover {
      case ex: Exception => {
        logger.error(s"exception is.. $ex")
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

  def listAll: Future[Seq[UserPerformanceDetail]] = {
    lazy val userTblQuery: TableQuery[UserTableDef] = TableQuery[UserTableDef]
    lazy val userPerformanceTblQuery: TableQuery[UserPerformanceTableDef] = TableQuery[UserPerformanceTableDef]
    lazy val associationTblQuery: TableQuery[AssociationTableDef] = TableQuery[AssociationTableDef]

    //Monadic Joins (Navigating using Foreign Keys ) - Inner Join
    logger.debug("Monadic Query")
    val allTablesQry =
      for {
        user <- userTblQuery
        userPerformance <- userPerformanceTblQuery if user.id === userPerformance.userId
        association <- associationTblQuery if(userPerformance.associationId === association.id)
      } yield (user, userPerformance, association)

   val xx =  dbConfig.db.run(allTablesQry.result)

   //Construct for return
   val yy = xx.map{ r =>
      for {
        (user, userPeformance, association) <- r
      } yield {
        UserPerformanceDetail(
          userPeformance.id,
          user.firstName,
          association.name,
          userPeformance.eventId,
          userPeformance.performanceName,
          userPeformance.performanceOrder)
      }
    }
    yy

  }

}
