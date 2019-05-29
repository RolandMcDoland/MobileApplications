package com.example.guessnumber

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    //Pick a random number between 0 and 20
    var numberToGuess = (0..20).random()

    //Initialize the try counter
    var counter = 1

    var dbHandler: DatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //For testing purposes
        numberToGuess = 7

        //Initialize the new game toaster
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, "New game started", duration)

        //Initialize the win/lose dialog
        val dialog = AlertDialog.Builder(this)

        //After alert is cancelled inform about new game started
        dialog.setOnCancelListener { toast.show() }

        //File name
        val fileName = "AppScore.txt"

        //Set the best score as something easy to beat
        var appScore = 0

        //Score in current session
        var score = 0

        //Create new file in Internal Storage directory
        val file = File(filesDir, fileName)

        var user = Users()

        //Read data from database
        var input = file.bufferedReader().readLines()
        var data = input[0].split(",")
        user.id = data[0].toInt()
        user.username = data[1]
        user.password = data[2]
        user.score = data[3].toInt()
        scoreTextView.text = ("Current score: " + user.score.toString())

        checkGuessButton.setOnClickListener {
            //If you win
            if(numberEditText.text.toString().toInt() == numberToGuess) {
                score = getScore(counter)

                //Update application score
                appScore += score

                //Send your score to endpoint
                CommunicationController().execute("http://hufiecgniezno.pl/br/record.php?f=add&id=" + user.username + "&r=" + appScore)

                //Show win dialog
                dialog.setTitle("Congratulations!")
                dialog.setMessage("Game won in " + counter.toString() + " tries with the score of " + score.toString())
                dialog.show()

                //Write application score to database
                val fileOutputStream: FileOutputStream
                try {
                    fileOutputStream = openFileOutput(fileName, MODE_PRIVATE)
                    fileOutputStream.write((user.id.toString()+",").toByteArray())
                    fileOutputStream.write((user.username+",").toByteArray())
                    fileOutputStream.write((user.password+",").toByteArray())
                    fileOutputStream.write((user.score.toString()+",").toByteArray())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                //Display score so far
                scoreTextView.setText("Score so far: " + appScore.toString()).toString()

                startNewGame()
            }
            //If you lose
            else {
                //Increase the try counter
                counter++

                //After ten failed tries start new game
                if (counter == 11) {
                    //Show lose dialog
                    dialog.setTitle("Game Over!")
                    dialog.setMessage("Took too many tries")
                    dialog.show()

                    startNewGame()
                }
            }
        }

        newGameButton.setOnClickListener{
            startNewGame(toast)
        }

        //Move to ranking activity to see top 10 scores
        rankingButton.setOnClickListener{
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }

        //Log the user out
        logoutButton.setOnClickListener{
            dbHandler = DatabaseHandler(this)
            dbHandler!!.updateUser(user)
            file.delete()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun startNewGame(toast: Toast) {
        //Pick new number
        numberToGuess = (0..20).random()

        //Reset the try counter
        counter = 1

        //Inform about new game being started
        toast.show()
    }

    fun startNewGame() {
        //Pick new number
        numberToGuess = (0..20).random()

        //Reset the try counter
        counter = 1
    }

    //Get score based on number of tries
    fun getScore(counter: Int): Int {
        if (counter == 1)
            return 5
        else if(counter < 5)
            return 3
        else if(counter < 7)
            return 2
        else
            return 1
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
    }
}

