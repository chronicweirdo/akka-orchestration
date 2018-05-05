package com.cacoveanu.akkaorchestration

import akka.actor.{Actor, PoisonPill}
import com.cacoveanu.akkaorchestration.MasterActor._
import com.cacoveanu.akkaorchestration.WorkerActor.{Process, ProcessResult, Timeout}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.beans.BeanProperty
import scala.collection.mutable
import scala.concurrent.duration._

object MasterActor {

  case object StartWork

  case object Check

  case class CheckResult(@BeanProperty running: Int, @BeanProperty queued: Int)

  case class StartWorkers(ids: Iterable[Int])

  case class StartedWorker(id: Int)

  case class SanityCheck(lastProgress: CheckResult)

}

@Component("masterActorPrototype")
@Scope("prototype")
class MasterActor extends Actor {
  import context._

  val logger = LoggerFactory.getLogger(classOf[MasterActor])

  @Autowired
  @BeanProperty
  val springExtension: SpringExtension = null

  def getWorker = context.actorOf(springExtension.props(classOf[WorkerActor])
    .withDispatcher("high-load-dispatcher"))

  var running = 0

  val messages = mutable.Queue.empty[Process]

  val maximumConcurrentWorkers = Runtime.getRuntime.availableProcessors() / 2

  def execute() = {
    while (running < maximumConcurrentWorkers && messages.nonEmpty) {
      getWorker ! messages.dequeue()
      running += 1
    }
  }

  override def receive: Receive = {
    case StartWork =>
      context.system.scheduler.scheduleOnce(2 minutes, self, SanityCheck(CheckResult(running, messages.size)))
      for (id <- 1 to 100000) {
        messages.enqueue(Process(id.toString))
      }
      execute()
      logger.info("done start work")
    case ProcessResult(_) =>
      logger.info("finished work")
      running -= 1
      execute()
    case Check =>
      logger.info("checking running tasks")
      sender() ! CheckResult(running, messages.size)
    case SanityCheck(CheckResult(lastRunning, lastEnqueued)) =>
      if (lastRunning == running && lastEnqueued == messages.size) {
        logger.warn("no progress being made")
        throw new Exception("no progress being made")
      } else {
        context.system.scheduler.scheduleOnce(2 minutes, self, SanityCheck(CheckResult(running, messages.size)))
      }
  }
}