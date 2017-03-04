package service.documents

import com.google.inject.AbstractModule

/**
  * Created by knut on 3/3/17.
  */
object DocumentServiceModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[DocumentRepository]).to(classOf[DocumentRepositoryImpl])
  }
}
