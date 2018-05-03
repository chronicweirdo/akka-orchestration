package com.cacoveanu.akkaorchestration

import akka.actor.{Actor, Extension, Props}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

import scala.beans.BeanProperty

@Component
class SpringExtension extends Extension {

  @Autowired @BeanProperty val applicationContext: ApplicationContext = null

  def props(actorBeanName: String): Props =
    Props.create(classOf[SpringActorProducer], applicationContext, actorBeanName)

  def props(actorClass: Class[_ <: Actor]): Props =
    Props.create(classOf[SpringActorClassProducer], applicationContext, actorClass)
}
