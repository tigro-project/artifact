package com.example.tigro.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tigro.R
import com.example.tigro.fragments.password.ChangePasswordFragment
import com.example.tigro.fragments.password.SetNuclearPasswordFragment
import com.example.tigro.util.account.AccountManager

/**
 * Comes from: MainActivity
 * Leads to: MainActivity (cancel)
 * Fragments: ChangePasswordFragment, SetNuclearPasswordFragment
 *
 * Allows user to change their settings, including:
 *  - change pin (must enter current pin, new pin and confirm)
 *  - set/change nuclear pin (must enter current pin, nuclear pin and confirm)
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var changePinSettingsBtn: Button
    private lateinit var nuclearPinSettingsBtn: Button
    private lateinit var darkModeSettingsBtn: Button
    private lateinit var logoutSettingsBtn: Button

    private lateinit var cancelSettingsBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        changePinSettingsBtn = findViewById(R.id.changePinSettingsBtn)
        changePinSettingsBtn.setOnClickListener { changePinSettingsBtnClicked() }

        nuclearPinSettingsBtn = findViewById(R.id.nuclearPinSettingsBtn)
        nuclearPinSettingsBtn.setOnClickListener { nuclearPinSettingsBtnClicked() }

        logoutSettingsBtn = findViewById(R.id.logoutSettingsBtn)
        logoutSettingsBtn.setOnClickListener { logoutSettingsBtnClicked() }

        cancelSettingsBtn = findViewById(R.id.cancelSettingsBtn)
        cancelSettingsBtn.setOnClickListener { cancelSettingsBtnClicked() }


    }

    private fun logoutSettingsBtnClicked() {
        AccountManager(this).logout()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun changePinSettingsBtnClicked() {
        ChangePasswordFragment().show(supportFragmentManager, "ChangePasswordFragment")
    }

    private fun nuclearPinSettingsBtnClicked() {
        SetNuclearPasswordFragment().show(supportFragmentManager, "SetNuclearPasswordFragment")
    }

    private fun cancelSettingsBtnClicked() {
        startActivity(Intent(this, MainActivity::class.java))
    }

}