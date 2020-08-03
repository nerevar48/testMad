package ru.test.livanov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_REPO_OWNER_TO_DETAIL = "repo_owner_to_detail"
        const val KEY_REPO_NAME_TO_DETAIL = "repo_name_to_detail"
    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.injectsMainActivity(this)

        navController = Navigation.findNavController(this, R.id.navFragment)

        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> navController.navigate(R.id.mainListFragment, null, navOptions)
                    1 -> navController.navigate(R.id.secondListFragment, null, navOptions)
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }
        })
    }


}