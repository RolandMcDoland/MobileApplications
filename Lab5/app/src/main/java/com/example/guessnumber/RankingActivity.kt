package com.example.guessnumber

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ranking.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        //Get 10 best scores from endpoint
        CommunicationController().execute("http://hufiecgniezno.pl/br/record.php?f=get")

        //Set listener to button to get back to main activity
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //Get a response from specified URL
    inner class CommunicationController : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            var response: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                response = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                connection.disconnect()
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            handleJSON(result)
        }
    }

    //Handle JSON as String as I have no knowledge about its structure
    private fun handleJSON(result: String?) {
        var rankingView = ""

        //Separate into single fields
        val separatedEntries = result!!.split(",")

        var counter = 0
        var jumper = 1

        //Get top ten
        while(counter < 10){
            //Get the username and score
            rankingView += separatedEntries.get(jumper) + "\t" + separatedEntries.get(jumper + 1) + "\n"

            jumper += 3
            counter++
        }

        //Get rid of unnecessary characters
        rankingView = rankingView.replace("]", "")
        rankingView = rankingView.replace("\"", "")

        //Put it in the proper textView
        rankingTextView.text = rankingView
    }
}
