package com.zzt.demo.visitor

import com.zzt.demo.annotation.Cost
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor

class CollectAnnotatedMethodVisitor(
  private val annotatedList: MutableList<IrSimpleFunction>
): IrElementVisitor<Unit, Nothing?> {

  override fun visitModuleFragment(declaration: IrModuleFragment, data: Nothing?) {
    println("visitModuleFragment ${declaration.render()}")
    declaration.acceptChildren(this, data)
  }

  override fun visitFile(declaration: IrFile, data: Nothing?) {
    println("visitFile ${declaration.render()}")
    declaration.acceptChildren(this, data)
  }

  override fun visitSimpleFunction(func: IrSimpleFunction, data: Nothing?) {
    println("visitSimpleFunction ${func.render()}")
    func.annotations.forEach {
      if (Cost::class.qualifiedName.equals(it.type.render())) {
        annotatedList.add(func)
      }
    }
  }

  override fun visitElement(element: IrElement, data: Nothing?) {
    println("visitElement ${element.render()}")
  }
}