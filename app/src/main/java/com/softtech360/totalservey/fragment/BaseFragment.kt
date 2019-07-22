package com.softtech360.totalservey.fragment

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import com.softtech360.totalservey.BuildConfig
import com.softtech360.totalservey.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

abstract class BaseFragment : Fragment() {

    abstract fun getLayout(): Int
    abstract fun initView(view: View?, savedInstanceState: Bundle?)
    lateinit var displayMetrics: DisplayMetrics
    private var dialog: ProgressDialog? = null
    private var backpress_timeout: Boolean = false
    private val timeoutHandler = Handler()
    private var finalizer: Runnable? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        displayMetrics = DisplayMetrics()
        getActivityBase().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    fun addFragment(container: Int, fragment: Fragment, tag: String, isAnimated: Boolean) {
        // fragment tag is for get fragment from container but backstack tag is for remove fragment from backstack queue.
        hideKeyboard()
        val mfragmentTransaction = childFragmentManager.beginTransaction()
    //    childFragmentManager.popBackStackImmediate()
        if (isAnimated)
            mfragmentTransaction.setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_from_left)

        mfragmentTransaction.replace(container, fragment, tag).addToBackStack(tag).commitAllowingStateLoss()
    }


    fun rightToLeftAnimation(context: Context): Animation {
        val animation = AnimationUtils.loadAnimation(context, R.anim.enter_from_right)
        animation.duration = 250
        return animation
    }

    fun is_callphn_PermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            //phone permission
            if (context!!.checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                //ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CALL_PHONE), 1)
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 0)
                return false
            }

        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }
    }

    fun is_location_PermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            // location permission
            if (context!!.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    context!!.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true

            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                return false
            }

        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }

    }

    fun getpostalfrom_latlang(latitude: Double, longitude: Double): String {

        /**
         * TODO :  IOException: grpc failed
         * */

        val gcd = Geocoder(context!!)
        val addresses: List<Address> = gcd.getFromLocation(latitude, longitude, 1) // error
        for (address: Address in addresses) {
            if (address.getLocality() != null && address.getPostalCode() != null) {
                // Log.e("",address.getLocality())
                Log.e("getpostalfrom_latlang", address.getPostalCode())
                return address.getPostalCode()
            }
        }
        return ""
    }




    fun popFragment(): Boolean {
        var isPop = false
        try {
            if (childFragmentManager.backStackEntryCount > 0) {
                /**
                 * Check Filter Fragment Appear or not, Filter Type Fragment Also
                 */
                hideKeyboard()
                loge("backStackCount", childFragmentManager.backStackEntryCount.toString())
                val fragment = childFragmentManager.findFragmentByTag(childFragmentManager.getBackStackEntryAt(childFragmentManager.backStackEntryCount - 1).name)
                if (fragment != null && fragment.isVisible) {
                    isPop = true
                    when (fragment) {



                    }
                    //   DrawableCompat.setTint(ContextCompat.getDrawable(activity!!, R.drawable.close)!!, ContextCompat.getColor(activity!!, R.color.white));

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isPop
    }

    fun popAllFragment() {

        for (i in 0 until childFragmentManager.backStackEntryCount) {
            childFragmentManager.popBackStack()
        }
    }

    fun popWithTag(tag: String) {
        childFragmentManager.popBackStack(tag, 0)
    }

    fun pop() {
        childFragmentManager.popBackStack()
    }






    fun isInternetAvailable(): Boolean {
        val connectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }



    fun validMail(email: String): Boolean {

        //  val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val EMAIL_PATTERN = "^[ÆØÅæøåA-Za-z0-9._%+-]+@(?:[ÆØÅæøåA-Za-z0-9-]+\\.)+[A-Za-z]{2,6}\$"
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()

    }


    fun isGpsEnable(): Boolean {
        val manager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return statusOfGPS
    }


    fun showSnackBar(view: View, string: String) {
        Snackbar.make(view, string, Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackBarIndefinite(view: View, string: String) {
        Snackbar.make(view, string, Snackbar.LENGTH_INDEFINITE).show()
    }


    fun showProgressDialog() {
        if (dialog == null) {
            dialog = ProgressDialog(activity);
            dialog!!.setMessage(getString(R.string.please_wait))
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.show()
        } else {
            dialog!!.dismiss()
            dialog = null
        }


    }

    fun clearProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
    }



    fun getActivityBase(): Activity {
        return activity!!
    }

    fun loge(tag: String, msg: String) {
        if (BuildConfig.DEBUG)
            Log.e(tag, msg)
    }

    fun logd(tag: String, msg: String) {
        if (BuildConfig.DEBUG)
            Log.d(tag, msg)
    }

    /**
     * API CAll START
     */



    interface OnApiCallInteraction {
        //  100 > network not found  : 404 > server error.
        fun <T> onSuccess(body: T?)

        fun onFail(error: Int)
    }

    /**
     * API Call END
     */

    fun hideKeyboard() {
        if (view != null) {
            val imm = getActivityBase().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


        }
    }

    fun showKeyboard() {
        if (view != null) {
            val imm = getActivityBase().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //  imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        }
    }

    fun getHeight(): Int {
        return displayMetrics.heightPixels
    }

    fun getWidth(): Int {
        return displayMetrics.widthPixels
    }


    fun getCalculatedDate(dateFormat: String, days: Int): String {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    fun gettimefrom_date(target_date: String, target_format: String): Long {
        //val sdf = SimpleDateFormat("dd/MM/yyyy")
        val sdf = SimpleDateFormat(target_format, Locale.ENGLISH)
        val strDate = sdf.parse(target_date)
        return strDate.time
    }

    fun getCurrentDate(): String {
        //2019-06-05 13:21:45
        val c = Calendar.getInstance().getTime() //yyyy-MM-dd  dd-MMM-yyyy
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedDate = df.format(c)
        return formattedDate
    }



    // Transition

    fun translateAnim(from_x: Float, to_x: Float, from_y: Float, to_y: Float, duration: Long, fill: Boolean): TranslateAnimation {
        val animation = TranslateAnimation(from_x, to_x, from_y, to_y)
        animation.duration = duration
        animation.fillAfter = fill
        return animation
    }



}