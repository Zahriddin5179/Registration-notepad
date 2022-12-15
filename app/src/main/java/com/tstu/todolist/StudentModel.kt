package com.tstu.todolist

import kotlin.random.Random

data class StudentModel(
    var id: Int = getAutoId(),
    var name: String,
    var email:String

) {
    companion object {
        fun getAutoId(): Int {
            val random = java.util.Random()
            return random.nextInt(100)
        }
    }
}