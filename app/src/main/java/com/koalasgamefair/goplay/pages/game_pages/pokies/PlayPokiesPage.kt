package com.koalasgamefair.goplay.pages.game_pages.pokies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.view.allViews
import com.koalasgamefair.goplay.R
import com.koalasgamefair.goplay.logic.game.pokies.PlayPokiesLogic
import com.koalasgamefair.goplay.pages.game_pages.GamePage

class PlayPokiesPage(navigate: (Int, Boolean, Boolean) -> Unit): GamePage(navigate) {
    override val logic = PlayPokiesLogic()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.page_play_pokies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logic.slots = view.allViews.filter { it is GridLayout }.map { it as GridLayout }.first()
            .allViews.filter { it is ImageView }.map { it as ImageView }.toList()
    }
}