import akka.actor.ActorSystem
import akka.event.Logging
import akka.event.Logging.InfoLevel
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.google.inject.Guice
import service.documents.{DocumentService, DocumentServiceModule}
import service.health._

object Main extends App with HealthRoutes {
  
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher 

  val settings = Settings(system)

  val logger = Logging(system, getClass)

  private val injector = Guice.createInjector(DocumentServiceModule)
  private val docService = injector.getInstance(classOf[DocumentService])
  
  val routes = logRequestResult("", InfoLevel)(docService.docRoutes ~ healthRoutes)

  Http().bindAndHandle(routes, settings.Http.interface, settings.Http.port) map { binding =>
    logger.info(s"Server started on port {}", binding.localAddress.getPort)
  } recoverWith { case _ => system.terminate() }
}
