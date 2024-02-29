package com.codeofduty.appointcare.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.databinding.FragmentMyBookingsBinding

class MyBookingsFragment : Fragment() {

    private val locations = arrayOf("Select Location: ↓ ", "San Fabian", "Dagupan", "San Carlos", "Calasiao")
    private val times = arrayOf("Select Time: ", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using view binding
        val binding = FragmentMyBookingsBinding.inflate(inflater, container, false)

        // Set up the Spinner with location choices
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, locations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.locationSpinner.adapter = adapter

        // Set up the Spinner with time choices
        val timeAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, times)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.timeSpinner.adapter = timeAdapter

        return binding.root
    }



}
