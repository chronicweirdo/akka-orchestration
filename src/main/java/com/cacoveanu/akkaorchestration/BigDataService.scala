package com.cacoveanu.akkaorchestration

import org.springframework.stereotype.Service

import scala.util.Random

case class Record(value: Double)

@Service
class BigDataService {

  def execute: List[_] =
    if (Random.nextBoolean())
      return (1 to 1000000).map(i => Record(Random.nextDouble())).toList
    else
      throw new Exception("data access failure")
}
