package com.sumit.mvvmwithcoroutine.model

data class User(
    val id: String = "",
    val createdAt: String = "",
    val name: String = "",
    val avatar: String = ""
){
    override fun toString(): String {
        return "ID: ${id}\nName: ${name}\nCreated At: ${createdAt}"
    }
}