package com.koalasgamefair.goplay.logic.game.pokies

import android.widget.ImageView
import com.koalasgamefair.goplay.R
import com.koalasgamefair.goplay.logic.game.GameLogic
import kotlinx.coroutines.delay
import kotlin.random.Random

class PlayPokiesLogic: GameLogic() {
    lateinit var slots: List<ImageView>
    private var slotsValues = List(12) { R.drawable.img_pokies01 + if(it <= 9) it else (it - 10)}

    override suspend fun realSpin(bet: Int, value: Double, balance: Int) {
        val threeSlotsLine = listOf(slots.take(4),
            slots.drop(4).take(4),
            slots.drop(8).take(4))
        for(i in 1..Random.nextInt(80, 100)) {
            slotsValues = (slotsValues as MutableList).apply {
                add(0, R.drawable.img_pokies01 + Random.nextInt(0, 10))
                add(5, R.drawable.img_pokies01 + Random.nextInt(0, 10))
                add(10, R.drawable.img_pokies01 + Random.nextInt(0, 10))
                removeAt(4)
                removeAt(8)
                removeAt(12)
            }
            for ((index, slotsLine) in threeSlotsLine.withIndex()) {
                for((ind, slot) in slotsLine.withIndex()) {
                    slot.setImageResource(slotsValues[ind + index * 4])
                }
            }
            delay(50)
        }
        for(i in 1..5) {
            slotsValues = (slotsValues as MutableList).apply {
                add(0, R.drawable.img_pokies01 + Random.nextInt(0, 10))
                add(5, R.drawable.img_pokies01 + Random.nextInt(0, 10))
                add(10, R.drawable.img_pokies01 + Random.nextInt(0, 10))
                removeAt(4)
                removeAt(8)
                removeAt(12)
            }
            for ((index, slotsLine) in threeSlotsLine.withIndex()) {
                for((ind, slot) in slotsLine.withIndex()) {
                    slot.setImageResource(slotsValues[ind + index * 4])
                }
            }
            delay(100)
        }
        val topLine = slotsValues.take(4)
        val centerLine = slotsValues.drop(4).take(4)
        val bottomLine = slotsValues.drop(8).take(4)
        var cash = 0
        for(i in R.drawable.img_pokies01..R.drawable.img_pokies10) {
            val numb1 = topLine.count {it == i}
            val numb2 = centerLine.count {it == i}
            val numb3 = bottomLine.count {it == i}
            if(numb1 != 0 && numb2 != 0 && numb3 != 0) {
                cash += calculateLineCash(i - R.drawable.img_pokies01, bet, value) * minOf(numb1, numb2, numb3)
            }
        }
        this.balance.value = balance + cash
        win.value = cash
    }

    private fun calculateLineCash(cashId: Int, bet: Int, value: Double)
        = (cashList[cashId] * (bet + 10.0 * value) / 100).toInt()

    companion object {
        val cashList = listOf(100, 750, 500, 250, 2000, 75, 50, 1500, 1250, 1000)
    }
}