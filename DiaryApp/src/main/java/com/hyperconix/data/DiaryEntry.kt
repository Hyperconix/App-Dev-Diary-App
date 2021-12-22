package com.hyperconix.data


/**
 * This class models a Diary Entry in the application.
 * The purpose of this class is to be used a data object
 * or wrapper for representing what is stored inside a
 * diary entry. This is useful for both UI list adapters to represent
 * the content and the content provider itself to bundle
 * stored data into a common object.
 *
 * @property title The title of the diary entry
 * @property date The date selected for the diary entry
 * @property attachedImagePath The uri (as a string) of the diary entries attached image
 * @property content The content of the diary entry
 *
 * @constructor Create an instance of the diary entry data object
 *
 * @author Luke S
 */
data class DiaryEntry(val title: String, val date: String, val attachedImagePath: String, val content: String) {
    /**
     * The unique identifier of the diary entry
     */
    var id = 0

}