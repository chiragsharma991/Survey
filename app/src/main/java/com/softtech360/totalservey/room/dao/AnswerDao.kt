package com.softtech360.totalservey.room.dao

import androidx.room.*
import com.softtech360.totalservey.room.entity.Answer
import com.softtech360.totalservey.room.entity.Question


@Dao
interface AnswerDao {

    @get:Query("SELECT * FROM answer")
    val all: List<Answer>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Answer)

    @Query("SELECT * FROM Answer WHERE form_id LIKE :form_id AND section_id LIKE :section_id")
    fun getAnswerbySecId(form_id: Int , section_id : Int): List<Answer>

    @Query("SELECT * FROM Answer WHERE form_id LIKE :form_id")
    fun getAnswerbyFormId(form_id: Int): List<Answer>

    @Query("UPDATE Answer SET answer = :answer WHERE form_id LIKE :form_id AND section_id LIKE :section_id AND question_id LIKE :question_id")
    fun update(form_id: Int , section_id : Int, question_id : Int , answer : String)

    @Delete
    fun delete(task: Answer)

    @Query("DELETE FROM Answer WHERE form_id IN (:form_id) ")
    fun deleteFormbyId (form_id: Int)

    @Query("DELETE FROM Answer WHERE form_id LIKE :form_id AND section_id LIKE :section_id")
    fun deleteEntry (form_id: Int , section_id : Int)

    @Query("DELETE FROM answer")
    fun drop()


}