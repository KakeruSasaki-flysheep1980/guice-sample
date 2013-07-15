package controllers

import play.api._
import play.api.mvc._
import com.google.inject.Singleton
import com.google.inject.Inject
import services.HogeService

@Singleton  // これが無いとControllerのインスタンスが1つではなくなる
class Application @Inject()(hogeService: HogeService) extends Controller {

  def index = Action { request =>
    hogeService.hoge(2)
    Ok(views.html.index(hogeService.hoge(1)))
  }
  
}