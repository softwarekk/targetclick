package com.young.targetclick

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlin.reflect.KClass

/*
 * Des  clickimpl 构造模板
 * Author Young
 * Date 
 */abstract class BaseClickPoet {
    constructor()

    //文件构造
    abstract fun addFile(fileSpec: FileSpec)
    //class :  BaseXmlBindingClick/BaseXmlDataBindingClick
    abstract fun addSuperclass(superclass: KClass<*>)
    /*
        生成如下代码
        override fun viewClick(view: Any) {
            super.viewClick(view)
            toTarget(view,null,null)
        }
    */
    abstract fun addViewClickFun(funSpec:FunSpec.Builder?)
    /*
        生成如下代码
        override fun adapterClick(view: Any, index: Int?) {
             super.adapterClick(view, index)
             toTarget(view,index,null)
        }
    */
    abstract fun addAdapterClickFun(funSpec:FunSpec.Builder?)
    /*
    *    生成如下代码
         override fun toTarget(
            view: Any,
            index: Int?,
            data: Any?
         ) {
            super.toTarget(view, index,data)
            implCallback.viewOnclick(view,index,data)
         }
    * */
    abstract fun addTargetFun(funSpec: FunSpec.Builder?)
    //
    abstract fun addPropertySpec(funSpec: PropertySpec.Builder?)


    /*
    * 输出构建的文件
    * addSuperclass -> addTargetFun->view/adapter
    * */
    abstract fun outPut(className: String): TypeSpec

}