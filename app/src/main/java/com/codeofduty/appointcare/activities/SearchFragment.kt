import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.activities.SearchAdapter
import com.codeofduty.appointcare.activities.SearchData
import com.codeofduty.appointcare.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: SearchAdapter
    private var mList = ArrayList<SearchData>()
    private lateinit var topDoctorsLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize searchView first
        searchView = view.findViewById(R.id.searchView)
        val searchText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        searchText.setHintTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))

        recyclerView = view.findViewById(R.id.recyclerViewSearch)
        searchView = view.findViewById(R.id.searchView)
        topDoctorsLayout = view.findViewById(R.id.linear_layout_top_doctors)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(mList)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                // Hide top doctors layout with fade animation when search view is active
                val isSearchViewActive = newText?.isNotEmpty() ?: false
                if (isSearchViewActive) {
                    val fadeOut = AlphaAnimation(1f, 0f)
                    fadeOut.duration = 300 // Adjust the duration as needed
                    topDoctorsLayout.startAnimation(fadeOut)
                    topDoctorsLayout.visibility = View.GONE
                } else {
                    val fadeIn = AlphaAnimation(0f, 1f)
                    fadeIn.duration = 300 // Adjust the duration as needed
                    topDoctorsLayout.startAnimation(fadeIn)
                    topDoctorsLayout.visibility = View.VISIBLE
                }
                return true
            }

        })

        searchView.setOnCloseListener {
            recyclerView.visibility = View.GONE // Hide RecyclerView when search bar is cleared
            false
        }

        fetchData()

        return view
    }

    private fun fetchData() {
        val service = RetrofitClient.getService()
        val call = service.getUsersWithDoctorRole()

        call.enqueue(object : Callback<List<DoctorUsers>> {
            override fun onResponse(call: Call<List<DoctorUsers>>, response: Response<List<DoctorUsers>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        populateList(users)
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<DoctorUsers>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun populateList(users: List<DoctorUsers>) {
        val doctorUsers = users.filter { it.role == "Doctor" && it.status == "Accepted" }
        for (user in doctorUsers) {
            mList.add(
                SearchData(
                    "${user.Fname} ${user.Lname}",
                    "Speciality: ${user.specialty}",
                    R.drawable.doctor_profile,
                    "Phone: ${user.number}",
                    "Email: ${user.email}",
                    "${user.hn} ${user.barangay} , ${user.municipality} ${user.province}",
                    "Consultation Price: ${user.consultPrice}"
                )
            )
        }
        adapter.notifyDataSetChanged()
    }

    private fun filterList(query: String?) {
        if (query != null && query.isNotEmpty()) {
            val filteredList = ArrayList<SearchData>()
            for (i in mList) {
                if (i.title.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No Data found", Toast.LENGTH_SHORT).show()
                recyclerView.visibility = View.GONE // Hide RecyclerView if no data found
            } else {
                adapter.setFilteredList(filteredList)
                recyclerView.visibility = View.VISIBLE // Show RecyclerView if data found
            }
        } else {
            recyclerView.visibility = View.GONE // Hide RecyclerView when search query is empty
        }
    }
}
