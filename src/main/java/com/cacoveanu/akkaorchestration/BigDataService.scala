package com.cacoveanu.akkaorchestration

import org.springframework.stereotype.Service

import scala.util.Random

@Service
class BigDataService {

  def execute =
    (1 to 1000000).map(i => s"data ${Random.nextDouble()}").toList
}
