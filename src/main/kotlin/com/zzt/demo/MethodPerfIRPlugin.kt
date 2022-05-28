package com.zzt.demo

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

/**
 * 统计方法耗时的 IR 插件
 */
class MethodPerfIRPlugin: KotlinCompilerPluginSupportPlugin {

  lateinit var project: Project
  lateinit var pluginId: String
  lateinit var group: String
  lateinit var artifactId: String
  lateinit var version: String

  override fun apply(target: Project) {
    super.apply(target)
    project = target
    pluginId = target.extensions.getByName("kotlin_plugin_id") as String
    group = target.extensions.getByName("group") as String
    artifactId = target.extensions.getByName("artifactId") as String
    version = target.extensions.getByName("version") as String
  }

  override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
    val project = kotlinCompilation.target.project
    val extension = project.extensions.getByType(MethodPerfExtension::class.java) as MethodPerfExtension
    return project.provider {
      listOf(
        SubpluginOption(key = "string", value = extension.string),
        SubpluginOption(key = "file", value = extension.file),
      )
    }
  }

  override fun getCompilerPluginId(): String {
    return pluginId
  }

  override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
    group,
    artifactId,
    version
  )


  override fun getPluginArtifactForNative(): SubpluginArtifact = SubpluginArtifact(
    group,
    "$artifactId-native",
    version
  )


  /**
   * [isApplicable] is checked against compilations of the project, and if it returns true,
   * then [applyToCompilation] may be called later.
   */
  override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = project.plugins.hasPlugin(MethodPerfIRPlugin::class.java)
}