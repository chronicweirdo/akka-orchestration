package com.cacoveanu.akkaorchestration

import akka.actor.{Actor, ActorRef, PoisonPill}
import com.cacoveanu.akkaorchestration.DataSourceActor.{Load, LoadResult}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import scala.concurrent.duration._

import scala.beans.BeanProperty

object WorkerActor {
  case class Process(id: String)
  case class ProcessResult(success: Boolean)
  case object Timeout
}

@Component("workerActorPrototype")
@Scope("prototype")
class WorkerActor extends Actor {
  import WorkerActor._
  import context._

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
    case Process(id) =>
      logger.info(s"starting work for $id (${context.dispatcher})")
      this.id = id
      this.initiator = sender()
      dataSourceActor ! Load(id)
      context.system.scheduler.scheduleOnce(30 seconds, self, Timeout)


    case LoadResult(data) =>
      logger.info(s"received data for $id")
      // processing this data somehow
      slowService.execute()
      initiator ! ProcessResult(true)
      self ! PoisonPill

    case Timeout =>
      logger.info("waiting for data timed out, stopping worker")
      initiator ! ProcessResult(false)
      self ! PoisonPill
  }
}
