package com.cacoveanu.akkaorchestration

import akka.actor.{Actor, IndirectActorProducer}
import org.springframework.context.ApplicationContext

class SpringActorProducer(private val applicationContext: ApplicationContext,
                          private val actorBeanName: String) extends IndirectActorProducer {

  override def produce(): Actor = applicationContext.getBean(actorBeanName).asInstanceOf[Actor]

  override def actorClass: Class[_ <: Actor] = classOf[Actor]
}

