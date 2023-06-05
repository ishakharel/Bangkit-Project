package com.ecoloops.ecoloopsapp.ui.boarding.screens

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.databinding.ActivityRegisterBinding
import com.ecoloops.ecoloopsapp.databinding.FragmentFirstScreenBinding


class FirstScreen : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_first_screen, container, false)

        val next = view.findViewById<TextView>(R.id.tvNext1)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager)
        next.setOnClickListener {
            viewPager?.currentItem = 1
        }
        return view
    }
}