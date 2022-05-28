package com.zzt.demo.visitor

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor

class StringIndentVisitor: IrElementVisitor<Unit, String> {
  override fun visitElement(element: IrElement, data: String) {
    println("$data${element.render()} {")
    element.acceptChildren(this, "  $data")
    println("$data}")
  }
}