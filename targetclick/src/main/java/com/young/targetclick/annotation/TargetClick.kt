package com.young.targetclick.annotation

@Target(AnnotationTarget.CLASS)
@Retention(value = AnnotationRetention.BINARY)

/*
* 统一事件线的主体
* poetClassName 局域插入的class名称 全路径类名
* */
annotation class TargetClick(val poetClassName:String="")


