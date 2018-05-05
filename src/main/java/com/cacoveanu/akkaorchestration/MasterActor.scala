package com.cacoveanu.akkaorchestration

import java.util.concurrent.Executors

import akka.actor.{Actor, ActorRef, PoisonPill}
import com.cacoveanu.akkaorchestration.MasterActor._
import com.cacoveanu.akkaorchestration.WorkerActor.{Process, ProcessResult}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.beans.BeanProperty

object MasterActor {

  case object StartWork

  case object Check

  case class CheckResult(@BeanProperty running: Int)

  case class StartWorkers(ids: Iterable[Int])

  case class StartedWorker(id: Int)

}

@Component("masterActorPrototype")
@Scope("prototype")
class MasterActor extends Actor {

  val logger = LoggerFactory.getLogger(classOf[MasterActor])

  @Autowired
  @BeanProperty
  val springExtension: SpringExtension = null

  def getWorkerStarter = context.actorOf(springExtension.props(classOf[WorkerStarterActor])
    .withDispatcher("high-load-dispatcher"))

  var running = 0

  override def receive: Receive = {
    case StartWork =>
      getWorkerStarter ! StartWorkers(1 to 100000)
      logger.info("done start work")
    case StartedWorker(_) =>
      running += 1
    case ProcessResult(_) =>
      logger.info("finished work")
      running -= 1
    case Check =>
      logger.info("checking running tasks")
      sender() ! CheckResult(running)
  }
}

@Component("workerStarterActorPrototype")
@Scope("prototype")
class WorkerStarterActor extends Actor {

  @Autowired
  @BeanProperty
  val springExtension: SpringExtension = null

  def getWorker = context.actorOf(springExtension.props(classOf[WorkerActor])
    .withDispatcher("high-load-dispatcher"))

  override def receive: Receive = {
    case StartWorkers(ids) =>
      for (id <- ids) {
        getWorker ! Process(id.toString, sender())
        sender() ! StartedWorker(id)
      }
      self ! PoisonPill
  }
}