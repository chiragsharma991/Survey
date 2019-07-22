package com.softtech360.totalservey.activity


import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.sqlite.db.SimpleSQLiteQuery
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.softtech360.totalservey.BuildConfig
import com.softtech360.totalservey.R
import com.softtech360.totalservey.fragment.BaseFragment
import com.softtech360.totalservey.room.database.AppDatabase
import com.softtech360.totalservey.utils.PreferenceUtil
import kotlinx.android.synthetic.main.new_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.concurrent.thread


abstract class BaseActivity : AppCompatActivity() {
    // protected abstract fun getLayout(): Int

    // protected abstract fun init(savedInstancedState: Bundle?)

/*    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        init(savedInstanceState)


    }*/

    var dialog: ProgressDialog? = null


/*    fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }*/

    fun changeStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    fun loge(tag: String, msg: String) {
        if (BuildConfig.DEBUG)
            Log.e(tag, msg)
    }

    fun logd(tag: String, msg: String) {
        if (BuildConfig.DEBUG)
            Log.d(tag, msg)
    }

    fun fullScreen() {
        if (Build.VERSION.SDK_INT >= 21) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    fun finishThisActivity() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition()
        else
            finish()
    }

    fun is_location_PermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            // location permission
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true

            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                return false
            }

        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }

    }

    fun isGpsEnable(): Boolean {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return statusOfGPS
    }

    fun validMail(email: String): Boolean {

        //  val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val EMAIL_PATTERN = "^[ÆØÅæøåA-Za-z0-9._%+-]+@(?:[ÆØÅæøåA-Za-z0-9-]+\\.)+[A-Za-z]{2,6}\$"
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()

    }


    fun rightToLeftAnimation(context: Context): Animation {
        val animation = AnimationUtils.loadAnimation(context, R.anim.enter_from_right)
        animation.duration = 250
        return animation
    }

    fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }
    }

    var databaseclient_ : AppDatabase? = null
    val locationListener = object : LocationListener {

        override fun onStatusChanged(provider: String, status: Int,
                                     extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}

        override fun onLocationChanged(location: Location) {

            // Display currnet longitude and latitude over textview

            // The toast will show loaction continuosly as we are requesting
            // local updates
            thread {
                // pass the values in status table:
                //     loge(TAG,"updateEntries -- "+dob.text!!.trim().toString()+" "+state_value+" "+district_value+" "+section_value+" "+village_council_value+" "+village.text!!.trim().toString()+" "+colony.text!!.trim().toString())

                databaseclient_?.let {
                    loge("tag","update status")
                    it.statusdao().updateLocation(
                            form_id = PreferenceUtil.getInt(PreferenceUtil.FORM_ID,0),
                            latitude = location.getLatitude().toString(),
                            longitude = location.longitude.toString()
                    )
                }


            }

            Log.e("location-",""+location.getLatitude().toString()+" - "+location.longitude.toString())





        }
    }


    // Method that will fetch the location in longitude and latitude in absence
    // of internet
    @SuppressLint("MissingPermission")
    fun getLocationWithoutInternet (appdatabase : AppDatabase) {

        // Change the state of button
        Log.e("location-","on--")

        val locationManager = this
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager

        databaseclient_=appdatabase
        locationManager.removeUpdates(locationListener)

        // Define a listener that responds to location updates

        // Register the listener with the Location Manager to receive location
        // updates
        if (locationManager.allProviders.contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

        if (locationManager.allProviders.contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }


    fun showProgressDialog() {
        if (dialog == null) {
            dialog = ProgressDialog(this);
            dialog!!.setMessage(getString(R.string.please_wait))
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.show()
        } else {
            dialog!!.dismiss()
            dialog = null
        }


    }

    fun removeLocationListner(){

        val locationManager = this
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.removeUpdates(locationListener)
    }

    fun clearProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
    }


    fun showSnackBar(view: View, string: String) {
        Snackbar.make(view, string, Snackbar.LENGTH_SHORT).show()
    }

    fun hideKeyboard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show()
    }

    fun popFragment(): Boolean {
        var isPop = false
        try {
            if (supportFragmentManager.backStackEntryCount > 0) {
                hideKeyboard()
                isPop = true
                supportFragmentManager.popBackStack()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isPop
    }

    fun popAllFragment() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }

    fun popAllReorderFragment() {
        for (i in 0 until (supportFragmentManager.backStackEntryCount - 1)) {
            supportFragmentManager.popBackStack()
        }
    }

    fun popWithTag(tag: String) {
        supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)


    }

    fun pop() {
        supportFragmentManager.popBackStack()
    }

    /**
     * Check fragment added or not
     */
    fun isAddedFragment(tag: String): Boolean {
        var frag = supportFragmentManager.findFragmentByTag(tag)
        return frag != null && frag.isVisible && frag.isVisible
    }

    /**
     * @param tag which fragment you need to visible
     */
    fun showFragment(tag: String) {
        hideKeyboard()
        supportFragmentManager.popBackStack(tag, 0)
    }

    /**
     * @param container is the id of adding fragment
     * @param fragment -> your fragment
     * @param tag Name of tag any
     */
    fun addFragment(container: Int, fragment: Fragment, tag: String, isAnimation: Boolean) {
        hideKeyboard()
        addFragment(container, fragment, tag, isAnimation, true)
    }

    /**
     * @param container is the id of adding fragment
     * @param fragment -> your fragment
     * @param tag Name of tag any
     * @param isAnimation for support animation
     */
    fun addFragment(container: Int, fragment: Fragment, tag: String, isAnimation: Boolean, isAddToBackStack: Boolean) {
        hideKeyboard()

        var mFragTransaction = supportFragmentManager.beginTransaction()
        if (isAnimation)
            mFragTransaction.setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_from_left)
        mFragTransaction.add(container, fragment, tag)
        if (isAddToBackStack)
            mFragTransaction.addToBackStack(tag)
        mFragTransaction.commit()
    }

    // Transition

    fun translateAnim(from_x: Float, to_x: Float, from_y: Float, to_y: Float, duration: Long, fill: Boolean): TranslateAnimation {
        val animation = TranslateAnimation(from_x, to_x, from_y, to_y)
        animation.duration = duration
        animation.fillAfter = fill
        return animation
    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    fun <T> callAPI(call: Call<T>, onAliCallInteraction: BaseFragment.OnApiCallInteraction) {
        if (isInternetAvailable()) {
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    try {
                        if (response.isSuccessful) {
                            loge("response.body----", response.body().toString())
                            onAliCallInteraction.onSuccess(response.body())
                        } else {
                            // var mErrorBody: String = response.errorBody()!!.string()
                            onAliCallInteraction.onFail(404)
                        }
                    } catch (e: Exception) {
                        loge("error of catch ", e.toString())
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    loge("on failure", "-----" + t.message)
                    onAliCallInteraction.onFail(404)
                }
            })
        } else {
            onAliCallInteraction.onFail(100)
            //  showSnackBar(view, getString(R.string.internet_not_available))
        }
    }

    fun getCurrentDate(): String {
        //2019-06-05 13:21:45
        val c = Calendar.getInstance().getTime() //yyyy-MM-dd  dd-MMM-yyyy
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedDate = df.format(c)
        return formattedDate
    }

    fun deleteAllTables(databaseclient : AppDatabase){

       // thread {

            databaseclient.pwddao().drop()
            databaseclient.answerdao().drop()
            databaseclient.statusdao().drop()
            databaseclient.questiondao().drop()
           // databaseclient.questiondao().clearPrimaryKey(SimpleSQLiteQuery("UPDATE SQLITE_SEQUENCE SET seq = 1 WHERE name = 'Question'"))
      //  }
    }


}