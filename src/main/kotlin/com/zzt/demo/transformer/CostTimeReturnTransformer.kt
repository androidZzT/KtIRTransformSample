package com.zzt.demo.transformer

import com.zzt.demo.utils.costExit
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlock
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irTemporary
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrValueDeclaration
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.util.render

class CostTimeReturnTransformer(
  private val irPluginContext: IrPluginContext,
  private val irFunction: IrFunction,
  private val startTime: IrValueDeclaration
): IrElementTransformerVoidWithContext() {

  override fun visitReturn(expression: IrReturn): IrExpression {
    println("visitReturn:: ${expression.render()}")
    if (expression.returnTargetSymbol != irFunction.symbol) //只 transform 目标函数
      return super.visitReturn(expression)

    println("transform return:: ")
    return DeclarationIrBuilder(irPluginContext, irFunction.symbol).irBlock {
      val result = irTemporary(expression.value) //保存返回表达式
      +costExit(irPluginContext, irFunction, startTime, irGet(result)) // 将统计时间逻辑插到 return 之前
      +expression.apply {
        value = irGet(result) // 将原有的返回表达式补回
      }
    }
  }

}