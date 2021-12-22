package com.hyperconix.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * This class defines the fragment adapter which will be used for managing
 * the three fragments which are used in this project for the user interface
 * control. This will be used the ViewPager2 to handle switching between tabs
 * in the application.
 *
 * @property mNumOfTabs The total number of tabs in the adapter
 *
 * @param fragmentManager The fragment manager to be used for this fragment adapter
 * @param lifecycle The lifecycle to be used for this fragment adapter
 *
 * @author Luke S
 */
internal class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val mNumOfTabs: Int) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SelectDiaryDateFragment()
            1 -> CreateDiaryEntryFragment()
            2 -> ViewDiaryEntriesFragment()
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int {
        return mNumOfTabs
    }
    
}