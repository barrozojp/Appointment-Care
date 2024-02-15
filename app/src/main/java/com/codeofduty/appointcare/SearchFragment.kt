package com.codeofduty.appointcare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<SearchData>()
    private lateinit var adapter: SearchAdapter
    private lateinit var topDoctorsLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewSearch)
        searchView = view.findViewById(R.id.searchView)
        topDoctorsLayout = view.findViewById(R.id.linear_layout_top_doctors)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        addDataToList()
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

        return view
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

    private fun addDataToList() {
        mList.add(
            SearchData(
                "Dr.Java",
                R.drawable.baseline_account_circle_24,
                "Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible."
            )
        )
        mList.add(
            SearchData(
                "Dr.Kotlin",
                R.drawable.baseline_account_circle_24,
                "Kotlin is a cross-platform, statically typed, general-purpose programming language with type inference. Kotlin is designed to interoperate fully with Java, and the JVM version of Kotlin's standard library depends on the Java Class Library, but type inference allows its syntax to be more concise."
            )
        )
        mList.add(
            SearchData(
                "Dr.HTML",
                R.drawable.baseline_account_circle_24,
                "The HyperText Markup Language or HTML is the standard markup language for documents designed to be displayed in a web browser."
            )
        )
        mList.add(
            SearchData(
                "Dr.Python",
                R.drawable.baseline_account_circle_24,
                "Python is a high-level, general-purpose programming language. Its design philosophy emphasizes code readability with the use of significant indentation."
            )
        )
        mList.add(
            SearchData(
                "Dr.C++",
                R.drawable.baseline_account_circle_24,
                "C++ is a high-level general-purpose programming language created by Danish computer scientist Bjarne Stroustrup as an extension of the C programming language, or C with Classes."

            )
        )
        mList.add(
            SearchData(
                "Dr.Swift",
                R.drawable.baseline_account_circle_24,
                "Swift is a general-purpose, multi-paradigm, compiled programming language developed by Apple Inc. and the open-source community."
            )
        )
        mList.add(
            SearchData(
                "Dr.JavaScript",
                R.drawable.baseline_account_circle_24,
                "JavaScript, often abbreviated as JS, is a programming language that is one of the core technologies of the World Wide Web, alongside HTML and CSS. As of 2022, 98% of websites use JavaScript on the client side for webpage behavior, often incorporating third-party libraries."
            )
        )
        mList.add(
            SearchData(
                "Dr.C#",
                R.drawable.baseline_account_circle_24,
                "C# is a general-purpose, high-level multi-paradigm programming language. C# encompasses static typing, strong typing, lexically scoped, imperative, declarative, functional, generic, object-oriented, and component-oriented programming disciplines."
            )
        )
    }
}
