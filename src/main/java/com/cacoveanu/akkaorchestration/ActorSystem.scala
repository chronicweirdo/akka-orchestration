package com.cacoveanu.akkaorchestration

import akka.actor.ActorSystem
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class AkkaSystem {

  @Bean
  def createSystem(springExtension: SpringExtension) : ActorSystem = {
    val system = ActorSystem("akka-orchestration-system")
    system
  }

  @Bean(Array("timerActor"))
  def createTimerActor(system: ActorSystem, springExtension: SpringExtension) = {
    system.actorOf(springExtension.props(classOf[TimerActor]))
  }

  @Bean(Array("masterActor"))
  def createMasterActor(system: ActorSystem, springExtension: SpringExtension) = {
    system.actorOf(springExtension.props(classOf[MasterActor]))
  }

  @Bean(Array("dataSourceActor"))
  def createDataSourceActor(system: ActorSystem, springExtension: SpringExtension) = {
    system.actorOf(springExtension.props(classOf[DataSourceActor]))
  }
}
