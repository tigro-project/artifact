package com.example.tigro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.tigro.R
import com.example.tigro.fragments.contactList.ContactListContainerFragment
import com.example.tigro.data.TargetGroup
import com.example.tigro.fragments.NameNewContactGroupFragment


/**
 * Comes from: MainActivity
 * Leads to: MainActivity (cancel, create)
 * Fragments: ContactListContainerFragment
 *
 * Allows users to create groups of their contacts and save those to the DB
 */

// TODO: finish implementing functionality
class CreateGroupActivity : AppCompatActivity() {

    private val contactListContainerFrag = ContactListContainerFragment()
    private lateinit var createGroupCancelBtn: Button
    private lateinit var createGroupCreateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        supportFragmentManager.beginTransaction()
            .replace(R.id.createGroupFragmentContainer, contactListContainerFrag)
            .commit()

        createGroupCancelBtn = findViewById(R.id.createGroupCancelBtn)
        createGroupCancelBtn.setOnClickListener { createGroupCancelBtnClicked() }
        createGroupCreateBtn = findViewById(R.id.createGroupCreateBtn)
        createGroupCreateBtn.setOnClickListener { createGroupCreateBtnClicked() }

    }

    // TODO: make this button disabled unless at least two people are selected
    private fun createGroupCreateBtnClicked() {
        val tempGroup: TargetGroup = contactListContainerFrag.getNewTargetGroup()
        println("creating group now: $tempGroup")
        val newGroupFrag = NameNewContactGroupFragment().newInstance(tempGroup)
        newGroupFrag.show(supportFragmentManager, "NameNewContactGroupFragment")
    }

    private fun createGroupCancelBtnClicked() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}