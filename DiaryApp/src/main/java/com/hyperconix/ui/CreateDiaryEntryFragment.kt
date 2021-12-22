package com.hyperconix.ui


import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hyperconix.R
import com.hyperconix.data.DiaryEntry
import com.hyperconix.db.DatabaseHelper

import com.hyperconix.utils.ImageSaveHandler


/**
 * This class defines the Create Diary Entry fragment,
 * this fragment will be responsible for the second tab
 * in the application. This will be where the user will
 * enter the information for a diary entry and be able
 * to insert it.
 *
 * @author Luke S
 */
class CreateDiaryEntryFragment : Fragment(R.layout.create_diary_entry_fragment) {

    /**
     * This represents the animation which is used when [fabDisplayOptions] is clicked and collapsed
     */
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.anim_rotate_open) }

    /**
     * This represents the animation which is used when [fabDisplayOptions] is clicked and expanded
     */
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.anim_rotate_close) }

    /**
     * This represents the animation which is used when [fabCreateEntry], [fabAttachImage] and
     * [fabClearFields] are expanded
     */
    private val expandOptions: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.anim_expand_options) }

    /**
     * This represents the animation which is used when [fabCreateEntry], [fabAttachImage] and
     * [fabClearFields] are collapsed
     */
    private val collapseOptions: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.anim_collapse_options) }

    /**
     * This registers an activity result contract and assigns it to the GetContent contract which
     * will open the files or gallery. This must be declared before onCreate as stated in
     * the dev docs.
     *
     * See https://developer.android.com/training/basics/intents/result
     */
    private val getImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if(uri != null) {
                // We set the uri on the attached image to the one received by the callback
                imageViewAttachedImage.setImageURI(uri)

                // Set the tag to the string representation of the uri path
                imageViewAttachedImage.tag = uri.toString()

                // Default visibility is gone, so we set it to visible after we've set the uri
                imageViewAttachedImage.visibility = View.VISIBLE

                context?.let { imageSaver.saveImage(imageViewAttachedImage, it) }
            }
    }

    /**
     * This represents the custom image save handler which is responsible
     * for saving the attached image to the internal storage
     * for use later.
     */
    private val imageSaver: ImageSaveHandler = ImageSaveHandler()

    /**
     * This represents the text view component which displays the date
     * that was picked by the user.
     */
    private lateinit var textViewDateDisplay: TextView

    /**
     * This represents the edit text component which allows the user to
     * enter the title of their entry.
     */
    private lateinit var editTextDiaryEntryTitle: EditText

    /**
     * This represents the edit text component which allows the user to
     * enter the content of their entry.
     */
    private lateinit var editTextDiaryEntryContent: EditText

    /**
     * This represents the image view component which will display the attached image when it is
     * picked from the users gallery.
     */
    private lateinit var imageViewAttachedImage: ImageView

    /**
     * This represents the floating action button which expands the other option buttons to the user.
     */
    private lateinit var fabDisplayOptions: FloatingActionButton

    /**
     * This represents the floating action button which attempts to create the entry.
     */
    private lateinit var fabCreateEntry: FloatingActionButton

    /**
     * This represents the floating action button which allows the user to attach an image.
     */
    private lateinit var fabAttachImage: FloatingActionButton

    /**
     * This represents the floating action button which allows to user to clear the title and content
     * fields.
     */
    private lateinit var fabClearFields: FloatingActionButton

    /**
     * This flag indicates whether or not the main floating action button was clicked,
     * expanding the other buttons.
     */
    private var actionButtonsExpanded = false

    /**
     * This represents the database helper object, that will allow for access to CRUD operations
     * on the database.
     */
    private lateinit var databaseHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewDateDisplay = view.findViewById(R.id.textViewDateDisplay)

        editTextDiaryEntryTitle = view.findViewById(R.id.editTextDiaryEntryTitle)

        editTextDiaryEntryContent = view.findViewById(R.id.editTextDiaryEntryContent)

        fabDisplayOptions = view.findViewById(R.id.fabDisplayActions)

        fabCreateEntry = view.findViewById(R.id.fabCreateEntry)

        fabAttachImage = view.findViewById(R.id.fabAttachImage)

        fabClearFields = view.findViewById(R.id.fabClearFields)

        imageViewAttachedImage = view.findViewById(R.id.imageViewAttachedImage)

        imageViewAttachedImage.tag = ""

        databaseHelper = DatabaseHelper(context)

        // Using Fragment Result API, listen for the date being passed from the Select Diary Fragment and update text
        parentFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            textViewDateDisplay.text = bundle.getString("calendarDate")
        }

        fabDisplayOptions.setOnClickListener {
            handleActionButtonDisplay()
        }

        fabCreateEntry.setOnClickListener() {
            if(isValidInput()) {
            createEntry()

            parentFragmentManager.setFragmentResult("dataChanged", bundleOf())

            clearFields()

             }
            else {
                Toast.makeText(context, "Error: Please check you filled in both fields", Toast.LENGTH_SHORT).show()
            }

        }

        fabAttachImage.setOnClickListener {
            attachImage()
        }

        fabClearFields.setOnClickListener {
            clearFields()
        }

    }

    /**
     * This function is responsible for creating
     * the diary entry and storing it in the system
     * so that it can be viewed in the future. This will
     * be called whenever the "Create Entry" button is
     * clicked.
     *
     *
     */
    private fun createEntry() {
        val title = editTextDiaryEntryTitle.text.toString()

        val date = textViewDateDisplay.text.toString()

        val content = editTextDiaryEntryContent.text.toString()

        val imagePath = imageSaver.imagePath

        val diaryEntry = DiaryEntry(title, date, imagePath, content)

        databaseHelper.addDiaryEntry(diaryEntry)
    }

    /**
     * This function is responsible for opening
     * the gallery or files and giving the option
     * to attach an image to the entry. This will
     * handle inserting that image should user choose
     * to attach it. This will be called whenever the
     * "Attach Image" button is clicked.
     *
     */
    private fun attachImage() = getImageFromGalleryResult.launch("image/*")


    /**
     * This function is responsible for clearing the entry title
     * and content fields. This includes clearing the
     * attached image. This will be called whenever
     * the "Clear Fields" button is clicked.
     *
     */
    private fun clearFields() {
        editTextDiaryEntryContent.setText("")

        editTextDiaryEntryTitle.setText("")

        imageViewAttachedImage.setImageURI(null)

        imageViewAttachedImage.tag = ""

        imageViewAttachedImage.visibility = View.GONE

        imageSaver.imagePath = ""
    }

    /**
     * This function is responsible for
     * handling whether to expand and collapsing
     * of the action buttons. This is determined by the
     * [actionButtonsExpanded] flag. It will decided
     * whether to call [actionButtonsExpand] or [actionButtonsCollapse]
     * based on this flag. This will be called when the
     * "Display Options" button is clicked.
     *
     *
     */
    private fun handleActionButtonDisplay() {
        if(!actionButtonsExpanded) {
            actionButtonsExpand()
        }
        else {
            actionButtonsCollapse()
        }

    }

    /**
     * This function is responsible for applying all necessary
     * logic to expand the action buttons and
     * start their animations.
     *
     *
     */
    private fun actionButtonsExpand() {
        fabCreateEntry.visibility = View.VISIBLE

        fabAttachImage.visibility = View.VISIBLE

        fabClearFields.visibility = View.VISIBLE

        fabCreateEntry.isClickable = true

        fabAttachImage.isClickable = true

        fabClearFields.isClickable = true

        fabCreateEntry.startAnimation(expandOptions)

        fabAttachImage.startAnimation(expandOptions)

        fabClearFields.startAnimation(expandOptions)

        fabDisplayOptions.startAnimation(rotateOpen)

        actionButtonsExpanded = true
    }

    /**
     * This function is responsible for applying all necessary
     * logic to collapse the action buttons and
     * start their animations.
     *
     *
     */
    private fun actionButtonsCollapse() {
        fabCreateEntry.visibility = View.INVISIBLE

        fabAttachImage.visibility = View.INVISIBLE

        fabClearFields.visibility = View.INVISIBLE

        fabCreateEntry.isClickable = false

        fabAttachImage.isClickable = false

        fabClearFields.isClickable = false

        fabCreateEntry.startAnimation(collapseOptions)

        fabAttachImage.startAnimation(collapseOptions)

        fabClearFields.startAnimation(collapseOptions)

        fabDisplayOptions.startAnimation(rotateClose)

        actionButtonsExpanded = false
    }

    /**
     * This function is responsible for checking
     * if the title and content edit text components
     * have a value, if they are empty then the
     * input is considered invalid and this
     * will return false. It will additionally
     * mark the appropriate fields with errors. Otherwise,
     * this will return true.
     *
     * @return true if both inputs have a value, false otherwise
     */
    private fun isValidInput() : Boolean {
        val entryTitle = editTextDiaryEntryTitle.text.toString().trim()

        val entryContent = editTextDiaryEntryContent.text.toString().trim()

        if(entryTitle.isEmpty()) {
            editTextDiaryEntryTitle.error = "The diary entry title cannot be empty"
        }

        if(entryContent.isEmpty()) {
            editTextDiaryEntryContent.error = "The diary entry content cannot be empty"
        }

        return entryTitle.isNotEmpty() && entryContent.isNotEmpty()
    }

}