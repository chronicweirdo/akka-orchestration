package com.cacoveanu.akkaorchestration

import org.springframework.stereotype.Service

@Service
class SlowService {

  def execute() = {
    Thread.sleep(10000)
    "result"
  }
}
