package have.fun_in.fairgonew.logic.game

import androidx.lifecycle.MutableLiveData

abstract class GameLogic {
    val balance = MutableLiveData(0)
    val win = MutableLiveData(0)
    val bet = MutableLiveData(100)
    val value = MutableLiveData(0.0)

    fun changeValue(increase: Boolean = true) {
        val value = value.value
        val bet = bet.value
        if(value != null && bet != null) {
            val operator = if(increase) 5 else -5
            val intValue = (value * 100).toInt()
            val condition = if(increase) intValue + operator <= 100 else intValue + operator >= 0
            if(condition) {
                this.value.value = (intValue + operator) / 100.0
            }
            else {
                if(!increase && bet == 10) return
                this.value.value = if (increase) 0.05 else 0.95
                this.bet.value = bet + if (increase) 10 else -10
            }
        }
    }

    suspend fun spin() {
        val bet = bet.value ?: 1
        val value = value.value ?: 0.0
        val balance = balance.value ?: -1
        if((bet + 10.0 * value) <= balance) {
            val balanceAfterClick = balance - (bet + 10.0 * value).toInt()
            this.balance.value = balanceAfterClick
            win.value = 0
            realSpin(bet, value, balanceAfterClick)
        }
    }

    abstract suspend fun realSpin(bet: Int, value: Double, balance: Int)
}