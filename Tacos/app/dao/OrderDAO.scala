package dao

import javax.inject.{Inject, Singleton}

import models.{Order, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the Order class with other DAO, thanks to the inheritance.
trait OrderComponent extends UserComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's "commande" table in a object-oriented entity: the Order model.
  class OrderTable(tag: Tag) extends Table[Order](tag, "commande") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def dateOrder = column[String]("dateCommande")
    def hourOrder = column[String]("heureCommande")
    def price = column[Double]("prix")
    def user = column[Long]("personne_fk")

    // Map the attributes with the model.
    def * = (id, dateOrder.?, hourOrder, price, user) <> (Order.tupled, Order.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.

@Singleton
class OrderDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends OrderComponent with UserComponent with RoleUserComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // Get the object-oriented list of orders directly from the query table.
  val orders = TableQuery[OrderTable]

  /** Retrieve the list of orders */
  def list(): Future[Seq[Order]] = {
    val query = orders.sortBy(o => (o.dateOrder, o.hourOrder))
    db.run(query.result)
  }

  /** Retrieve the list of orders for a specific day */
  def list(day: String): Future[Seq[Order]] = {
    val query = orders.filter(_.dateOrder === day).sortBy(o => o.hourOrder)
    db.run(query.result)
  }
}