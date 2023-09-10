package have.fun_in.fairgonew.pages.general

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import have.fun_in.fairgonew.R
import have.fun_in.fairgonew.screens.MainScreen

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