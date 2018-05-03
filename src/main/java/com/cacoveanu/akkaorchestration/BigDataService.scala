package com.cacoveanu.akkaorchestration

import org.springframework.stereotype.Service

@Service
class BigDataService {

  def execute =
    (1 to 10000).map(i => s"data $i").toList
}
