package com.cacoveanu.akkaorchestration

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Configuration

object AkkaOrchestrationApp {

  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[Configuration])
  }
}
