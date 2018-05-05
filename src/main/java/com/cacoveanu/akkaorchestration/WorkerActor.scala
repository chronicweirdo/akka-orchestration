package com.cacoveanu.akkaorchestration

import akka.actor.{Actor, ActorRef, PoisonPill}
import com.cacoveanu.akkaorchestration.DataSourceActor.{Load, LoadResult}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.beans.BeanProperty
import scala.concurrent.Await

object WorkerActor {
  case class Process(id: String, listener: ActorRef)
  case class ProcessSynch(id: String, listener: ActorRef)
  case class ProcessResult(success: Boolean)
}

@Component("workerActorPrototype")
@Scope("prototype")
class WorkerActor extends Actor {
  import WorkerActor._

  val logger = LoggerFactory.getLogger(classOf[WorkerActor])

  @Autowired
  @Qualifier("dataSourceActor")
  @BeanProperty
  val dataSourceActor: ActorRef = null

  @Autowired
  @BeanProperty
  val slowService: SlowService = null

  var initiator: ActorRef = _
  var id: String = _

  override def receive: Receive = {
    case ProcessSynch(id, listener) =>
      logger.info(s"starting synchronous work for $id (${context.dispatcher})")
      this.id = id
      this.initiator = listener
      implicit val timeout = Timeout(30 second)
      val future = dataSourceActor ? Load(id)
      val result = Await.result(future, timeout.duration).asInstanceOf[LoadResult]
      logger.info(s"received synchronous data for $id of size ${result.data.size}")
      // processing this data somehow
      slowService.execute()
      initiator ! ProcessResult(true)
      self ! PoisonPill

    /*case Process(id, listener) => {
      logger.info(s"starting work for $id (${context.dispatcher})")
      this.id = id
      this.initiator = listener
      dataSourceActor ! Load(id)
    }
    case LoadResult(data) => {
      logger.info(s"received data for $id")
      // processing this data somehow
      slowService.execute()
      initiator ! ProcessResult(true)
      self ! PoisonPill
    }*/
  }
}
