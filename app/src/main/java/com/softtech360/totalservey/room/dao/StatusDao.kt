package com.softtech360.totalservey.room.dao

import androidx.room.*
import com.softtech360.totalservey.room.entity.Status


@Dao
interface StatusDao {

    @get:Query("SELECT * FROM status")
    val all: List<Status>

    @Query("SELECT * FROM status WHERE form_id LIKE :form_id")
    fun getStatusListByformId (form_id: Int ): List<Status>

    @Insert
    fun insert(task: Status)

    @Delete
    fun delete(task: Status)

    @Query("UPDATE status SET question_answer = :question_answer , is_form_submited = :is_form_submited WHERE form_id LIKE :form_id")
    fun updateStatus(form_id: Int , question_answer : String, is_form_submited : Boolean)

    @Query("UPDATE status SET is_synch_completed = :is_synch_completed WHERE form_id LIKE :form_id")
    fun updateSync(form_id: Int , is_synch_completed : Boolean)

    @Query("UPDATE status SET date_time = :date_time , latitude = :latitude , longitude = :longitude , state = :state , district = :district , section = :section , village_council = :village_council , village = :village , colony = :colony WHERE form_id LIKE :form_id")
    fun updateEntries(form_id: Int ,date_time : String ,latitude : String, longitude : String, state : String,
                      district : String, section : String, village_council : String, village : String, colony : String)

    @Query("UPDATE status SET completed_at = :completed_at WHERE form_id LIKE :form_id")
    fun updateCompleteDate(completed_at : String , form_id : Int )

    @Query("UPDATE status SET  latitude = :latitude , longitude = :longitude WHERE form_id LIKE :form_id")
    fun updateLocation(form_id: Int ,latitude : String, longitude : String)

    @Query("DELETE FROM status WHERE form_id IN (:form_id) ")
    fun deleteFormbyId (form_id: Int)

    @Query("DELETE FROM status")
    fun drop()


}

