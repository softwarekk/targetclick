package com.young.targetclick

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.young.targetclick.annotation.TargetClickAll
import com.young.targetclick.annotation.TargetClick
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

/*
 * Des
 * Author Young
 * Date
 */
@AutoService(Processor::class)
@SupportedAnnotationTypes("com.young.targetclick.annotation.TargetClick","com.young.targetclick.annotation.TargetClickAll") // 监控处理这个注解
@SupportedSourceVersion(SourceVersion.RELEASE_7) //版本依赖
@SupportedOptions("forModule") //module 接收值
class BindingClickProcessor : AbstractProcessor() {
    // 操作Element的工具类（类，函数，属性，其实都是Element）
    private var elementTool: Elements? = null
    // 输出在gradle中日志打印log
    private var messager: Messager? = null
    // 文件生成器， 类 资源 等，就是最终要生成的文件 是需要Filer来完成的
    private var filer: Filer? = null
    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        messager = processingEnvironment.messager
        elementTool = processingEnvironment.elementUtils
        filer = processingEnvironment.filer
        messager?.printMessage(Diagnostic.Kind.NOTE, ">>>>>>>>>young type build binding click ")
    }

    override fun process(set: Set<TypeElement?>, roundEnvironment: RoundEnvironment): Boolean {
        if (set == null) {
            messager!!.printMessage(Diagnostic.Kind.NOTE, "----BindingClick not found----")
            return false
        }
        // BindingClick 注解的 "类节点信息"
        val bindingClickElements = roundEnvironment.getElementsAnnotatedWith(
            TargetClick::class.java
        )
        val targetAllElements = roundEnvironment.getElementsAnnotatedWith(
                TargetClickAll::class.java
        )
        // 拿注解
        messager!!.printMessage(
            Diagnostic.Kind.NOTE,
            "BindingClick annotation size" + bindingClickElements.size
        )
        for (element in bindingClickElements) {
            //com.young.xxx 包节点
            val packageName = elementTool?.getPackageOf(element)?.qualifiedName.toString()
            // 获取简单类名，例如：MainActivity
            val className = element.simpleName.toString()
            messager?.printMessage(Diagnostic.Kind.NOTE, "BindingClick class name：$className")
            val bindingClick: TargetClick = element.getAnnotation(TargetClick::class.java)
            val poetName=bindingClick.poetClassName
            var originalClickPoet: OriginalClickPoet = OriginalClickPoet()
            //全域插入的查询逻辑
            messager!!.printMessage(
                Diagnostic.Kind.NOTE,
                "BindingClick targetAllElements size" + targetAllElements.size
            )

            if(targetAllElements.size>0){
                targetAllElements
                //唯一一个注解类被处理
                if(targetAllElements.size>1) {
                    messager?.printMessage(Diagnostic.Kind.WARNING, "BindingClick TargetClickAll class annotation is single please remove other class with TargetClickAll annotation ")
                }
                val targetClickAll: TargetClickAll =targetAllElements.elementAt(0)
                    .getAnnotation(TargetClickAll::class.java)
                val classByName =Class.forName(packageName+"."+targetAllElements.elementAt(0).simpleName)
                originalClickPoet=classByName.newInstance() as OriginalClickPoet
            }
            //局域插入的查询逻辑
            if(poetName!="") {
                val classByName =Class.forName(poetName)
                val a=classByName.newInstance() as BaseClickPoet
                FileSpec.builder(packageName,className+"BindingClickImpl").addType(a.outPut(className)).build().writeTo(filer!!)
            }else{
                FileSpec.builder(packageName,className+"BindingClickImpl").addType(originalClickPoet.outPut(className)).build().writeTo(filer!!)
            }
        }
        return true
    }
}