package com.cacoveanu.akkaorchestration

import akka.actor.ActorRef
import com.cacoveanu.akkaorchestration.MasterActor.{Check, CheckResult, StartWork}
import org.springframework.beans.factory.annotation.{Autowired, Qualifier}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}
import akka.pattern.ask
import akka.util.Timeout

import scala.beans.BeanProperty
import scala.concurrent.Await
import scala.concurrent.duration._

@Controller
class MonitoringController {

  @Autowired
  @Qualifier("masterActor")
  @BeanProperty
  val masterActor: ActorRef = null

  @RequestMapping(Array("/test"))
  @ResponseBody
  def test() = {
    "akka orchestration app"
  }

  @RequestMapping(Array("/start"))
  @ResponseBody
  def start() = {
    masterActor ! StartWork
    "started"
  }

  @RequestMapping(Array("/check"))
  @ResponseBody
  def check() = {
    implicit val timeout = Timeout(1 second)
    val future = masterActor ? Check
    Await.result(future, timeout.duration).asInstanceOf[CheckResult]
  }
}
