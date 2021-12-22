package com.hyperconix.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * This class is responsible for encapsulating
 * the logic required to save an image to the
 * internal storage of the device.
 *
 * @author Luke S
 */
class ImageSaveHandler {

    /**
     * This is responsible for storing the image path
     * of the current image being prepared to be
     * saved. If there is no image, this should be
     * set to an empty string.
     */
    var imagePath = ""

    /**
     * This function is responsible for saving the image to
     * the internal storage. It requires both the image view
     * to fetch the drawable and the context.
     *
     * @param imageViewAttachedImage The attached image view to get the drawable from
     * @param context The context to act on
     */
    fun saveImage(imageViewAttachedImage: ImageView, context: Context) {
        val imageAsDrawable = imageViewAttachedImage.drawable

        // Setting to arbitrary height for now, this may stretch some images
        val bitmap = imageAsDrawable.toBitmap(2000, 2000)

        val contextWrapper = ContextWrapper(context)

        val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)

        val file = File(directory, "AttachedImage" + ".jpg")

        Log.d("path", file.toString())

        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)

            fos.flush()

            fos.close()
        } catch (e: IOException) {

            e.printStackTrace()
        }

        imagePath = file.toString()
    }

}