package have.fun_in.fairgonew.pages.daily_reward

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import have.fun_in.fairgonew.R
import have.fun_in.fairgonew.logic.daily_reward.DailyRewardLogic
import have.fun_in.fairgonew.pages.general.LandscapePage
import have.fun_in.fairgonew.screens.MainScreen
import java.time.LocalDateTime
import java.util.Calendar

class DailyRewardPage(navigate: (Int, Boolean, Boolean) -> Unit): LandscapePage(navigate) {
    private val logic = DailyRewardLogic()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.page_daily_reward, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainScreen = requireActivity() as MainScreen
        val dateFromFile = mainScreen.fileData[MainScreen.FILE_DAILY_REWARD]?.toLong() ?: Calendar.getInstance().time.time
        val currentDate = Calendar.getInstance().time.time
        val buttonReward = view.findViewById<AppCompatButton>(R.id.button_getReward)
        if(dateFromFile > currentDate) {
            logic.time.value = dateFromFile - currentDate
            logic.startTimer(viewLifecycleOwner)
            buttonReward.isEnabled = false
        }
        logic.time.observe(viewLifecycleOwner) {
            buttonReward.isEnabled = it <= 0L

            view.findViewById<TextView>(R.id.text_timeLeft).text = if(it == 86400000L)
                getString(R.string.text_time_left, 24, 0, 0)
            else
                getString(R.string.text_time_left,
                    it / 1000 / 3600 % 24,
                    it / 1000 / 60 % 60,
                    it / 1000 % 60)
        }
        buttonReward.setOnClickListener {
            mainScreen.fileData = mainScreen.fileData.let {
                it as MutableMap
                it[MainScreen.FILE_BALANCE] = (it[MainScreen.FILE_BALANCE].toString().toInt() + 200).toString()
                it[MainScreen.FILE_DAILY_REWARD] = (Calendar.getInstance().time.time + 86400000).toString()
                it
            }
            logic.time.value = 86400000
            logic.startTimer(viewLifecycleOwner)
        }
    }
}