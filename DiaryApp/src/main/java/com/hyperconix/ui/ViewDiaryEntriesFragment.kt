package com.hyperconix.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.hyperconix.R
import com.hyperconix.db.DatabaseHelper

/**
 * This class defines the View Diary Entries fragment,
 * this fragment will be responsible for the third tab
 * in the application. This will be where the user will
 * able to view previously inserted diary entries.
 *
 * @author Luke S
 */
class ViewDiaryEntriesFragment : Fragment(R.layout.view_diary_entries_fragment), RecyclerViewOnItemClickListener {

    /**
     * This represents the diary entries custom [RecyclerView.Adapter] that will be used
     * to help display entries in the [RecyclerView].
     */
    private lateinit var diaryEntriesAdapter: DiaryEntriesAdapter

    /**
     * This represents the database helper object, that will allow for access to CRUD operations
     * on the database.
     */
    private lateinit var databaseHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycleViewDiaryEntries = view.findViewById<RecyclerView>(R.id.recyclerViewDiaryEntries)

        val searchViewDiaryEntries = view.findViewById<SearchView>(R.id.searchViewDiaryEntries)

        val radioGroupSortOptions = view.findViewById<RadioGroup>(R.id.radioGroupSortOptions)

        val buttonDelete = view.findViewById<Button>(R.id.buttonDelete)

        databaseHelper = DatabaseHelper(context)

        diaryEntriesAdapter = DiaryEntriesAdapter(databaseHelper.getDiaryEntries(), this)

        recycleViewDiaryEntries.adapter = diaryEntriesAdapter

        recycleViewDiaryEntries.layoutManager = LinearLayoutManager(context)

        // Listen for the data being changed and then update the diary entries adapter
        parentFragmentManager.setFragmentResultListener("dataChanged", this) { _, _ ->
            diaryEntriesAdapter.updateDiaryEntries(databaseHelper.getDiaryEntries())
        }

        val spanCount = getResponsiveSpanCount()

        recycleViewDiaryEntries.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)

        buttonDelete.setOnClickListener() {
            if(diaryEntriesAdapter.itemCount != 0) {
                delete()
            }
        }

        radioGroupSortOptions.setOnCheckedChangeListener { _, checkedId ->
            if(!searchViewDiaryEntries.hasFocus() && diaryEntriesAdapter.itemCount > 1) {
                applyStandaloneSort(checkedId)
            }
        }

        searchViewDiaryEntries.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus && diaryEntriesAdapter.itemCount > 1) {
                applyStandaloneSort(radioGroupSortOptions.checkedRadioButtonId)
            }
        }

        searchViewDiaryEntries.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                // We do not apply any sorting when the text changes so we call the basic filter method
                diaryEntriesAdapter.filter(newText)

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // When submitting, we call search and pass the currently checked radio button id so it can determine what sort to use (if any)
                search(query, radioGroupSortOptions.checkedRadioButtonId)

                return false
            }

        })

        }

    override fun onListItemClick(view: View, position: Int) {

        val alertDialog = context?.let { AlertDialog.Builder(it) }

        alertDialog?.apply {
            setTitle("Delete Diary Entries")

            setMessage("WARNING: Are you sure you want to delete this item?")

            setPositiveButton("Yes") { _, _ ->

                val selectedItem = diaryEntriesAdapter.itemAt(position)

                val selectedItemId = selectedItem.id

                databaseHelper.deleteByID(selectedItemId)

                diaryEntriesAdapter.removeItem(position)
            }

            setNegativeButton("No") { _, _ ->
            }
        }?.create()?.show()

    }

    /**
     * This function is responsible for clearing/deleting all
     * entries that are currently stored in the database. As
     * this is a destructive operation, it will first ask
     * the user for confirmation and only delete with
     * a positive response. This is called whenever the
     * "Delete Entries" button is clicked.
     *
     */
    private fun delete() {
        val alertDialog = context?.let { AlertDialog.Builder(it) }

        alertDialog?.apply {
            setTitle("Delete Diary Entries")

            setMessage("WARNING: This will delete ALL diary entries. Are you sure?")

            setPositiveButton("Yes") { _, _ ->
                databaseHelper.deleteAllEntries()

                diaryEntriesAdapter.clear()
            }

            setNegativeButton("No") { _, _ ->
            }
        }?.create()?.show()
    }

    /**
     * This determines the search logic to use for the SearchView provided in this
     * fragment for diary entries. It uses the [constraint] which is
     * provided when the user types in the search box to filter
     * the entries correctly. This will use [radioButtonCheckedId] to determine
     * whether to sort the filtered items or not.
     *
     * @param constraint The text used as the constraint for the filter
     * @param radioButtonCheckedId The currently checked Radio Button Id
     */
    private fun search(constraint: String, radioButtonCheckedId: Int) {
        when (radioButtonCheckedId) {
            R.id.radioButtonDate -> {
                diaryEntriesAdapter.filterDate(constraint)
            }
            R.id.radioButtonZtoA -> {
                diaryEntriesAdapter.filterDescending(constraint)
            }
            R.id.radioButtonNone -> {
                diaryEntriesAdapter.filter(constraint)
            }
        }
    }

    /**
     * This will apply a standalone sort to list of diary entries, standalone
     * meaning that the user is currently not searching the list and is simply
     * toggling between the sort options.
     *
     * @param radioButtonCheckedId The currently checked Radio Button Id
     */
    private fun applyStandaloneSort(radioButtonCheckedId: Int) {
        when (radioButtonCheckedId) {
            R.id.radioButtonDate -> {
                diaryEntriesAdapter.sortDate()
            }
            R.id.radioButtonZtoA -> {
                diaryEntriesAdapter.sortDescending()
            }
            R.id.radioButtonNone -> {
                diaryEntriesAdapter.revertToOriginal()
            }
        }
    }

    /**
     * This function is responsible for retrieving
     * an appropriate span count which is used
     * to determine how many columns are
     * displayed in the Recycler Views scattered
     * grid layout.
     *
     * @return An appropriate span count based on the smallest screen width
     */
    private fun getResponsiveSpanCount(): Int {

        var spanCount = 2

        val config = resources.configuration

        val smallestScreenWidthDp = config.smallestScreenWidthDp

        if(smallestScreenWidthDp >= 600) {
            spanCount = 3
        }

        return spanCount
    }

}