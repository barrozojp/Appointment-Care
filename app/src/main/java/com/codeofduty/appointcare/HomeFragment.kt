import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.ServicesFragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        val visitWebTextView: TextView = view.findViewById(R.id.visitWEB)

        // Set OnClickListener to navigate to the website
        visitWebTextView.setOnClickListener {
            // Define the URL
            val url = "https://appointcare-web.netlify.app/"

            // Create an intent to open the URL in a browser
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            // Start the intent
            startActivity(intent)
        }

        val learnMoreButton: Button = view.findViewById(R.id.btn_learnMoreServices)

        // Set OnClickListener to navigate to ServicesFragment
        learnMoreButton.setOnClickListener {
            // Navigate to ServicesFragment
            val fragment = ServicesFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return view
    }
}
