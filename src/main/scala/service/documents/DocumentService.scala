package service.documents

import javax.inject.{Inject, Named}

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol


/**
  * Created by knut on 3/3/17.
  */

@Named
class DocumentService @Inject()(documentRepository: DocumentRepository) extends DefaultJsonProtocol with SprayJsonSupport {

  val docRoutes =
    path("documents") {
      get {
        complete(documentRepository.getDocuments)
      }
    } ~
      path("document"/ """\w+""".r  ) {
        r => complete(documentRepository.getDocument(r))
      } ~
      path("nowatermarks") {
        complete(documentRepository.getNoWatermarks)
      } ~
      path("nowatermark"/"""\w+""".r) {
        r => complete(documentRepository.getNoWatermark(r))
      }

}



