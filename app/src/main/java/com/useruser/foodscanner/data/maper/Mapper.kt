package com.useruser.foodscanner.data.maper

import java.util.ArrayList

interface  Mapper<T1, T2>{

    fun map(value: T1): T2

    fun reverseMap(value: T2): T1

    fun map(values: List<T1>): List<T2>{
        val resultList = ArrayList<T2>(values.size)
        values.forEach { resultList.add(map(it)) }
        return resultList
    }

    fun reverseMap(values: List<T2>): List<T1>{
        val resultList = ArrayList<T1>(values.size)
        values.forEach { resultList.add(reverseMap(it)) }
        return resultList
    }
}