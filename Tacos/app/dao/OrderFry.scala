package dao

import scala.concurrent.Future
import javax.inject.{Inject, Singleton}
import models.OrderFry
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

// We use a trait component here in order to share the OrderFry class with other DAO, thanks to the inheritance.
trait OrderFryComponent extends OrderComponent with FryComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's orderFry table in a object-oriented entity: the OrderFry model.
  class OrderTable(tag: Tag) extends Table[OrderFry](tag, "asso_commande_frite") {
    def orderId = column[Long]("orderId", O.PrimaryKey) // Primary key
    def fryId = column[Long]("fryId", O.PrimaryKey) // Primary key
    def quantity = column[Int]("quantity")

    // Map the attributes with the model.
    def * = (orderId, fryId, quantity) <> (OrderFry.tupled, OrderFry.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
