package com.softtech360.totalservey.room.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import android.util.Log
import com.softtech360.totalservey.room.dao.AnswerDao
import com.softtech360.totalservey.room.dao.PwdDao
import com.softtech360.totalservey.room.dao.QuestionDao
import com.softtech360.totalservey.room.dao.StatusDao
import com.softtech360.totalservey.room.entity.Answer
import com.softtech360.totalservey.room.entity.Pwd
import com.softtech360.totalservey.room.entity.Question
import com.softtech360.totalservey.room.entity.Status

@Database(entities = arrayOf(Question::class,Answer::class,Status::class,Pwd::class), version = 1)
abstract class AppDatabase : RoomDatabase(){
abstract fun questiondao(): QuestionDao
abstract fun answerdao(): AnswerDao
abstract fun statusdao(): StatusDao
abstract fun pwddao(): PwdDao

/*    companion object {
        @JvmField
        val MIGRATION_1_2 : Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.e("Migreation ---","+++----")
            }
        }}*/


}
