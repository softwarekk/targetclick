package com.young.targetclick.base.poet

import com.squareup.kotlinpoet.*
import com.young.targetclick.base.BaseXmlTargetClick
import com.young.targetclick.base.TargetClickImplCallback
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
        superclass= BaseXmlTargetClick::class
        implCallback= PropertySpec.builder("implCallback", TargetClickImplCallback::class)
            .addModifiers(KModifier.PRIVATE)
            .initializer("callBack")
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
    private var viewSpecs:ArrayList<ParameterSpec> = arrayListOf()
    private var adapterSpecs:ArrayList<ParameterSpec> = arrayListOf()

    private fun createParameterSpec(){
        viewSpecs.clear()
        adapterSpecs.clear()
        val oneParam=ParameterSpec.builder("view",Any::class).build()
        val twoParam=ParameterSpec.builder("index",Int::class).build()
        val threeParam=ParameterSpec.builder("data",Any::class).build()
        if(superclass==BaseXmlTargetClick::class){
            viewSpecs.add( oneParam )
            adapterSpecs.add( oneParam )
            adapterSpecs.add( twoParam  )
        }else if(superclass==BaseXmlTargetClick::class){
            viewSpecs.add( oneParam )
            viewSpecs.add( twoParam )
            viewSpecs.add( threeParam )
            adapterSpecs.add( oneParam )
            adapterSpecs.add( twoParam  )
            adapterSpecs.add( threeParam  )
        }
    }

    override fun addFile(fileSpec: FileSpec) {

    }

    override fun addSuperclass(superclass: KClass<*>) {
        this.superclass=superclass
    }

    override fun addViewClickFun(funSpec: FunSpec.Builder?) {
        viewClickFun =  funSpec
            ?.addParameters(viewSpecs)
            ?.addStatement(if(superclass== BaseXmlTargetClick::class ) "super.viewClick(view)" else "super.viewClick(view,index,data)")
            ?.addStatement(if(superclass== BaseXmlTargetClick::class) "%N(view,null,null)" else "%N(view,index,data)",toTargetFun!!)
            ?.addModifiers(KModifier.OVERRIDE)
            ?.addParameter("view",Any::class)
            ?.build()
    }

    override fun addAdapterClickFun(funSpec: FunSpec.Builder?) {
        adapterClickFun= funSpec
            ?.addParameters(adapterSpecs)
            ?.addStatement(if(superclass== BaseXmlTargetClick::class) "super.adapterClick(view, index)" else "super.viewClick(view,index,data)")
            ?.addStatement(if(superclass== BaseXmlTargetClick::class ) "%N(view,index,null)" else "%N(view,index,data)",toTargetFun!!)
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

    override fun addPropertySpec( proSpecs: ArrayList<PropertySpec>) {
        proSpecs.add(implCallback?.build()!!)
        propertySpecs=proSpecs
    }

    override fun outPut(className: String): TypeSpec {
        addPropertySpec(propertySpecs)
        addTargetFun(FunSpec.builder("toTarget"))
        addViewClickFun(FunSpec.builder("viewClick"))
        addAdapterClickFun(FunSpec.builder("adapterClick"))
        return TypeSpec.classBuilder(className + "TargetClickImpl") //class 构造器
                .superclass(superclass!!)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("callBack", TargetClickImplCallback::class)
                        .build()
                )
                .addProperties(propertySpecs)
                .addFunctions(
                    listOf(viewClickFun!!,adapterClickFun!!,toTargetFun!!)
                ).build()
    }
}