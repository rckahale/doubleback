package com.issue.doubleback

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun goToNeedsPerm(view: View) {
        val intent = Intent(this, NeedsPermissionActivity::class.java)
        startActivity(intent)
    }
}
