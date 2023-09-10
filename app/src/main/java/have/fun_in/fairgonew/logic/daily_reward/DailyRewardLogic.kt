package have.fun_in.fairgonew.logic.daily_reward

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class DailyRewardLogic {
    val time = MutableLiveData(0L)

    fun startTimer(viewLifecycleOwner: LifecycleOwner) {
        viewLifecycleOwner.lifecycleScope.launch {
            while(time.value != 0L) {
                delay(1000)
                time.value?.let {
                    time.value = it - 1000
                } ?: Log.w("Daily reward logic", "Time value is null...")
            }
        }
    }
}