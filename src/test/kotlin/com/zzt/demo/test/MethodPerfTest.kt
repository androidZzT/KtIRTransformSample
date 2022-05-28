package com.zzt.demo.test

import com.zzt.demo.MethodPerfComponentRegistrar
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class MethodPerfTest {
  @Test
  fun `IR plugin success`() {
//    val result = compile(
//      sourceFile = SourceFile.fromPath(File("/Users/zhangzhengtian/Github/KtIRPlugin/src/test/kotlin/com/sankuai/demo/test/main.kt"))
//    )
    val result = compile(
      sourceFile = SourceFile.kotlin("main.kt", """ 
        import com.zzt.demo.annotation.Cost       

        fun main() {
          println(foo())
          println(foo("Transform", "Kotlin IR"))
        }

        @Cost
        fun foo(param1: String? = "Hello", param2: String? = "World"): String {
          println("foo called param1=[${'$'}param1], param2=[${'$'}param2]") //注意 $ 需要转义
          return param1 + param2
        }
      """.trimIndent())
    )
    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

    val ktClazz = result.classLoader.loadClass("MainKt")
    val main = ktClazz.declaredMethods.single { it.name == "main" && it.parameterCount == 0 }
    main.invoke(null)
  }

  fun compile(
    sourceFiles: List<SourceFile>,
    plugin: ComponentRegistrar = MethodPerfComponentRegistrar(),
  ): KotlinCompilation.Result {
    return KotlinCompilation().apply {
      sources = sourceFiles
      useIR = true
      compilerPlugins = listOf(plugin)
      inheritClassPath = true
    }.compile()
  }

  fun compile(
    sourceFile: SourceFile,
    plugin: ComponentRegistrar = MethodPerfComponentRegistrar(),
  ): KotlinCompilation.Result {
    return compile(listOf(sourceFile), plugin)
  }
}