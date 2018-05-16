package dao

import scala.concurrent.Future
import javax.inject.{Inject, Singleton}
import models.Drink
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

trait DrinkComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  // This class convert the database's drink table in a object-oriented entity: the Drink model.
  class FryTable(tag: Tag) extends Table[Drink](tag, "boisson") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
    def name = column[String]("nom")
    def deciliter = column[Int]("decilitres")
    def price = column[Double]("prix")

    // Map the attributes with the model.
    def * = (id, name, deciliter, price) <> (Drink.tupled, Drink.unapply)
  }
}

// This class contains the object-oriented list of users and offers methods to query the data.
// A DatabaseConfigProvider is injected through dependency injection; it provides a Slick type bundling a database and
// driver. The class extends the user query table and loads the JDBC profile configured in the application's
// configuration file.
