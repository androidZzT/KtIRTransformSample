package com.zzt.demo.transformer

import com.zzt.demo.utils.*
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.FqName

class MethodCostTimeTransformer(
  private val pluginContext: IrPluginContext
): IrElementTransformerVoidWithContext() {

  override fun visitFunctionNew(declaration: IrFunction): IrStatement {
    println("visitFunctionNew:: ${declaration.render()}")
    val body = declaration.body
    val annotationClass = pluginContext.referenceClass(FqName("com.zzt.demo.annotation.Cost"))!!
    if (body != null && declaration.hasAnnotation(annotationClass)) { //有方法体&被注解标注的方法
      declaration.body = irCost(declaration, body) //transform 方法体
    }
    return super.visitFunctionNew(declaration)
  }

  private fun irCost(
    irFunction: IrFunction,
    irBody: IrBody): IrBlockBody {
    println("irCost:: ")
    return DeclarationIrBuilder(pluginContext, irFunction.symbol).irBlockBody {
      +costEnter(pluginContext, irFunction) //打印目标函数信息

      val startTime = irTemporary(irCall(pluginContext.markNowFunc()).also {
        it.dispatchReceiver = irGetObject(pluginContext.monotonicClass())
      })

      +irBlock(resultType = irFunction.returnType) {
        for(statement in irBody.statements) { //原有方法体中的表达式
          +statement
        }
        if (irFunction.returnType == pluginContext.irBuiltIns.unitType) {
          +costExit(pluginContext, irFunction, startTime)
        }
      }.transform(CostTimeReturnTransformer(pluginContext, irFunction, startTime), null)
    }
  }
}