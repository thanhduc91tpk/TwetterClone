package com.thanhduc91tpk.twetterclone.util

data class User(
    val email : String? ="",
    val username : String? ="",
    val imageUrl : String? ="",
    val followHashtags : ArrayList<String>? = arrayListOf(),
    val followUsers : ArrayList<String>? = arrayListOf()
)