package com.sg.jhony50.model

import com.google.firebase.Timestamp

data class Thought constructor(
    val userName:String,
    val timestamp: Timestamp?,
    val thoughtTxt:String,
    val numLikes:Int,
    val numComments:Int,
    val documentId:String
)