package com.zzt.demo.visitor

import org.jetbrains.kotlin.backend.common.serialization.proto.IrDeclaration
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor

class RootParentVisitor: IrElementVisitor<IrDeclaration, Nothing?> {

  override fun visitElement(element: IrElement, data: Nothing?): IrDeclaration {
    return element.accept(this, null)
  }
}