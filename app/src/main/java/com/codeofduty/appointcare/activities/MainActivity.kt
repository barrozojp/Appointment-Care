package com.codeofduty.appointcare.activities

import SearchFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottom_home -> openFragment(HomeFragment())
                R.id.bottom_bookings_dr -> openFragment(DoctorAcceptRejectFragment())
                R.id.bottom_patients_dr -> openFragment(DoctorsPagePatientFragment())
                R.id.bottom_rejected -> openFragment(DoctorRejectedPatientsFragment())
                R.id.bottom_profile -> openFragment(ProfileFragment())
            }

            true
        }
        fragmentManager = supportFragmentManager
        openFragment(HomeFragment())


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_profile -> openFragment(ProfileFragment())
            R.id.nav_services -> openFragment(MyBookingsFragment())
            R.id.nav_search -> openFragment(SearchFragment())
            R.id.nav_faqs -> openFragment(FAQsFragment())
            R.id.nav_contact -> openFragment(ContactFragment())
            R.id.nav_aboutus -> openFragment(AboutUsFragment())
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        // Do nothing
    }


    private fun openFragment(fragment: Fragment){

        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()

    }

}