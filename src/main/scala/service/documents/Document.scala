package service.documents

import spray.json.DefaultJsonProtocol._

/**
  * Created by knut on 3/3/17.
  */
sealed trait Document {

  val title: String
  val author: String
  val watermark: String
  val sep = ":"
  val authorL = "author"
  val titleL = "title"
  val contentL = "content"
  val comma = ","
  val empty = "No watermark for:"

  def noWatermark: Boolean
}

class Journal(val title: String, val author: String) extends Document {

  val content = "journal"
  override val watermark: String =
    if (noWatermark) empty + title
    else
      contentL + sep + content + comma + authorL + sep + author + comma + titleL + sep + title + comma
  override  def noWatermark = title.isEmpty || author.isEmpty
}


class Book(val title: String, val author: String, val topic: String) extends Document {

  val content = "book"
  override val watermark: String =
    if (noWatermark) empty + title
    else
      contentL + sep + content + comma + authorL + sep + author + comma + titleL + sep + title + comma + "topic" + sep + topic
  override  def noWatermark = title.isEmpty || author.isEmpty || topic.isEmpty
}


final case class Ticket(watermark: String)

object Ticket {
  implicit val format = jsonFormat1(Ticket.apply)
}

