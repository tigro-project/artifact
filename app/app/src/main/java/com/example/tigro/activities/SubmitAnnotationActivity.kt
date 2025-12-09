package com.example.tigro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tigro.R
import com.example.tigro.data.Contact
import com.example.tigro.fragments.CreateAnnotation.LostAnnotationWarningFragment
import com.example.tigro.fragments.CreateAnnotation.CreateAnnotationFragment
import com.example.tigro.fragments.contactList.ContactListContainerFragment
import com.example.tigro.util.annotation.AnnotationManager
import com.example.tigro.util.annotation.SetAnnotation.TigroAnnotationListener
import com.example.tigro.util.annotation.TigroAnnotation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.time.LocalDateTime

/**
 * Comes from: MainActivity
 * Leads to: MainActivity (cancel, create)
 * Fragments: CreateAnnotationFragment, ContactListContainerFragment, LostAnnotationWarningFragment
 *
 * Create an annotation and submit it to the tigro server.
 * User initially sees the CreateAnnotationFragment, which allows them to provide a title and annotation.
 * They set an expiration, then choose who in their contacts to provide access to the annotation.
 */
class SubmitAnnotationActivity : AppCompatActivity(), TigroAnnotationListener {

    private lateinit var cancelAnnotationBtn: Button
    private lateinit var submitAnnotationBtn: Button

    private var currentFragment: Fragment? = null
    private val createAnnotationFrag = CreateAnnotationFragment()
    private val contactListContainerFrag = ContactListContainerFragment()

    private var targetContacts: List<Contact>? = null
    private var annotation: TigroAnnotation? = null
    private var expiration: LocalDateTime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_annotation)

        cancelAnnotationBtn = findViewById(R.id.cancelAnnotationBtn)
        submitAnnotationBtn = findViewById(R.id.submitAnnotationBtn)
        cancelAnnotationBtn.setOnClickListener { cancelAnnotationBtnClicked() }
        submitAnnotationBtn.setOnClickListener { submitAnnotationBtnClicked() }

        switchFragment(createAnnotationFrag)
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.submitAnnotationFragmentContainer, fragment)
            .commit()

        currentFragment = fragment
    }

    private fun submitAnnotationBtnClicked() {
        println("submit btn clicked")
        when (currentFragment) {
            createAnnotationFrag -> switchFragment(contactListContainerFrag)
            contactListContainerFrag -> submitAnnotation()
            else -> println("submitAnnotationActivity: no match in switch statement")
        }
    }

    private fun submitAnnotation() {
        if (annotation == null || targetContacts == null) {

        } else {
            println("annotation added")

            lifecycleScope.launch(Dispatchers.IO){
                AnnotationManager().submitAnnotationToBoxes(annotation!!, targetContacts!!)
            }

            Toast.makeText(this, "Annotation Submitted", Toast.LENGTH_SHORT).show()
        }
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun cancelAnnotationBtnClicked() {
        println("cancel btn clicked")
        displayCancelPopup()
    }

    private fun displayCancelPopup() {
        val popupFragment = LostAnnotationWarningFragment()
        popupFragment.show(supportFragmentManager, "popup_fragment")
    }

    override fun setAnnotation(tempAnn: TigroAnnotation) {
        if (expiration != null) {
            annotation = TigroAnnotation(
                tempAnn.label,
                tempAnn.imageByteArray,
                tempAnn.contents,
                expiration.toString()
            )
            println("annotation: $annotation")
        } else {
            throw NullPointerException("Issue with setAnnotation")
        }
    }

    override fun setAnnotationExpiration(tempExp: LocalDateTime) {
        expiration = tempExp
        println("expiration: $expiration")
    }

    override fun setContactTargetList(targets: List<Contact>) {
        targetContacts = targets
        println("target contacts: $targets")
    }


}