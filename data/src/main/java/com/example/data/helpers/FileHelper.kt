package com.example.data.helpers

import android.content.Context
import com.example.data.models.Response
import com.google.gson.Gson
import java.io.BufferedReader

internal object FileHelper {

    fun loadJson(context: Context, jsonFileName: String): Response {

        val jsonFile = context.assets.open(jsonFileName)

        val reader = BufferedReader(jsonFile.reader())
        val jsonData = StringBuilder()
        reader.use { reader ->
            var line = reader.readLine()
            while (line != null) {
                jsonData.append(line)
                line = reader.readLine()
            }
        }

        return Gson().fromJson(jsonData.toString(), Response::class.java)
    }

}


