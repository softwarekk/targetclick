package com.young.targetclick.base.poet

import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass

/*
 * Des  全域模板处理
 * Author Young
 * Date 
 */
open class AllTargetPoet : OriginalClickPoet {

    constructor() : super()
    private var propertySpecs:ArrayList<PropertySpec> = arrayListOf()


    override fun addAdapterClickFun(funSpec: FunSpec.Builder?) {
        super.addAdapterClickFun(funSpec)
    }

    override fun addFile(fileSpec: FileSpec) {
        super.addFile(fileSpec)
    }
    override fun addPropertySpec(proSpecs: ArrayList<PropertySpec>) {
        //private val clickMil:Long?=null
        proSpecs.add( PropertySpec.builder("clickMil", Long::class)
            .addModifiers(KModifier.PRIVATE)
            .mutable()
            .initializer(CodeBlock.of("0")).build())
        super.addPropertySpec(proSpecs)
    }

    override fun addSuperclass(superclass: KClass<*>) {
        super.addSuperclass(superclass)
    }

    override fun addTargetFun(funSpec: FunSpec.Builder?) {
        funSpec?.addStatement("val currentMil=System.currentTimeMillis()")
               ?.addStatement("     if(clickMil!=0L&&(currentMil-clickMil!!)>1500){" +
                       "\n" +
                    "      clickMil=currentMil\n" +
                    "      return\n" +
                    " }else{\n" +
                    "     clickMil=currentMil\n" +
                    " }")
        super.addTargetFun(funSpec)
    }

    override fun addViewClickFun(funSpec: FunSpec.Builder?) {
        super.addViewClickFun(funSpec)
    }

    override fun outPut(className: String): TypeSpec {
        return super.outPut(className)
    }
}