package com.cacoveanu.akkaorchestration

import akka.actor.Actor
import com.cacoveanu.akkaorchestration.DataSourceActor.{Load, LoadResult}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.beans.BeanProperty

object DataSourceActor {
  case class Load(id: String)
  case class LoadResult(data: List[_])
}

@Component("dataSourceActorPrototype")
@Scope("prototype")
class DataSourceActor extends Actor {

  val logger = LoggerFactory.getLogger(classOf[DataSourceActor])

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    logger.warn(s"restarting data source actor because: ${reason.getMessage}")
  }

  @Autowired
  @BeanProperty
  val bigDataService: BigDataService = null

  override def receive: Receive = {
    case Load(_) => sender() ! LoadResult(bigDataService.execute)
  }
}
