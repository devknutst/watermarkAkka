package service.documents

import scala.concurrent.Future

/**
  * Created by knut on 3/3/17.
  */
trait DocumentRepository {
  def getDocuments: Future[List[Ticket]]
  def getDocument(search: String): Future[List[Ticket]]
  def getNoWatermarks: Future[List[Ticket]]
  def getNoWatermark(search: String): Future[List[Ticket]]
}

final class DocumentRepositoryImpl extends DocumentRepository {

  private val documents = List(
    new Book("The Dark Code", "Bruce Wayne", "Science"),
    new Book("How to make money", "Dr. Evil", "Business"),
    new Book("How to make more money", "Dr. very Evil", "Business"),
    new Journal("Journal of human flight routes", "Clark Kent"),
    new Journal("Why can't all bats fly", "Bruce Wayne"),
//    two examples of documents without watermark
    new Journal("test journal", ""),
    new Book("test book", "", "")
  )

  val syncSet = scala.collection.mutable.LinkedHashSet.synchronized(documents)

  def getTickets(docs: List[Document]):List[Ticket] = {
    val t = docs.foldRight(List[Ticket]())((a,b) => Ticket(a.watermark)::b)

    val n = docs.grouped(5)

    def test(l: List[Document], acc: List[List[Document]], count: Int, temp: List[Document]): List[List[Document]] = l match {
      case Nil => acc
      case h :: t => if (count==0) test(t, temp::acc, 5, List()) else test(t, acc, count-1, h::temp)
    }

    n.flatMap(t => t.foldRight(List[Ticket]())((a,b) => Ticket(a.watermark)::b)).toList
  }

  def getDocuments: Future[List[Ticket]] = Future.successful(getTickets(documents))

  def getDocument(search: String): Future[List[Ticket]] = {
    val t = documents.filter(d => d.title.contains(search))

    Future.successful(getTickets(t))
  }

  def getNoWatermarks: Future[List[Ticket]] = {
    val t = documents.filter(d => d.noWatermark)
    Future.successful(getTickets(t))
  }

  def getNoWatermark(search: String): Future[List[Ticket]] = {
    val t = documents.filter(d => d.noWatermark && d.title.contains(search))
    Future.successful(getTickets(t))
  }


}

