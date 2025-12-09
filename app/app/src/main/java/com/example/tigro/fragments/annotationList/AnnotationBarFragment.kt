package com.example.tigro.fragments.annotationList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.tigro.R
import com.example.tigro.util.annotation.AnnotationImageManager
import com.example.tigro.util.annotation.TigroAnnotation


/**
 * Fragment representing one Tigro Annotation in the AnnotationListContainerFragment
 */
class AnnotationBarFragment : Fragment() {

    private lateinit var annotation: TigroAnnotation

    private lateinit var annotationBarName: TextView
    private lateinit var annotationBarMessage: TextView
    private lateinit var annotationBarImage: ImageView

    companion object {
        fun newInstance(annotation: TigroAnnotation): AnnotationBarFragment {
            val fragment = AnnotationBarFragment()
            fragment.annotation = annotation
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_annotation_bar, container, false)

        annotationBarName = view.findViewById(R.id.annotationBarName)
        annotationBarMessage = view.findViewById(R.id.annotationBarMessage)
        annotationBarImage = view.findViewById(R.id.annotationBarImage)

        annotationBarName.text = annotation.label
        annotationBarMessage.text = annotation.contents

        val a = AnnotationImageManager()
        // a.updateAnnotationBarImage(annotationBarImage, a.createGrayPlaceholderImage());

        return view
    }


}