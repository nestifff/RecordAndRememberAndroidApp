package com.nestifff.recordrememberproj.views.viewWords

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nestifff.recordrememberproj.R
import com.nestifff.recordrememberproj.model.dao.SetWordsInProcess
import com.nestifff.recordrememberproj.model.dao.SetWordsLearned
import com.nestifff.recordrememberproj.model.inFromFiles.WordsInFromFile.pullWordsInProcessFromFile
import com.nestifff.recordrememberproj.model.inFromFiles.WordsInFromFile.pushWordsInFile
import com.nestifff.recordrememberproj.model.word.Word
import com.nestifff.recordrememberproj.model.word.WordInProcess
import com.nestifff.recordrememberproj.model.word.WordsType
import com.nestifff.recordrememberproj.model.word.WordsType.*
import com.nestifff.recordrememberproj.views.viewWords.addWord.AddWordDialogFragment
import com.nestifff.recordrememberproj.views.viewWords.deleteWord.DeleteWordCallback
import com.nestifff.recordrememberproj.views.viewWords.recyclerView.WordsListAdapter


class WordsListActivity :
    AppCompatActivity(),
    AddWordDialogFragment.AddWordDialogListener,
    WordsListAdapter.DeleteWordOnClick {

    private lateinit var whatShow: WordsType
    private val whatShowKey = "${this.javaClass.name}.whatShowKey"

    private lateinit var wordsSetRecyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: WordsListAdapter
    private lateinit var currWordsSet: MutableList<Word>

    private var recyclerViewHasBeenCreated = false

    private val dialogAddWordKey = "DialogAddWord"

    private val pushWordsLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val filePath = pushWordsInFile(currWordsSet)
                Toast.makeText(
                    this,
                    "Words are added in $filePath", Toast.LENGTH_LONG
                ).show()
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_set_of_words)

        val currType = if (savedInstanceState?.containsKey(whatShowKey) == true) {
            WordsType.valueOf(savedInstanceState.getString(whatShowKey)!!)
        } else {
            IN_PROCESS
        }

        refreshWordsSet(currType)
    }


    override fun onDestroy() {
        pushWordsLauncher.unregister()
        pullWordsLauncher.unregister()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (whatShow == IN_PROCESS) {
            val inflater = menuInflater
            inflater.inflate(R.menu.pm_view_words_in_process, menu)
        }
        return true
    }

    override fun addWordDialogOnClick(word: WordInProcess) {

        adapter.wordsSet.add(word)
        adapter.wordsSet.sortBy { it.rus }

        adapter.addWordInRV(word)

        setWordsAmountCurrentSet(whatShow)
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

        setWordsAmountCurrentSet(whatShow)
    }

    override fun onDelete(word: Word) {
        setWordsAmountCurrentSet(whatShow)
    }

    override fun onRestore(word: Word) {
        setWordsAmountCurrentSet(whatShow)
    }

    private fun setWordsAmountCurrentSet(where: WordsType) {
        when (where) {
            IN_PROCESS ->
                findViewById<TextView>(R.id.tv_size_words_set_in_process).text =
                    "Amount: " + currWordsSet.size
            LEARNED ->
                findViewById<TextView>(R.id.tv_size_words_set_learned).text =
                    "Amount: " + currWordsSet.size
            else -> return
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(whatShowKey, whatShow.name)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        whatShow = valueOf(savedInstanceState.getString(whatShowKey)!!)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshWordsSet(newType: WordsType) {

        whatShow = newType

        currWordsSet = when (whatShow) {
            IN_PROCESS -> ArrayList(SetWordsInProcess.get(applicationContext).allWords)
            LEARNED -> ArrayList(SetWordsLearned.get(applicationContext).allWords)
            ARCHIVED -> mutableListOf()
        }
        currWordsSet.sortBy { it.rus }
        initTopSwitch(whatShow)

        val buttonAddWord = findViewById<Button>(R.id.btn_add_word)
        if (whatShow != IN_PROCESS) {
            buttonAddWord.visibility = View.GONE
        } else {
            buttonAddWord.visibility = View.VISIBLE
            buttonAddWord.setOnClickListener {
                AddWordDialogFragment().show(supportFragmentManager, dialogAddWordKey)
            }
        }

        if (!recyclerViewHasBeenCreated) {

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

            recyclerViewHasBeenCreated = true

        } else {
            adapter.wordsSet = currWordsSet
            adapter.notifyDataSetChanged()
        }

        setOnClickListenersTopSwitch()
        invalidateOptionsMenu()
    }

    private fun setOnClickListenersTopSwitch() {

        findViewById<LinearLayout>(R.id.ll_switch_learned).setOnClickListener {
            if (whatShow == LEARNED) return@setOnClickListener
            refreshWordsSet(LEARNED)
        }

        findViewById<LinearLayout>(R.id.ll_switch_in_process).setOnClickListener {
            if (whatShow == IN_PROCESS) return@setOnClickListener
            refreshWordsSet(IN_PROCESS)
        }

        findViewById<LinearLayout>(R.id.ll_switch_archived).setOnClickListener {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initTopSwitch(activeSet: WordsType) {

        findViewById<TextView>(R.id.tv_size_words_set_in_process).text =
            "Amount: " + SetWordsInProcess.get(this).wordsNum
        findViewById<TextView>(R.id.tv_size_words_set_learned).text =
            "Amount: " + SetWordsLearned.get(this).wordsNum
        findViewById<TextView>(R.id.tv_size_words_set_archived).text =
            "Amount: 0"

        val llInProcess = findViewById<LinearLayout>(R.id.ll_switch_in_process)
        val llLearned = findViewById<LinearLayout>(R.id.ll_switch_learned)

        when (activeSet) {
            IN_PROCESS -> {
                llInProcess.background =
                    getDrawable(R.drawable.layout_switch_active_in_process)
                llLearned.background =
                    getDrawable(R.drawable.layout_switch_not_active)
            }
            LEARNED -> {
                llLearned.background =
                    getDrawable(R.drawable.layout_switch_active_learned)
                llInProcess.background =
                    getDrawable(R.drawable.layout_switch_not_active)
            }
            else -> return
        }
    }

}
