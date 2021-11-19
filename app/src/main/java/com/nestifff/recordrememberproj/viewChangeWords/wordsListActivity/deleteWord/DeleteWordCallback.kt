package com.nestifff.recordrememberproj.viewChangeWords.wordsListActivity.deleteWord

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nestifff.recordrememberproj.R
import com.nestifff.recordrememberproj.viewChangeWords.wordsListActivity.WordsListAdapter


class DeleteWordCallback(
    private val adapter: WordsListAdapter
) : ItemTouchHelper.SimpleCallback(0, (ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)) {

    private var icon: Drawable? = ContextCompat.getDrawable(
        adapter.context,
        R.drawable.delete_white_image
    )
    @SuppressLint("UseCompatLoadingForDrawables")
    private var background: Drawable = adapter.context.getDrawable(R.drawable.delete_rounded_corners)!!

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteWord(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 60

        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon!!.intrinsicHeight) / 2
        val iconBottom = iconTop + icon!!.intrinsicHeight

        when {
            dX > 0 -> { // Swiping to the right
                val iconLeft = itemView.left + iconMargin + icon!!.intrinsicWidth
                val iconRight = itemView.left + iconMargin
                icon!!.setBounds(iconRight, iconTop, iconLeft, iconBottom)
                background.setBounds(
                    itemView.left, itemView.top,
                    itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
                )
            }
            dX < 0 -> { // Swiping to the left
                val iconLeft = itemView.right - iconMargin - icon!!.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
            }
            else -> { // view is unSwiped
                background.setBounds(0, 0, 0, 0)
            }
        }

        background.draw(c)
        icon!!.draw(c)
        super.onChildDraw(
            c, recyclerView, viewHolder, dX,
            dY, actionState, isCurrentlyActive
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false
}