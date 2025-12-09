package com.example.tigro.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.tigro.R
import com.example.tigro.database.AppDatabase
import com.example.tigro.database.AppDatabaseSingleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



/**
 * Comes from: LoginActivity
 * Leads to: ScanQrActivity, SubmitAnnotationActivity, SearchAnnotationActivity, CreateGroupActivity, SettingsActivity
 *
 * tigro app main page
 */
class MainActivity : AppCompatActivity() {

    private lateinit var addContactBtn: Button
    private lateinit var submitAnnotationBtn: Button
    private lateinit var searchAnnotationBtn: Button
    private lateinit var createGroupBtn: Button
    private lateinit var settingsBtn: Button
    private lateinit var readDbBtn: Button
    private lateinit var shareTestBtn: Button
    private lateinit var miscTestBtn: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addContactBtn = findViewById(R.id.addContactBtn)
        addContactBtn.setOnClickListener { addContactBtnClicked() }

        submitAnnotationBtn = findViewById(R.id.submitAnnotationBtn)
        submitAnnotationBtn.setOnClickListener { submitAnnotationBtnClicked() }

        searchAnnotationBtn = findViewById(R.id.searchAnnotationBtn)
        searchAnnotationBtn.setOnClickListener { searchAnnotationBtnClicked() }

        createGroupBtn = findViewById(R.id.createGroupBtn)
        createGroupBtn.setOnClickListener { createGroupBtnClicked() }

        settingsBtn = findViewById(R.id.settingsBtn)
        settingsBtn.setOnClickListener { settingsBtnClicked() }

        shareTestBtn = findViewById(R.id.shareTestBtn)
        shareTestBtn.setOnClickListener { shareTestBtnClicked() }

        miscTestBtn = findViewById(R.id.miscTestBtn)
        miscTestBtn.setOnClickListener { miscTestBtnClicked() }

        // NOTE: running this will disable any demo buttons
        disableTestButtons()
    }

    private fun shareTestBtnClicked() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            putExtra(Intent.EXTRA_SUBJECT, "TEST: title")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun disableTestButtons() {
        Log.d("prod","disabling testing buttons for prod")
        readDbBtn.visibility = View.GONE
        shareTestBtn.visibility = View.GONE
        miscTestBtn.visibility = View.GONE
    }

    private fun searchAnnotationBtnClicked() {
        startActivity(Intent(this, SearchAnnotationActivity::class.java))
    }

    private fun settingsBtnClicked() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun createGroupBtnClicked() {
        startActivity(Intent(this, CreateGroupActivity::class.java))
    }

    private fun miscTestBtnClicked() {
    }

    private fun submitAnnotationBtnClicked() {
        startActivity(Intent(this, SubmitAnnotationActivity::class.java))
    }

    private fun addContactBtnClicked() {
        startActivity(Intent(this, ScanQrActivity::class.java))
    }
}