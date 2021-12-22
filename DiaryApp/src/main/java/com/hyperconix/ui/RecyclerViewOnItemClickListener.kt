package com.hyperconix.ui

import android.view.View

/**
 * This is a custom interface which is used
 * to define a click listener for the items
 * which are in the RecyclerView. This
 * does not exist by default, so this extends
 * that functionality.
 *
 * @author Luke S
 */
interface RecyclerViewOnItemClickListener {

    /**
     * This function will be called whenever
     * an item inside a RecyclerView is
     * clicked. The view and position of the
     * clicked item are to be provided.
     *
     * @param view The view of the item
     * @param position The position of the item
     */
    fun onListItemClick(view: View, position: Int)
}