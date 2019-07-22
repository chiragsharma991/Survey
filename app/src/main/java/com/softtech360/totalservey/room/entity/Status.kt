package com.softtech360.totalservey.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable


@Entity(tableName = "status")
data class Status(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "serial_number") var serial_number: Long?  , // sec id: 1->1.1,1.2

        @ColumnInfo(name = "form_id") var form_id: Int? ,
        @ColumnInfo(name = "email") var email: String="" ,

        @ColumnInfo(name = "user_id") var user_id: Int? ,
        @ColumnInfo(name = "date_time") var date_time: String="" ,
        @ColumnInfo(name = "completed_at") var completed_at: String="" ,
        @ColumnInfo(name = "latitude") var latitude: String="" ,
        @ColumnInfo(name = "longitude") var longitude: String="" ,
        @ColumnInfo(name = "organization") var organization: String="" ,
        @ColumnInfo(name = "state") var state: String="" ,
        @ColumnInfo(name = "district") var district: String="" ,
        @ColumnInfo(name = "section") var section: String="" ,
        @ColumnInfo(name = "village_council") var village_council: String="" ,
        @ColumnInfo(name = "village") var village: String="" ,
        @ColumnInfo(name = "colony") var colony: String="" ,

        @ColumnInfo(name = "question_answer") var question_answer: String="" ,
        @ColumnInfo(name = "is_form_submited") var submission_status: Boolean? = false,
        @ColumnInfo(name = "is_synch_completed") var synch_status: Boolean? = false











) : Serializable

