package com.softtech360.totalservey.room.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Room
import android.content.Context
import android.util.Log


class DatabaseClient


private constructor(private val mCtx: Context) {

    //our app database object
    val appDatabase: AppDatabase
    val inMemoryAppDatabase: AppDatabase

    init {
        Log.e("DatabaseClient"," init--")

        appDatabase = Room.databaseBuilder(mCtx, AppDatabase::class.java, "Question.db").build()
       // appDatabase = Room.databaseBuilder(mCtx, AppDatabase::class.java, "chinook.db").addMigrations(AppDatabase.MIGRATION_1_2).build()
        inMemoryAppDatabase = Room.inMemoryDatabaseBuilder(mCtx, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
/*
        val db = RoomAsset
                .databaseBuilder(mCtx, AppDatabase::class.java, "survey.db")
                .build()

        appDatabase=   Room.databaseBuilder(mCtx,
                AppDatabase::class.java,
                "survey.db")
                .openHelperFactory(db)
                .allowMainThreadQueries()
                .build()

*/


    }


    fun getappDatabase() : AppDatabase{
        return appDatabase
    }
    fun getmemoryDatabase() : AppDatabase{
        return inMemoryAppDatabase
    }



    companion object {

        var mInstance: DatabaseClient? = null

        @Synchronized
        fun getInstance(mCtx: Context): DatabaseClient {
            if (mInstance == null) {
                mInstance = DatabaseClient(mCtx)
            }
            return mInstance as DatabaseClient
        }
    }
    fun getInMemoryDatabase(): SupportSQLiteDatabase {
        return inMemoryAppDatabase.openHelper.writableDatabase
    }



}



