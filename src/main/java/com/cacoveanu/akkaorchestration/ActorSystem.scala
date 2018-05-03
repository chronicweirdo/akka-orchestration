package com.cacoveanu.akkaorchestration

import akka.actor.ActorSystem
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class AkkaSystem {

  @Bean
  def createSystem(springExtension: SpringExtension) : ActorSystem = {
    ActorSystem("akka-orchestration-system")
  }

  @Bean(Array("timerActor"))
  def createInjectionActor(system: ActorSystem, springExtension: SpringExtension) = {
    system.actorOf(springExtension.props(classOf[TimerActor]))
  }
}
