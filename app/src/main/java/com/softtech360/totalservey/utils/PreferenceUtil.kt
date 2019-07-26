package com.softtech360.totalservey.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log

object PreferenceUtil {

    private var sharedPreferences: SharedPreferences? = null

    private var editor: SharedPreferences.Editor? = null

    /**
     * returns map of all the key value pair available in SharedPreference
     *
     * @return Map<String></String>, ?>
     */
    val all: Map<String, *>
        get() = sharedPreferences!!.all

    /**
     * Initialize the SharedPreferences instance for the app.
     * This method must be called before using any other methods of this class.
     */

    fun init(mcontext: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mcontext)
            editor = sharedPreferences!!.edit()
        }
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     */
    fun putValue(key: String, value: String) {
        editor!!.putString(key, value)
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     */
    fun putValue(key: String, value: Int) {
        editor!!.putInt(key, value)
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     */
    fun putValue(key: String, value: Long) {
        editor!!.putLong(key, value)
    }

    /**
     * Puts new Key and its Values into SharedPreference map.
     *
     * @param key
     * @param value
     */
    fun putValue(key: String, value: Boolean) {
        editor!!.putBoolean(key, value)
    }


    /**
     * saves the values from the editor to the SharedPreference
     */
    fun clearAll() {
        editor!!.clear()
    }

    fun remove(key : String) {
        editor!!.remove(key)
    }


    fun save() {
        editor!!.commit()
    }

    /**
     * returns a values associated with a Key default value ""
     *
     * @return String
     */
    fun getString(key: String, defValue: String): String? {
        return sharedPreferences!!.getString(key, defValue)
    }

    /**
     * returns a values associated with a Key default value -1
     *
     * @return String
     */
    fun getInt(key: String, defValue: Int): Int {
        return sharedPreferences!!.getInt(key, defValue)
    }

    /**
     * returns a values associated with a Key default value -1
     *
     * @return String
     */
    fun getLong(key: String, defValue: Long): Long {
        return sharedPreferences!!.getLong(key, defValue)
    }

    /**
     * returns a values associated with a Key default value false
     *
     * @return String
     */
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defValue)
    }

    /**
     * Checks if key is exist in SharedPreference
     *
     * @param key
     * @return boolean
     */
    operator fun contains(key: String): Boolean {
        return sharedPreferences!!.contains(key)
    }



    val USER_STATES = "user_states"
    val USER_DISTRICTS = "user_districts"
    val USER_SECTIONS = "user_sections"
    val USER_VILLAGE_COUNCIL = "user_village_council"
    val FORM_ID = "form_id"
    val USER_ID = "user_id"
    val TOTAL_SECTION = "total_section"
    val USER_NAME = "user_name"
    val ORGANIZATION_NAME = "organization_name"
    val SURVEY_COUNT = "survey_count"
    val ORGANIZATION_ID = "organization_id"
    val EMAIL = "email"
    val PASSWORD = "password"
    val ISLOGIN = "islogin"
    val INITIAL_TIME = "initial_time"
    val STATUSOFAGE = "statusofAge"
    val UPDATE_VERSION_CODE = "update_version_code"

}