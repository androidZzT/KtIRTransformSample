package com.zzt.demo.test

import kotlin.time.TimeSource

annotation class Cost

fun main() {
  println(foo())
  println(foo("Transform", "Kotlin IR"))
}

@Cost
fun foo(param1: String? = "Hello", param2: String? = "World"): String {
  println("foo called param1=[$param1], param2=[$param2]")
  return param1 + param2
}