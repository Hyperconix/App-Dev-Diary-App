package com.hyperconix.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.hyperconix.R
import com.hyperconix.data.DiaryEntry
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * This class represents a custom [RecyclerView.Adapter] which is used
 * for diary entries items which are displayed in the [ViewDiaryEntriesFragment]
 * portion of the application. This receives a list of type [DiaryEntry] which
 * will be provided from the content provider.
 *
 * @property diaryEntries The list of diary entries to recycle in the view
 *
 * @author Luke S
 */
class DiaryEntriesAdapter(private var diaryEntries: MutableList<DiaryEntry>, val itemOnClickListener: RecyclerViewOnItemClickListener) : RecyclerView.Adapter<DiaryEntriesAdapter.DiaryEntriesViewHolder>(){

    /**
     * This maintains a copy of the full list of diary entries. As [diaryEntries] can be searched
     * to filter the items. This means that [diaryEntries] will add and remove elements. This
     * list exists to preserve those elements. This is updated as part of [updateDiaryEntries]
     * to ensure new data changes are kept.
     */
    private val diaryEntriesFull: MutableList<DiaryEntry> = ArrayList(diaryEntries)


    /**
     * This class represents the ViewHolder for this
     * adapter. This is responsible for binding each
     * individual item in the diary entries list
     * to its data.
     *
     * @param itemView The view to be used with the ViewHolder
     *
     * @author 2727141
     */
    inner class DiaryEntriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        private val textViewEntryTitle: TextView = itemView.findViewById(R.id.textViewEntryTitle)

        private val textViewEntryDate: TextView = itemView.findViewById(R.id.textViewEntryDate)

        private val textViewEntryContent: TextView = itemView.findViewById(R.id.textViewEntryContent)

        private val imageViewEntryImage: ImageView = itemView.findViewById(R.id.imageViewEntryImage)

        init {
            itemView.setOnClickListener(this)
        }

        /**
         * This function binds the data from a [DiaryEntry]
         * to its corresponding view components for
         * this diary entry item being recycled in
         * the [RecyclerView]
         *
         * @param diaryEntry The diary entry that contains the data to bind
         */
        fun bindData(diaryEntry: DiaryEntry) {
            textViewEntryTitle.text = diaryEntry.title

            textViewEntryDate.text =  diaryEntry.date

            textViewEntryContent.text = diaryEntry.content

            val imageUriString = diaryEntry.attachedImagePath

            handleImage(imageUriString)

        }

        override fun onClick(view: View) {
           itemOnClickListener.onListItemClick(view, this.layoutPosition)
        }

        /**
         * This function will handle the image which may
         * be attached to a Diary Entry. It will parse
         * the URI and if it is not empty it will
         * set that URI. Otherwise the view will
         * be set to gone.
         *
         * @param imageUriString The uri string to be parsed
         */
        private fun handleImage(imageUriString: String) {

            val uri: Uri = Uri.parse(imageUriString)

            if(uri != Uri.EMPTY) {
                imageViewEntryImage.setImageURI(uri)

                imageViewEntryImage.visibility = View.VISIBLE
            }
            else {
                imageViewEntryImage.setImageURI(null)

                imageViewEntryImage.visibility = View.GONE
            }

        }

    }

    /**
     * Update the diary entries so that the RecycleView can
     * be refreshed and show the current entries that
     * are stored.
     *
     *
     * @param diaryEntries The new list of diary entries
     */
    fun updateDiaryEntries(diaryEntries: MutableList<DiaryEntry>) {
        this.diaryEntries = diaryEntries

        updateFullList()

        notifyDataSetChanged()
    }

    /**
     * This will sort diary entries in descending order
     * based on their title.
     *
     */
    fun sortDescending() {
        diaryEntries.sortByDescending { it.title }

        notifyDataSetChanged()
    }

    /**
     * This will sort diary entries in descending order
     * based on their date.
     *
     */
    fun sortDate() {
        val dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale.UK)

        diaryEntries.sortByDescending { dateFormat.parse(it.date) }

        notifyDataSetChanged()
    }

    /**
     * Revert to the original full list of diary entries.
     *
     */
    fun revertToOriginal() {
        diaryEntries.clear()

        diaryEntries.addAll(diaryEntriesFull)

        notifyDataSetChanged()
    }

    /**
     * This method will clear the [diaryEntries]
     * list, I.E remove all the contents and notify
     * that the item range has changed.
     *
     */
    fun clear() {
        val originalSize = diaryEntries.size

        diaryEntries.clear()

        notifyItemRangeRemoved(0, originalSize)

        updateFullList()
    }

    /**
     * This method will get a diary entry at a particular position
     *
     * @param position The position to get the item at
     *
     * @return The diary entry at the provided position
     */
    fun itemAt(position: Int): DiaryEntry {
        return diaryEntries[position]
    }

    /**
     * This will remove an item from diary entries
     * and then notify that an item has been removed
     * at the corresponding position
     *
     * @param position The position to remove the item at
     */
    fun removeItem(position: Int) {
        diaryEntries.removeAt(position)

        updateFullList()

        notifyItemRemoved(position)
    }

    /**
     * This will filter the diary entries using the [constraint]
     * provided.
     *
     * @param constraint The text to use as the filter
     */
    fun filter(constraint: String?) {
        val filteredList = ArrayList<DiaryEntry>()

        if(constraint.isNullOrEmpty()) {
            filteredList.addAll(diaryEntriesFull)
        }
        else {
            val pattern = constraint.lowercase().trim()

            for(diaryEntry in diaryEntriesFull) {
                val condition = diaryEntry.title.lowercase().contains(pattern)

                if(condition) {
                    filteredList.add(diaryEntry)
                }
            }
        }

        diaryEntries.clear()

        diaryEntries.addAll(filteredList)

        notifyDataSetChanged()
    }

    /**
     * This will filter the diary entries using the [constraint]
     * provided and apply the [sortDescending] function to the filtered entries.
     *
     * @param constraint The text to use as the filter
     */
    fun filterDescending(constraint: String?) {
        val filteredList = ArrayList<DiaryEntry>()

        if(constraint.isNullOrEmpty()) {
            filteredList.addAll(diaryEntriesFull)
        }
        else {
            val pattern = constraint.lowercase().trim()

            for(diaryEntry in diaryEntriesFull) {
                val condition = diaryEntry.title.lowercase().contains(pattern)

                if(condition) {
                    filteredList.add(diaryEntry)
                }
            }
        }

        diaryEntries.clear()

        diaryEntries.addAll(filteredList)

        sortDescending()
    }

    /**
     * This will filter the diary entries using the [constraint]
     * provided and apply the [sortDate] function to the filtered entries.
     *
     * @param constraint The text to use as the filter
     */
    fun filterDate(constraint: String?) {
        val filteredList = ArrayList<DiaryEntry>()

        if(constraint.isNullOrEmpty()) {
            filteredList.addAll(diaryEntriesFull)
        }
        else {
            val pattern = constraint.lowercase().trim()

            for(diaryEntry in diaryEntriesFull) {
                val condition = diaryEntry.title.lowercase().contains(pattern)

                if(condition) {
                    filteredList.add(diaryEntry)
                }
            }
        }

        diaryEntries.clear()

        diaryEntries.addAll(filteredList)

        sortDate()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryEntriesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.diary_entry_item_layout, parent, false)

        return DiaryEntriesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DiaryEntriesViewHolder, position: Int) {
        holder.bindData(diaryEntries[position])
    }

    override fun getItemCount() = diaryEntries.size

    /**
     * Update the full list of diary entries
     * after an item has been added or removed.
     *
     */
    private fun updateFullList() {
        diaryEntriesFull.clear()

        diaryEntriesFull.addAll(diaryEntries)
    }
    
}