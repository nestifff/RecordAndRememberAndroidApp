package com.nestifff.recordrememberproj.views.viewWords.recyclerView

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nestifff.recordrememberproj.R
import com.nestifff.recordrememberproj.model.word.Word
import com.nestifff.recordrememberproj.views.viewWords.changeWord.ChangeWordClickHandler

class WordsListViewHolder(
    view: View,
) : RecyclerView.ViewHolder(view) {

    var rusTextView: TextView = itemView.findViewById(R.id.textView_viewRusWord)
    var engTextView: TextView = itemView.findViewById(R.id.textView_viewEngWord)
    var blockChangeWord: LinearLayout = itemView.findViewById(R.id.ll_changeWord)

    var word: Word? = null

    var changeWordHandler: ChangeWordClickHandler =
        ChangeWordClickHandler()

    init {
        itemView.setOnClickListener(changeWordHandler)
    }
}