package com.codeofduty.appointcare.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codeofduty.appointcare.databinding.FragmentMyConsultationBinding

class MyConsultationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using view binding
        val binding = FragmentMyConsultationBinding.inflate(inflater, container, false)


        return binding.root
    }



}
