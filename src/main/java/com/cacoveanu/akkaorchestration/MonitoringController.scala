package com.cacoveanu.akkaorchestration

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}

@Controller
class MonitoringController {

  @RequestMapping(Array("/test"))
  @ResponseBody
  def test() = {
    "akka orchestration app"
  }
}
