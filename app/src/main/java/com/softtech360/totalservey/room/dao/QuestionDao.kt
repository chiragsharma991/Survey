package com.softtech360.totalservey.room.dao

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.room.*
import com.softtech360.totalservey.room.entity.Question
import androidx.room.RawQuery
import com.softtech360.totalservey.activity.Login.User






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


 /*   @Query("SELECT * FROM User WHERE " +
            "addr_home_lat BETWEEN :lat1 AND :lat2" +
            " AND addr_home_lng BETWEEN :lng1 AND :lng2")
    fun findInRange(lat1: Long, lat2: Long, lng1: Long, lng2: Long): List<User>*/


}