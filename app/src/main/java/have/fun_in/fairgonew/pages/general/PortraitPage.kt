package have.fun_in.fairgonew.pages.general

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class PortraitPage: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        super.onCreate(savedInstanceState)
    }
}