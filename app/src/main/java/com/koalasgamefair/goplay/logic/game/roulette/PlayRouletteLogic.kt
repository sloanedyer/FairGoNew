package com.koalasgamefair.goplay.logic.game.roulette

import android.widget.ImageView
import com.koalasgamefair.goplay.logic.game.GameLogic
import kotlinx.coroutines.delay
import kotlin.random.Random

class PlayRouletteLogic: GameLogic() {
    lateinit var roulette: ImageView
    override suspend fun realSpin(bet: Int, value: Double, balance: Int) {
        for(k in 1..Random.nextLong(200, 400)) {
            roulette.rotation += 5
            delay(20)
            if(roulette.rotation >= 360f) {
                roulette.rotation = 0f
            }
        }
        while(roulette.rotation % 30 != 0f) {
            roulette.rotation += 5
            delay(50)
        }
        calculateCash(bet, value, balance)
    }

    private fun calculateCash(bet: Int, value: Double, balance: Int) {
        val cash = (cashList[(roulette.rotation % 360 / 30).toInt()] * (bet + 10.0 * value) / 100).toInt()
        this.balance.value = balance + cash
        win.value = cash
    }

    companion object {
        val cashList = listOf(50, 200, 400, 0, 50, 0, 1000, 0, 100, 300, 0, 500)
    }
}