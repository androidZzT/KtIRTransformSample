package com.zzt.demo

import com.zzt.demo.transformer.MethodCostTimeTransformer
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump

class MethodPerfExtension(
  val string: String,
  val file: String
): IrGenerationExtension {

  override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
    println("generate:: string: $string file: $file")

    println("------ before transform dump IR -------")
    println(moduleFragment.dump())

    println("------  Transforming... -------")
    moduleFragment.transform(MethodCostTimeTransformer(pluginContext), null)

    println("------ after transform dump IR -------")
    println(moduleFragment.dump())

//    println("------  recursive visit IR -------")
//    moduleFragment.accept(RecursiveVisitor(), null)

//    println("------  string indent visit IR -------")
//    moduleFragment.accept(StringIndentVisitor(), "data")

//    println("------  get println func IrSymbolFunction -------")
//    val funPrintln = pluginContext.referenceFunctions(FqName("kotlin.io.println")).single {
//      val parameters = it.owner.valueParameters
//      parameters.size == 1 && parameters[0].type == pluginContext.irBuiltIns.anyNType
//    }
//    println("$funPrintln")
//
//    println("------  build main function -------")
//    val funMain = pluginContext.irFactory.buildFun {
//      name = Name.identifier("main")
//      visibility = DescriptorVisibilities.PUBLIC
//      modality = Modality.FINAL
//      returnType = pluginContext.irBuiltIns.unitType
//    }.also { function ->
//      println("------  add body for main function -------")
//      function.body = DeclarationIrBuilder(pluginContext, function.symbol).irBlockBody {
//        val callPrintln = irCall(funPrintln)
//        callPrintln.putValueArgument(0, irString("Hello, World!"))
//        +callPrintln
//      }
//    }
//    println(funMain.dump())


  }
}