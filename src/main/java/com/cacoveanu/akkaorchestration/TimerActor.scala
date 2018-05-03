package com.cacoveanu.akkaorchestration

import akka.actor.Actor
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component


import scala.concurrent.duration._

@Component("timerActorPrototype")
@Scope("prototype")
class TimerActor extends Actor {
  import context._

  val logger = LoggerFactory.getLogger(classOf[TimerActor])
  val intervalInSeconds = 3

  override def preStart(): Unit = {
    super.preStart()
    logger.info("starting timer actor")
    context.system.scheduler.scheduleOnce(50 milliseconds, self, "start")
    //self ! "start"
  }

  override def receive: Receive = {
    case "start" =>
      logger.info("starting timer")
      context.system.scheduler.scheduleOnce(intervalInSeconds seconds, self, "execute")
    case "execute" =>
      logger.info("executing timed task")
      context.system.scheduler.scheduleOnce(intervalInSeconds seconds, self, "execute")
  }
}
