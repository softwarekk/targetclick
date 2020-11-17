package com.young.targetclick

import com.squareup.kotlinpoet.*
import kotlin.reflect.KClass

/*
 * Des  api 描述 BaseClickPoet
 * Author Young
 * Date 2020/11/15
 */
open class OriginalClickPoet: BaseClickPoet {
    constructor() : super(){
        initOriginal()
    }
    private fun initOriginal() {
        superclass= BaseXmlBindingClick::class
        implCallback= PropertySpec.builder("implCallback", BindingClickImplCallback::class)
            .addModifiers(KModifier.PRIVATE)
            .initializer("callBack")
        propertySpecs.add(implCallback?.build()!!)
        toTargetFun=FunSpec.builder("toTarget")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("view",Any::class)
            .addParameter("index",Int::class.asTypeName().copy(true))
            .addParameter("data",Any::class.asTypeName().copy(true))
            .addStatement("super.toTarget(view, index,data)")
            .addStatement("implCallback.viewOnclick(view,index,data)")
            .build()
        viewClickFun =  FunSpec.builder("viewClick")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("view",Any::class)
            .addStatement("super.viewClick(view)")
            .addStatement("%N(view,null,null)",toTargetFun!!)
            .build()
        adapterClickFun= FunSpec.builder("adapterClick")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("view",Any::class)
            .addParameter("index",Int::class.asTypeName().copy(true))
            .addStatement("super.adapterClick(view, index)")
            .addStatement("%N(view,index,null)",toTargetFun!!)
            .build()
    }

    private var superclass: KClass<*>?=null
    private var viewClickFun: FunSpec?= null
    private var adapterClickFun: FunSpec?=null
    private var toTargetFun: FunSpec?=null
    private var implCallback:PropertySpec.Builder ?=null
    private var propertySpec:PropertySpec.Builder ?=null
    private var propertySpecs:ArrayList<PropertySpec> = arrayListOf()

    override fun addFile(fileSpec: FileSpec) {

    }

    override fun addSuperclass(superclass: KClass<*>) {
        this.superclass=superclass
    }

    override fun addViewClickFun(funSpec: FunSpec.Builder?) {
        viewClickFun =  funSpec
            ?.addStatement(if(superclass?.isInstance(BaseXmlBindingClick::class)!! ) "super.viewClick(view)" else "super.viewClick(view,index,data)")
            ?.addStatement(if(superclass?.isInstance(BaseXmlBindingClick::class)!! ) "%N(view,null,null)" else "%N(view,index,data)",toTargetFun!!)
            ?.addModifiers(KModifier.OVERRIDE)
            ?.addParameter("view",Any::class)
            ?.build()
    }

    override fun addAdapterClickFun(funSpec: FunSpec.Builder?) {
        adapterClickFun= funSpec
            ?.addStatement(if(superclass?.isInstance(BaseXmlBindingClick::class)!! ) "super.adapterClick(view, index)" else "super.viewClick(view,index,data)")
            ?.addStatement(if(superclass?.isInstance(BaseXmlBindingClick::class)!! ) "%N(view,null,null)" else "%N(view,index,data)",toTargetFun!!)
            ?.addModifiers(KModifier.OVERRIDE)
            ?.addParameter("view",Any::class)
            ?.addParameter("index",Int::class.asTypeName().copy(true))
            ?.build()
    }

    override fun addTargetFun(funSpec: FunSpec.Builder?) {
        toTargetFun=funSpec
            ?.addModifiers(KModifier.OVERRIDE)
            ?.addParameter("view",Any::class)
            ?.addParameter("index",Int::class.asTypeName().copy(true))
            ?.addParameter("data",Any::class.asTypeName().copy(true))
            ?.addStatement("super.toTarget(view, index,data)")
            ?.addStatement("implCallback.viewOnclick(view,index,data)")
            ?.build()
    }

    override fun addPropertySpec(funSpec: PropertySpec.Builder?) {
        propertySpecs.add(funSpec?.build()!!)
    }

    override fun outPut(className: String): TypeSpec {
        return TypeSpec.classBuilder(className + "BindingClickImpl") //class 构造器
                .superclass(superclass!!)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("callBack", BindingClickImplCallback::class)
                        .build()
                )
                .addProperties(propertySpecs.asIterable()!!)
                .addFunctions(
                    listOf(viewClickFun!!,adapterClickFun!!,toTargetFun!!)
                ).build()
    }
}