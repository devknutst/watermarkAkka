package service.documents

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, _}

/**
  * Created by knut on 3/4/17.
  */
class DocumentServiceSpec extends WordSpecLike with Matchers with ScalatestRouteTest with SprayJsonSupport {

  "The Service for all documents" should {

    val repository = new DocumentRepositoryImpl()

    "return a list of documents" in {
      Get("/documents") ~> new DocumentService(repository).docRoutes ~> check {
        status shouldEqual StatusCodes.OK
        response.toString() should include("How to make money")
      }
    }
  }

  "The Service for a single document" should {

    val repository = new DocumentRepositoryImpl()

    "return a list of documents" in {
      Get("/document/Dark") ~> new DocumentService(repository).docRoutes ~> check {
        status shouldEqual StatusCodes.OK
        response.toString() should include("The Dark Code")
      }
    }
  }

  "The Service for all documents with no watermark" should {

    val repository = new DocumentRepositoryImpl()

    "return a list of documents" in {
      Get("/nowatermarks") ~> new DocumentService(repository).docRoutes ~> check {
        status shouldEqual StatusCodes.OK
        val text = response.toString()
        text should include("test book")
        text should include("test journal")
        text should include("No watermark")
      }
    }
  }

  "The Service for a single document with no watermark" should {

    val repository = new DocumentRepositoryImpl()

    "return a list of documents" in {
      Get("/nowatermark/journal") ~> new DocumentService(repository).docRoutes ~> check {
        status shouldEqual StatusCodes.OK
        val text = response.toString()
        text should include("test journal")
        text should include("No watermark")
      }
    }
  }

}
