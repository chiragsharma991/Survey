package com.softtech360.totalservey.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable


@Entity(tableName = "answer")
data class Answer(


        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "serial_number")
        var serial_number: Long?, // sec id: 1->1.1,1.2
        @ColumnInfo(name = "is_pwd") var is_pwd: Int ,

        @ColumnInfo(name = "form_id")
        var form_id: Int? ,
        @ColumnInfo(name = "section_id")
        var section_id: Int, // sec id: 1->1.1,1.2
        @ColumnInfo(name = "section_name")
        var section_name: String, // sec id: 1->1.1,1.2
        @ColumnInfo(name = "question_id")
        var question_id: Int,  // id 0,1
        @ColumnInfo(name = "question_type")
        var question_type: Int, // que type -> option type muliple/single
        @ColumnInfo(name = "question")
        var question: String = "", // question in hindi
        @ColumnInfo(name = "answer")
        var answer: String = "" // answer : answer

) : Serializable
