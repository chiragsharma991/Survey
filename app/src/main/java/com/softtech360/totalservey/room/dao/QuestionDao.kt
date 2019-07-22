package com.softtech360.totalservey.room.dao

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.room.*
import com.softtech360.totalservey.room.entity.Question
import androidx.room.RawQuery




@Dao
interface QuestionDao {

    @get:Query("SELECT * FROM question")
    val all: List<Question>

    @Query("SELECT * FROM question WHERE section_id IN(:section_id)")
    fun getQuestionFromSection(section_id: Int): List<Question>

    @Insert
    fun insert(task: Question)

    @Delete
    fun delete(task: Question)

    @Update
    fun update(task: Question)


    @Query("DELETE FROM question")
    fun drop()

    @RawQuery
    fun clearPrimaryKey(query: SupportSQLiteQuery): Int  //We can return int status like it used to return with database.delete()


}