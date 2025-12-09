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
 * Searches for annotations in the tigro database, returns relevant annotations
 */
class SearchAnnotationActivity : AppCompatActivity() {

    private val annotationListContainerFrag = AnnotationListContainerFragment()

    private lateinit var editTextAnnotationSearch: EditText
    private lateinit var annotationSearchCancelBtn: Button
    private lateinit var annotationSearchQueryBtn: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_annotation)

        editTextAnnotationSearch = findViewById(R.id.editTextAnnotationSearch)
        annotationSearchCancelBtn = findViewById(R.id.annotationSearchCancelBtn)
        annotationSearchQueryBtn = findViewById(R.id.annotationSearchQueryBtn)
        annotationSearchCancelBtn.setOnClickListener { annotationSearchCancelBtnClicked() }
        annotationSearchQueryBtn.setOnClickListener { annotationSearchQueryBtnClicked() }

        supportFragmentManager.beginTransaction()
            .replace(R.id.searchAnnotationFragmentContainer, annotationListContainerFrag)
            .commit()
    }

    private fun annotationSearchCancelBtnClicked() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun annotationSearchQueryBtnClicked() {
        val query: String = editTextAnnotationSearch.text.toString()
        lifecycleScope.launch {
            val annotations: List<TigroAnnotation> = getFromServer(query)
//            val mockAnnotations = getMockAnnotations()
            annotationListContainerFrag.setData(annotations)
        }
    }


    // Mock function for testing data
    private fun getMockAnnotations(): List<TigroAnnotation> {
        val dummyDateTime = LocalDateTime.of(2024, Month.JULY, 8, 14, 0).toString()
        val annA = TigroAnnotation("titleA", ByteArray(0), " contentsA", dummyDateTime)
        val annB = TigroAnnotation("titleB", ByteArray(0), " contentsB", dummyDateTime)
        val annC = TigroAnnotation("titleC", ByteArray(0), " contentsC", dummyDateTime)

        return listOf(annA, annB, annC)
    }

    private suspend fun getFromServer(query: String): List<TigroAnnotation> {
        return withContext(Dispatchers.IO){
            AnnotationManager().getAnnotationsByLabel(query, applicationContext)
        }
    }
}