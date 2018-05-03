package com.cacoveanu.akkaorchestration

import akka.actor.{Actor, IndirectActorProducer}
import org.springframework.context.ApplicationContext

class SpringActorClassProducer(private val applicationContext: ApplicationContext,
                               private val cls: Class[_ <: Actor]) extends IndirectActorProducer {

  override def produce(): Actor = applicationContext.getBean(cls)

  override def actorClass: Class[_ <: Actor] = classOf[Actor]
}

