package com.codeofduty.appointcare.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.codeofduty.appointcare.R

class AboutUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)

        // Find TextView elements
        val tvDev1 = view.findViewById<TextView>(R.id.tv_dev_1)
        val tvDev2 = view.findViewById<TextView>(R.id.tv_dev_2)
        val tvDev3 = view.findViewById<TextView>(R.id.tv_dev_3)
        val tvDev4 = view.findViewById<TextView>(R.id.tv_dev_4)
        val tvDev5 = view.findViewById<TextView>(R.id.tv_dev_5)

        // Set OnClickListener for each TextView
        setOnClickListenerForLink(tvDev1, "https://example.com/dev_1")
        setOnClickListenerForLink(tvDev2, "https://example.com/dev_2")
        setOnClickListenerForLink(tvDev3, "https://example.com/dev_3")
        setOnClickListenerForLink(tvDev4, "https://example.com/dev_4")
        setOnClickListenerForLink(tvDev5, "https://example.com/dev_5")

        return view
    }

    private fun setOnClickListenerForLink(textView: TextView, url: String) {
        textView.setOnClickListener {
            // Open the link in a web browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}
