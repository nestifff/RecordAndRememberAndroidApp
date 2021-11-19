package com.nestifff.recordrememberproj.viewChangeWords.wordsListActivity

//import android.support.v4.view.Menu
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nestifff.recordrememberproj.R
import com.nestifff.recordrememberproj.model.SetWordsInProcess
import com.nestifff.recordrememberproj.model.SetWordsLearned
import com.nestifff.recordrememberproj.model.Word
import com.nestifff.recordrememberproj.model.WordInProcess
import com.nestifff.recordrememberproj.model.WordsInFromFile.pullWordsInProcessFromFile
import com.nestifff.recordrememberproj.model.WordsInFromFile.pushWordsInFile
import com.nestifff.recordrememberproj.viewChangeWords.wordsListActivity.deleteWord.DeleteWordCallback


class WordsListActivity :
    AppCompatActivity(),
    AddWordDialogFragment.AddWordDialogListener,
    WordsListAdapter.DeleteWordOnClick {

    private val LEARNED_OR_IN_PROCESS = "isItNecessaryToShowLearnedWords"
    private var isShowLearned = false

    private lateinit var wordsSetRecyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: WordsListAdapter
    private lateinit var currWordsSet: MutableList<Word>

    private val dialogAddWordIdentifier = "DialogAddWord"

    private val pushWordsLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val filePath = pushWordsInFile(currWordsSet)
                Toast.makeText(this,
                    "Words are added in $filePath", Toast.LENGTH_LONG).show()
            }
        }

    private val pullWordsLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val words = pullWordsInProcessFromFile()
                addWordsIntoCurrWordsSet(words)
                Toast.makeText(
                    this,
                    "Words from \"Documents/pullWords.txt\" are added in set",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    fun newIntent(packageContext: Context?, isItNecessaryToShowLearnedWords: Boolean): Intent {
        val intent = Intent(packageContext, WordsListActivity::class.java)
        intent.putExtra(LEARNED_OR_IN_PROCESS, isItNecessaryToShowLearnedWords)
        return intent
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_set_of_words)

        isShowLearned = intent.getBooleanExtra(LEARNED_OR_IN_PROCESS, false)

        val textTypeSet = if (isShowLearned) "Learned Words" else "Words In Process"
        findViewById<TextView>(R.id.tv_type_of_set_to_view).text = textTypeSet

        if (!isShowLearned) {
            currWordsSet = ArrayList(
                SetWordsInProcess
                    .get(applicationContext).allWords
            )
            currWordsSet.sortBy { it.rus }
        } else {
            currWordsSet = ArrayList(SetWordsLearned.get(applicationContext).allWords)
            currWordsSet.sortBy { it.rus }
        }
        setWordsAmount()

        val buttonAddWord = findViewById<Button>(R.id.btn_add_word);
        if (isShowLearned) {
            buttonAddWord.visibility = View.GONE
        } else {
            buttonAddWord.setOnClickListener {
                AddWordDialogFragment()
                    .show(supportFragmentManager, dialogAddWordIdentifier)
            }
        }

        wordsSetRecyclerView = findViewById(R.id.view_set_recyclerView)
        layoutManager = LinearLayoutManager(this)
        wordsSetRecyclerView.layoutManager = layoutManager

        val divider = DividerItemDecoration(this, layoutManager.orientation)
        divider.setDrawable(getDrawable(R.drawable.divider_words_list)!!)

        wordsSetRecyclerView.addItemDecoration(divider)

        adapter = WordsListAdapter(
            currWordsSet,
            wordsSetRecyclerView,
            this,
            this,
        )

        wordsSetRecyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(DeleteWordCallback(adapter))
        itemTouchHelper.attachToRecyclerView(wordsSetRecyclerView)
    }

    override fun onDestroy() {
        pushWordsLauncher.unregister()
        pullWordsLauncher.unregister()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!isShowLearned) {
            val inflater = menuInflater
            inflater.inflate(R.menu.pm_view_words_in_process, menu)
        }
        return true

    }

    override fun addWordDialogOnClick(word: WordInProcess) {

        adapter.wordsSet.add(word)
        adapter.wordsSet.sortBy { it.rus }

        adapter.addWordInRV(word)

        setWordsAmount()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.pm_add_words_from_file -> {
                pullWordsLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                return true
            }
            R.id.pm_download_words_in_file -> {
                pushWordsLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addWordsIntoCurrWordsSet(newWords: MutableList<Word>) {

        for (newWord: Word in newWords) {

            if (!currWordsSet.any {
                    it.rus == newWord.rus && it.eng == newWord.eng
                }) {

                currWordsSet.add(newWord)
                SetWordsInProcess.get(applicationContext).addWord(newWord as WordInProcess)
            }
        }
        currWordsSet.sortBy { it.rus }

        val listOfIndexes: MutableList<Int> = mutableListOf()
        for ((index, word) in currWordsSet.withIndex()) {
            if (newWords.contains(word)) {
                listOfIndexes.add(index)
            }
        }

        adapter.wordsSet = currWordsSet
        for (ind in listOfIndexes) {
            adapter.notifyItemInserted(ind)
        }

        setWordsAmount()
    }

    override fun onDelete(word: Word) {
        setWordsAmount()
    }

    override fun onRestore(word: Word) {
        setWordsAmount()
    }

    private fun setWordsAmount() {
        findViewById<TextView>(R.id.tv_size_words_set).text =
            "Amount: " + currWordsSet.size
    }
}
