package com.cacoveanu.akkaorchestration

import akka.actor.Actor
import com.cacoveanu.akkaorchestration.MasterActor.{Check, CheckResult, StartWork}
import com.cacoveanu.akkaorchestration.WorkerActor.{Process, ProcessResult}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.beans.BeanProperty

object MasterActor {
  case object StartWork
  case object Check
  case class CheckResult(@BeanProperty running: Int)
}

@Component("masterActorPrototype")
@Scope("prototype")
class MasterActor extends Actor {

  @Autowired
  @BeanProperty
  val springExtension: SpringExtension = null

  def getWorker = context.actorOf(springExtension.props(classOf[WorkerActor]))

  var running = 0

  override def receive: Receive = {
    case StartWork =>
      (1 to 100000).foreach(id => {
        getWorker ! Process(id.toString)
        running += 1
      })
    case ProcessResult(_) => running -= 1
    case Check => sender() ! CheckResult(running)
  }
}
