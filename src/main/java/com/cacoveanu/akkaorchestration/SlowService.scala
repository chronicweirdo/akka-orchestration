package com.cacoveanu.akkaorchestration

import org.springframework.stereotype.Service

import scala.util.Random

@Service
class SlowService {

  def execute() = {
    if (Random.nextBoolean()) throw new Exception("processing exception")
    Thread.sleep(10000)
    "result"
  }
}
