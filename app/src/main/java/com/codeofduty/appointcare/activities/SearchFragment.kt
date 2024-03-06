import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.activities.AppointmentClickListener
import com.codeofduty.appointcare.activities.MakeAppointment
import com.codeofduty.appointcare.activities.SearchAdapter
import com.codeofduty.appointcare.activities.SearchData
import com.codeofduty.appointcare.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SearchFragment : Fragment(), AppointmentClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: SearchAdapter
    private lateinit var loadingCARD: CardView
    private var mList = ArrayList<SearchData>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize and set the adapter with the click listener
        adapter = SearchAdapter(mList, this) // Pass 'this' as the AppointmentClickListener
        recyclerView.adapter = adapter
    }

    override fun onAppointmentClick(_id: String) {
        val makeAppointmentFragment = MakeAppointment.newInstance(_id)

        // Navigate to the MakeAppointment fragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, makeAppointmentFragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        loadingCARD = view.findViewById(R.id.loadingCARD)
        // Initialize searchView first
        searchView = view.findViewById(R.id.searchView)
        val searchText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        searchText.setHintTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))

        recyclerView = view.findViewById(R.id.recyclerViewSearch)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(mList, this)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        fetchData()

        return view
    }

    private fun fetchData() {
        val service = RetrofitClient.getService()
        val call = service.getUsersWithDoctorRole()

        call.enqueue(object : Callback<List<DoctorUsers>> {
            override fun onResponse(call: Call<List<DoctorUsers>>, response: Response<List<DoctorUsers>>) {
                if (response.isSuccessful) {
                    loadingCARD.visibility = View.GONE
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
                Toast.makeText(requireContext(), "Failed to fetch data: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun populateList(users: List<DoctorUsers>) {
        val doctorUsers = users.filter { it.role == "Doctor" && it.status == "Accepted" }
        for (user in doctorUsers) {
            mList.add(
                SearchData(
                    "${user.Fname} ${user.Lname}",
                    "${user._id}",
                    "Speciality: ${user.specialty}",
                    user.imageData,
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
            val filteredList = mList.filter { it.title.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT)) }
            adapter.setFilteredList(filteredList)
        } else {
            adapter.setFilteredList(mList)
        }
    }

}
