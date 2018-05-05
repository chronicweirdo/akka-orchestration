package com.cacoveanu.akkaorchestration

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class AkkaSystem {

  @Bean
  def createSystem(springExtension: SpringExtension): ActorSystem = {
    val customConf = ConfigFactory.parseString(
      """
        high-load-dispatcher {
          type = Dispatcher
          executor = "thread-pool-executor"
          thread-pool-executor {
            fixed-pool-size = 4
          }
          throughput = 1
        }
      """)

    ActorSystem("akka-orchestration-system", ConfigFactory.load(customConf))
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
