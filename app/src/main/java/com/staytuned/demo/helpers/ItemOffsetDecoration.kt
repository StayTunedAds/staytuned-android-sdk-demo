package com.staytuned.demo.helpers

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class ItemOffsetDecoration(private val mItemOffset: Int, private val adapterType: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            if (adapterType == 1) {
                outRect.set(mItemOffset, mItemOffset / 2, mItemOffset / 2, mItemOffset / 2)
            } else {
                outRect.set(mItemOffset, mItemOffset / 2, mItemOffset / 2, mItemOffset * 2)
            }
        } else {
            if (adapterType == 1) {
                outRect.set(mItemOffset / 2, mItemOffset / 2, mItemOffset, mItemOffset / 2)
            } else {
                outRect.set(mItemOffset / 2, mItemOffset / 2, mItemOffset, mItemOffset * 2)

            }
        }
    }
}
