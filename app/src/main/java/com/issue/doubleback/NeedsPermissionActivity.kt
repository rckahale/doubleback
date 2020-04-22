package com.issue.doubleback

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class NeedsPermissionActivity : AppCompatActivity() {

    var oncePerLandingFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_needs_permission)
    }

    override fun onResume() {
        super.onResume()
        var thisContext: Context = this
        var moduleLevelPerms = arrayOf(Manifest.permission.RECORD_AUDIO)
        Dexter.withActivity(this)
            .withPermissions(*moduleLevelPerms).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // called for default permission popup?
                    report?.let {
                        if(it.areAllPermissionsGranted()){
                            // all good scenario
                        }
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    permissionReq: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                    if (!oncePerLandingFlag) {      // repeated getting fired because of fragment show hide activities
                        var permissionList: MutableList<String> = mutableListOf()
                        permissionReq?.let {
                            for (permit in it) {
                                permissionList.add(permit.name)
                            }
                        }
                        var displayMessg = "Some permissions missing. Please enable them to make best use of this app"
                        if (permissionList.indexOf(Manifest.permission.RECORD_AUDIO) != -1) {
                            displayMessg = "Please enable record audio permission"
                        }
                        showSettingsDialog(thisContext, displayMessg)
                        oncePerLandingFlag = true
                    }

                }
            }).check()

    }

    fun showSettingsDialog(
        context: Context, displayMsg: String
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Need Permission")
        builder.setMessage(displayMsg)
        builder.setPositiveButton("GOTO SETTINGS", object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, whch: Int) {
                dialog!!.dismiss()
//                openSettings(thisActivity)
            }
        })
        builder.setNegativeButton("Cancel", object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, whch: Int) {
                dialog!!.dismiss()
                // app is not usable, so show snackbar
            }
        })
        builder.show()
    }
    // navigating user to app settings
    fun openSettings(thisActivity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", thisActivity.getPackageName(), null)
        intent.data = uri
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        val randomActivityID = (100..199).random()  // if there will be multiple activity calling it, code should vary
        thisActivity.startActivityForResult(intent, randomActivityID)
    }

}
