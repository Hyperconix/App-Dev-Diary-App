package com.hyperconix.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.tabs.TabLayout
import com.hyperconix.R

/**
 * This class defines the main activity, which is
 * the entry point of this application. This will
 * display - using fragments - the first tab of the
 * application.
 *
 * @author Luke S
 */
class MainActivity : AppCompatActivity() {

    /**
     * This represents a reference to the ViewPager2 which is used to cycle through tabs and
     * their corresponding fragments. This has been declared as a property to allow support
     * for back pressing.
     */
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_main)

            val toolbar = findViewById<Toolbar>(R.id.toolbar)

            setSupportActionBar(toolbar)

            val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

            tabLayout.addTab(tabLayout.newTab().setText(R.string.select_diary_date_tab))

            tabLayout.addTab(tabLayout.newTab().setText(R.string.create_diary_entry_tab))

            tabLayout.addTab(tabLayout.newTab().setText(R.string.view_diary_entries_tab))

            tabLayout.tabGravity = TabLayout.GRAVITY_FILL

            viewPager = findViewById(R.id.pager)

            val adapter = FragmentAdapter(supportFragmentManager, lifecycle, tabLayout.tabCount)

            viewPager.adapter = adapter

                tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        viewPager.currentItem = tab.position
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {}

                    override fun onTabReselected(tab: TabLayout.Tab) {}
                })

                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        // This switch will persist across all fragments, therefore it is defined here in MainActivity
        val switchLightMode = findViewById<SwitchCompat>(R.id.switchLightMode)

        // This is simply to override device default night mode. We start in night mode regardless of the device default.
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        switchLightMode.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    /**
     * This function overrides the default onBackPressed function
     * to allow the viewpager to handle the case where
     * the back button is pressed. This will cycle
     * to the previous tab or if the current tab is
     * the first tab, it will pop the back stack
     * as normal and call finish().
     *
     */
    override fun onBackPressed() {
        if(viewPager.currentItem == 0) {
            super.onBackPressed()
        }
        else
        {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

}