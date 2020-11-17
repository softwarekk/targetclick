package com.young.targetclick.annotation

/*
 * Des  全域插入的标识
 * Author Young
 * Date 
 */
@Target(AnnotationTarget.CLASS)
@Retention(value = AnnotationRetention.BINARY)

annotation class TargetClickAll(val targetClassName:String="")