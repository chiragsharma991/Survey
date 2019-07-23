package com.softtech360.totalservey.room.dao

import androidx.room.*
import com.softtech360.totalservey.room.entity.Answer
import com.softtech360.totalservey.room.entity.Pwd
import com.softtech360.totalservey.room.entity.Question


@Dao
interface PwdDao {

    @get:Query("SELECT * FROM Pwd")
    val all: List<Pwd>

    @Query("SELECT * FROM Pwd WHERE form_id IN(:form_id)")
    fun getQuestionUsingFormId(form_id: Int): List<Pwd>

    @Query("SELECT * FROM Pwd WHERE user_type IN(:user_type)")
    fun getQuestionFromSection(user_type: Int): List<Pwd>

    @Query("SELECT * FROM Pwd WHERE user_type LIKE :user_type AND form_id LIKE :form_id ")
    fun getQuestionUserwise(user_type: Int, form_id : Int): List<Pwd>

    @Query("SELECT * FROM Pwd WHERE user_type LIKE :user_type AND form_id LIKE :form_id AND section_id LIKE :section_id")
    fun getQuestionbyFormId(user_type: Int , form_id : Int,section_id : Int): List<Pwd>

    @Query("SELECT * FROM Pwd WHERE user_type LIKE :user_type AND form_id LIKE :form_id AND section_id LIKE :section_id AND p_section_id LIKE :p_section_id ")
    fun getQuestionFromPsection(user_type: Int , form_id : Int,section_id : Int ,p_section_id : Int): List<Pwd>

    @Query("SELECT * FROM Pwd WHERE user_type LIKE :user_type AND form_id LIKE :form_id AND section_id LIKE :section_id")
    fun getQuestionbyFromSecId(user_type: Int , form_id : Int, section_id : Int): List<Pwd>

    @Query("SELECT * FROM Pwd WHERE form_id LIKE :form_id AND section_id LIKE :section_id")
    fun getQuestionusingFormNsec(form_id : Int, section_id : Int): List<Pwd>

    @Query("UPDATE Pwd SET answer = :answer, is_saved = :is_saved WHERE form_id LIKE :form_id AND user_type LIKE :user_type AND question_id LIKE :question_id")
    fun update(form_id: Int , user_type : Int, question_id : Int , answer : String, is_saved :Boolean)

    @Query("SELECT section_id FROM Pwd WHERE form_id LIKE :form_id AND p_section_id LIKE :p_section_id ORDER BY section_id DESC LIMIT 1")
    fun getlastSectionid(form_id: Int , p_section_id : Int) : Int


    @Insert
    fun insert(task: Pwd)

    @Delete
    fun delete(task: Pwd)

    @Query("DELETE FROM Pwd")
    fun drop()

    @Query("DELETE FROM Pwd WHERE form_id IN (:form_id) ")
    fun deleteFormbyId (form_id: Int)

    @Query("DELETE FROM Pwd WHERE form_id IN (:form_id) AND user_type IN (:user_type) AND section_id IN (:section_id) ")
    fun deleteFormbyUsertype (form_id: Int,user_type: Int, section_id : Int)



}