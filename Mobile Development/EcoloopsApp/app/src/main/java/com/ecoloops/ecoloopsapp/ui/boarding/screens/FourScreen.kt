package com.ecoloops.ecoloopsapp.ui.boarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.ecoloops.ecoloopsapp.R


class FourScreen : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_four_screen, container, false)
        val finish = view.findViewById<TextView>(R.id.tvFinish)

        finish.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginActivity)
            onBoardingIsFinished()
        }

        return view
    }

    private fun onBoardingIsFinished(){

        val sharedPreferences = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("finished",true)
        editor.apply()
    }


}