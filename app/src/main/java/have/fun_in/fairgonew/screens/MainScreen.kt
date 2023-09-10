package have.fun_in.fairgonew.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import have.fun_in.fairgonew.R
import have.fun_in.fairgonew.pages.choice.ChoicePage
import have.fun_in.fairgonew.pages.daily_reward.DailyRewardPage
import have.fun_in.fairgonew.pages.game_pages.pokies.PlayPokiesPage
import have.fun_in.fairgonew.pages.game_pages.roulette.PlayRoulettePage
import have.fun_in.fairgonew.pages.privacy_policy.PrivacyPolicyPage
import have.fun_in.fairgonew.pages.synchronization.SynchronizationPage
import java.io.File
import java.io.FileNotFoundException

class MainScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val urlFromFile = savedFileData[FILE_URL]
        if(!urlFromFile.isNullOrEmpty()) {
            val intentWebGame = Intent(this, WebGameScreen::class.java)
            startActivity(intentWebGame)
            finish()
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            setContentView(R.layout.screen_main)
            navigate(TO_SYNCHRONIZATION, isAdd = true, isAllowingStateLoss = false)
        }
    }

    private fun navigate(destination: Int, isAdd: Boolean, isAllowingStateLoss: Boolean) {
        Log.i("Navigation callback", "Navigation code: $destination. Is add: $isAdd. Is allowing state loss: $isAllowingStateLoss.")
        if(destination == POP_BACK_STACK) {
            val page = supportFragmentManager.fragments.first()
            if(page is SynchronizationPage || page is ChoicePage) {
                Log.i("Navigation callback", "Finish.")
                finish()
            }
            else {
                navigate(TO_CHOICE, isAdd = false, isAllowingStateLoss = true)
            }
        }
        else {
            val transaction = supportFragmentManager.beginTransaction()
            if (isAdd) {
                if(destination != TO_SYNCHRONIZATION) {
                    transaction.addToBackStack(null)
                }
                transaction.add(R.id.fragments, pageByDestination(destination))
            } else {
                transaction.replace(R.id.fragments, pageByDestination(destination))
            }

            if (isAllowingStateLoss) {
                transaction.commitAllowingStateLoss()
            } else {
                transaction.commit()
            }
        }
    }

    private var savedFileData: Map<String, String> = mapOf()
        get() {
            if(field.isEmpty()) {
                savedFileData = getFileDataFromFile()
            }
            return field
        }
    var fileData: Map<String, String>
        get() = savedFileData
        set(value) {
            saveFileDataIntoFile(value)
            savedFileData = value
        }

    private fun getFileDataFromFile(): Map<String, String> {
        val tag = "Get file data from file"
        val file = File(applicationContext.filesDir, "saved_data.txt")
        val stringFile = try {
            file.readLines()
        }
        catch(e: FileNotFoundException) {
            Log.i(tag, "File not found exception.")
            emptyList()
        }
        if(stringFile.isEmpty() || stringFile[0].isEmpty()) {
            val temp = mapOf(
                FILE_BALANCE to "1000",
                FILE_DAILY_REWARD to "0",
                FILE_URL to ""
            )
            saveFileDataIntoFile(temp)
            return temp
        }
        Log.i(tag, stringFile.joinToString())
        return mapOf(
            FILE_BALANCE to stringFile[0].substring(
                stringFile[0].indexOf("$FILE_BALANCE:") + FILE_BALANCE.length + 1,
                stringFile[0].length
            ),
            FILE_DAILY_REWARD to stringFile[1].substring(
                stringFile[1].indexOf("$FILE_DAILY_REWARD:") + FILE_DAILY_REWARD.length + 1,
                stringFile[1].length
            ),
            FILE_URL to stringFile[2].substring(
                stringFile[2].indexOf("$FILE_URL:") + FILE_URL.length + 1,
                stringFile[2].length
            )
        )
    }

    private fun saveFileDataIntoFile(fileData: Map<String, String>) {
        val file = File(applicationContext.filesDir, "saved_data.txt")

        fun appendString(str: String) = "$str:${fileData[str]}"

        val fileStringBuilder = StringBuilder()
        fileStringBuilder.appendLine(appendString(FILE_BALANCE))
        fileStringBuilder.appendLine(appendString(FILE_DAILY_REWARD))
        fileStringBuilder.appendLine(appendString(FILE_URL))

        file.writeText(fileStringBuilder.toString())
    }

    private fun pageByDestination(destination: Int) = when(destination) {
        TO_SYNCHRONIZATION -> SynchronizationPage(::navigate)
        TO_CHOICE -> ChoicePage(::navigate)
        TO_POKIES -> PlayPokiesPage(::navigate)
        TO_ROULETTE -> PlayRoulettePage(::navigate)
        TO_DAILY_REWARD -> DailyRewardPage(::navigate)
        TO_PRIVACY_POLICY -> PrivacyPolicyPage(::navigate)
        else -> throw IllegalArgumentException("Destination code is $destination, but expected from $TO_SYNCHRONIZATION to $TO_PRIVACY_POLICY.")
    }

    companion object {
        const val POP_BACK_STACK = -1
        const val TO_SYNCHRONIZATION = 0
        const val TO_CHOICE = 1
        const val TO_POKIES = 2
        const val TO_ROULETTE = 3
        const val TO_DAILY_REWARD = 4
        const val TO_PRIVACY_POLICY = 5

        const val FILE_BALANCE = "balance"
        const val FILE_DAILY_REWARD = "daily-reward"
        const val FILE_URL = "url"
    }
}