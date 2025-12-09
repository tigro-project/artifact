package com.example.tigro.fragments.annotationList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import com.example.tigro.R
import com.example.tigro.util.annotation.TigroAnnotation
import kotlinx.coroutines.launch

// TODO: need a better way of searching for data
class AnnotationListContainerFragment : Fragment() {

    private var tigroAnnotations: List<TigroAnnotation> = emptyList()
    private lateinit var llItemsContainer: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_annotation_list_container, container, false)

        llItemsContainer = view.findViewById(R.id.llAnnotationItemsContainer)
        setData(tigroAnnotations)
        return view
    }

    fun setData(data: List<TigroAnnotation>) {
        tigroAnnotations = data
        llItemsContainer.removeAllViews()
        for (annotation in tigroAnnotations) {
            val itemFragment = AnnotationBarFragment.newInstance(annotation)

            childFragmentManager.beginTransaction()
                .add(llItemsContainer.id, itemFragment)
                .commit()
        }
    }


}