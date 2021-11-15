package com.nestifff.recordrememberproj.viewChangeWords.wordsListActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nestifff.recordrememberproj.R
import com.nestifff.recordrememberproj.model.*
import com.google.android.material.snackbar.Snackbar


class WordsListAdapter(
        var wordsSet: MutableList<Word>?,
        private var view: View?,
        val context: Context,
        val deleteWordListener: DeleteWordOnClick
) : RecyclerView.Adapter<WordsListViewHolder>(), WordsListViewHolder.DeleteButtonClickListener {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsListViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView: View = inflater.inflate(R.layout.view_set_item, parent, false)

        return WordsListViewHolder(contactView, this)
    }

    override fun onBindViewHolder(holder: WordsListViewHolder, position: Int) {

        holder.blockChangeWord.visibility = View.GONE
        holder.deleteWordButton.visibility = View.VISIBLE

        val word = wordsSet!![position]
        holder.word = word
        holder.changeWordHandler?.setWord(word)

        val rusTextView = holder.rusTextView
        rusTextView.text = word.rus
        val endTextView = holder.engTextView
        endTextView.text = word.eng
    }

    override fun getItemCount() = wordsSet?.size ?: 0


    fun addWordInRV(word: Word?) {

        val position = wordsSet!!.indexOf(word)
        setEmptyViewVisibility()
        notifyItemInserted(position)

    }

    private fun setEmptyViewVisibility() {
        if (itemCount == 0) {
            view?.visibility = View.GONE
        } else {
            view?.visibility = View.VISIBLE
        }
    }

    override fun deleteButtonOnClick(word: Word) {

        if (word is WordInProcess) {
            SetWordsInProcess.get(context).deleteWord(word)

        } else if (word is WordLearned) {
            SetWordsLearned.get(context).deleteWord(word)
        }

        val position = wordsSet!!.indexOf(word)
        wordsSet!!.remove(word)

        setEmptyViewVisibility()
        notifyItemRemoved(position)

        deleteWordListener.onDelete(word)

        launchSnackbarToRestoreWord(word, position)
    }

    private fun launchSnackbarToRestoreWord(word: Word, position: Int) {

        if (view == null) return

        val snackbar: Snackbar = Snackbar.make(
                view!!,
                "Deleted word: ${word.rus} - ${word.eng}",
                Snackbar.LENGTH_SHORT
        )
        snackbar.setAction("Undo") { view ->
            if (view != null) {
                wordsSet!!.add(word)

                wordsSet?.sortBy { it.rus }

                setEmptyViewVisibility()
                notifyItemInserted(position)

                deleteWordListener.onRestore(word)

                if (word is WordInProcess) {
                    SetWordsInProcess.get(context).addWord(word)

                } else if (word is WordLearned) {
                    SetWordsLearned.get(context).addWord(word)
                }
            }
        }
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.purple_500))
        snackbar.show()

    }

    interface DeleteWordOnClick {
        fun onDelete(word: Word)
        fun onRestore(word: Word)
    }

}