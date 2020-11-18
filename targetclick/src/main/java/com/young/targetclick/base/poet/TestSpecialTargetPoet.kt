package com.young.targetclick.base.poet

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlin.reflect.KClass

/*
 * Des  自定义局域模板
 * Author Young
 * Date
 */
class TestSpecialTargetPoet: AllTargetPoet {
    constructor() : super()
    private var propertySpecs:ArrayList<PropertySpec> = arrayListOf()

    override fun addAdapterClickFun(funSpec: FunSpec.Builder?) {
        super.addAdapterClickFun(funSpec)
    }

    override fun addFile(fileSpec: FileSpec) {
        super.addFile(fileSpec)
    }

    override fun addPropertySpec(proSpecs: ArrayList<PropertySpec>) {
        super.addPropertySpec(proSpecs)
    }

    override fun addSuperclass(superclass: KClass<*>) {
        super.addSuperclass(superclass)
    }

    override fun addTargetFun(funSpec: FunSpec.Builder?) {
        super.addTargetFun(funSpec)
    }

    override fun addViewClickFun(funSpec: FunSpec.Builder?) {
        super.addViewClickFun(funSpec)
    }

    override fun outPut(className: String): TypeSpec {
        addTargetFun(FunSpec.builder("toTarget"))
        return super.outPut(className)
    }
}