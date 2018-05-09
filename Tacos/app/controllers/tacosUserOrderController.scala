package controllers

//import dao.{CoursesDAO, StudentsDAO}
import javax.inject._
import play.api.mvc._
//import play.api.routing.JavaScriptReverseRouter
//import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class tacosUserOrderController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val title = "User Order TACOS Food"


  /**
    * Call the "tacos_home" html template.
    */
  def tacosUserOrder = Action {
    Ok(views.html.tacos_user_order(title))
  }
}
