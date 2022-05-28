package com.zzt.demo.visitor

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor

class RecursiveVisitor: IrElementVisitor<Unit, Nothing?> {
  override fun visitElement(element: IrElement, data: Nothing?) {
    println("visitElement:: ${element.render()}")
    element.acceptChildren(this, data)
  }
}