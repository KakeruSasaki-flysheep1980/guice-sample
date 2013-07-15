import com.google.inject.matcher.Matchers
import com.google.inject.{AbstractModule, Guice, Singleton}
import com.hp.cache4guice.adapters.ehcache.EhCacheModule
import net.codingwell.scalaguice.ScalaModule
import org.aopalliance.intercept.{MethodInvocation, MethodInterceptor}
import play.api.GlobalSettings
import services.HogeService

object Global extends GlobalSettings {

  class MyModule extends AbstractModule with ScalaModule {
    def configure() {
      bind[HogeService].in[Singleton]
    }
  }

  private lazy val injector = {
    // onStartでやればいいね
    Guice.createInjector(new MyModule, new EhCacheModule) // cache方法を指定
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    injector.getInstance(controllerClass)
  }
}
