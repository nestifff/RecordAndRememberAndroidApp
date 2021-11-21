package com.nestifff.recordrememberproj.model.inFromFiles

import android.os.Environment
import com.nestifff.recordrememberproj.model.word.Word
import com.nestifff.recordrememberproj.model.word.WordInProcess
import com.nestifff.recordrememberproj.model.word.WordLearned
import java.io.*

object WordsInFromFile {

    fun pushWordsInFile(words: List<Word>): String {

        var toReturnPath = ""
        try {
            val rootPath: String = Environment.getExternalStorageDirectory()
                    .absolutePath.toString() + "/Documents/"
            toReturnPath += "Documents/"

            val fileName =
                    when (words[0]) {
                        is WordInProcess -> "pushedWordsInProcess.txt"
                        is WordLearned -> "pushedWordsLearned.txt"
                        else -> ""
                    }
            toReturnPath += fileName

            val f = File(rootPath + fileName)
            f.createNewFile()
            val out = FileOutputStream(f)

            for (word: Word in words) {
                out.write((word.rus + ", " + word.eng + "\n").toByteArray())
            }

            out.flush()
            out.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return toReturnPath
    }

    fun pullWordsInProcessFromFile(): MutableList<Word> {

        val words = mutableListOf<Word>()
        val rootPath: String = Environment.getExternalStorageDirectory()
                .absolutePath.toString() + "/Documents/"

        val fileName = "pullWords.txt"
        val file = File(rootPath + fileName)

        try {

            var rus: String?
            var eng: String?
            val br = BufferedReader(FileReader(file))
            var line: String?

            while (br.readLine().also { line = it } != null) {
                rus = line?.let { it.split(",".toRegex(), 0)[0].trim() }
                eng = line?.let { it.split(",".toRegex(), 0)[1].trim() }
                words.add(
                    WordInProcess(
                        eng,
                        rus
                    )
                )
            }
            br.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return words
    }


    private fun writeAllWords(words: List<Word>, out: FileOutputStream) {

        for (word: Word in words) {
            out.write((word.rus + ", " + word.eng + "\n").toByteArray())
        }

        out.flush()
        out.close()
    }

}