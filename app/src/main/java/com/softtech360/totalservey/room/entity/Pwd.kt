package com.softtech360.totalservey.room.entity

import androidx.room.*
import org.jetbrains.annotations.NotNull
import java.io.Serializable


/*@Entity(
        foreignKeys = arrayOf(ForeignKey(entity = Question::class,
        parentColumns = arrayOf("section_id","section_name","question_id","question_type","question","answer"),
        childColumns = arrayOf("pwd_section_id","pwd_section_name","pwd_question_id","pwd_question_type","pwd_question","pwd_answer"),
        onDelete = ForeignKey.CASCADE)))*/



@Entity(tableName = "pwd")


data class Pwd(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "serial_number") var serial_number: Long?  , // sec id: 1->1.1,1.2
        @ColumnInfo(name = "is_pwd") var is_pwd: Int ,
        @NotNull
        @ColumnInfo(name = "form_id") var form_id: Int ,
        @NotNull
        @ColumnInfo(name = "user_type") var user_type: Int ,  // if you enter survey for 4 user then user type goes 4

        @ColumnInfo(name = "section_id") var section_id: Int , // sec id: 1->1.1,1.2
        @ColumnInfo(name = "p_section_id") var p_section_id: Int , // sec id: 1->1.1,1.2
        @ColumnInfo(name = "section_name") var section_name: String , // sec id: 1->1.1,1.2
        @ColumnInfo(name = "question_id") var question_id: Int ,  // id 0,1
        @ColumnInfo(name = "question_type") var question_type: Int, // que type -> option type muliple/single
        @ColumnInfo(name = "question") var question: String = "", // question in hindi
        @ColumnInfo(name = "answer") var answer: String ="", // answer : answer

        @ColumnInfo(name = "is_saved") var is_saved: Boolean? = false


) : Serializable
