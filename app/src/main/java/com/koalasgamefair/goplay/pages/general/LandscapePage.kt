package com.koalasgamefair.goplay.pages.general

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.koalasgamefair.goplay.R
import com.koalasgamefair.goplay.screens.MainScreen

abstract class LandscapePage(private val navigate: (Int, Boolean, Boolean) -> Unit): Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.button_home).setOnClickListener {
            navigate(MainScreen.TO_CHOICE, false, false)
        }
    }
}