package com.koalasgamefair.goplay.pages.game_pages

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.koalasgamefair.goplay.R
import com.koalasgamefair.goplay.logic.game.GameLogic
import com.koalasgamefair.goplay.pages.general.LandscapePage
import com.koalasgamefair.goplay.screens.MainScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class GamePage(navigate: (Int, Boolean, Boolean) -> Unit): LandscapePage(navigate) {
    abstract val logic: GameLogic
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainScreen).fileData[MainScreen.FILE_BALANCE]?.let {
            logic.balance.value = it.toInt()
        }
        fun bindTexts(textId: Int, liveData: LiveData<Int>) {
            liveData.observe(viewLifecycleOwner) {
                view.findViewById<TextView>(textId).text = it.toString()
                if(textId == R.id.text_balance) {
                    (requireActivity() as MainScreen).fileData = (requireActivity() as MainScreen).fileData.let { fileData ->
                        fileData as MutableMap
                        fileData[MainScreen.FILE_BALANCE] = it.toString()
                        fileData
                    }
                }
            }
        }
        val textIdsToLiveData = mapOf(
            R.id.text_balance to logic.balance,
            R.id.text_win to logic.win,
            R.id.text_bet to logic.bet
        )
        for(elem in textIdsToLiveData) {
            bindTexts(elem.key, elem.value)
        }
        logic.value.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.text_value).text = it.toString()
        }

        view.findViewById<ImageButton>(R.id.button_decreaseValue).apply {
            setOnClickListener {
                logic.changeValue(false)
            }
            setOnLongClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    while(it.isPressed) {
                        logic.changeValue(false)
                        delay(100)
                    }
                }
                true
            }
        }
        view.findViewById<ImageButton>(R.id.button_increaseValue).apply {
            setOnClickListener {
                logic.changeValue(true)
            }
            setOnLongClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    while(it.isPressed) {
                        logic.changeValue(true)
                        delay(100)
                    }
                }
                true
            }
        }
        view.findViewById<ImageButton>(R.id.button_spin).setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                it.isEnabled = false
                logic.spin()
                it.isEnabled = true
            }
        }
    }
}