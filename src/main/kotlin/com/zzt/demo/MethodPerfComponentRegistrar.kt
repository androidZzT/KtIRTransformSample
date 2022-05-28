package com.zzt.demo

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(ComponentRegistrar::class) // don't forget it
class MethodPerfComponentRegistrar: ComponentRegistrar {

  override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
    println("registerProjectComponents:: ${project.name}")
    val string = configuration.get(MethodPerfCommandLineProcessor.ARG_STRING, "String")
    val file = configuration.get(MethodPerfCommandLineProcessor.ARG_FILE, "File")
    IrGenerationExtension.registerExtension(project, MethodPerfExtension(string, file))
  }

}