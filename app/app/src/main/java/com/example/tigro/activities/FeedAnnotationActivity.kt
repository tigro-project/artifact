package com.example.tigro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.tigro.R
import com.example.tigro.fragments.annotationList.AnnotationListContainerFragment
import com.example.tigro.util.Server.GrpcTigroServerProxy
import com.example.tigro.util.Server.TigroServer
import com.example.tigro.util.annotation.AnnotationManager
import com.example.tigro.util.annotation.TigroAnnotation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.Month


/**
 * Comes from: MainActivity
 * Leads to: MainActivity (cancel, create)
 * Fragments: AnnotationListContainerFragment
 *
 * Searches for annotations in the tigro database via feed, returns relevant annotations
 */
class FeedAnnotationActivity : AppCompatActivity() {

    private val annotationListContainerFrag = AnnotationListContainerFragment()

    private lateinit var annotationFeedCancelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_annotation)

        annotationFeedCancelBtn = findViewById(R.id.annotationFeedCancelBtn)
        annotationFeedCancelBtn.setOnClickListener { annotationFeedCancelBtnClicked() }

        supportFragmentManager.beginTransaction()
            .replace(R.id.feedAnnotationFragmentContainer, annotationListContainerFrag)
            .commit()


    }

    private fun annotationFeedCancelBtnClicked() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private suspend fun getFromServer(query: String): List<TigroAnnotation> {
        return withContext(Dispatchers.IO){
            AnnotationManager().getAnnotationsByLabel("test", applicationContext)
        }
    }
}