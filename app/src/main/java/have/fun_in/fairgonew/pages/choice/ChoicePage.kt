package have.fun_in.fairgonew.pages.choice

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import have.fun_in.fairgonew.R
import have.fun_in.fairgonew.pages.general.PortraitPage
import have.fun_in.fairgonew.screens.MainScreen

class ChoicePage(private val navigate: (Int, Boolean, Boolean) -> Unit): PortraitPage() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.page_choice, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun setClickListenerForButton(buttonId: Int, destination: Int) {
            view.findViewById<AppCompatButton>(buttonId).setOnClickListener {
                navigate(destination, false, false)
            }
        }
        val idToDestination = mapOf(
            R.id.button_playPokies to MainScreen.TO_POKIES,
            R.id.button_playRoulette to MainScreen.TO_ROULETTE,
            R.id.button_dailyReward to MainScreen.TO_DAILY_REWARD,
            R.id.button_privacyPolicy to MainScreen.TO_PRIVACY_POLICY)

        for(itd in idToDestination) {
            setClickListenerForButton(itd.key, itd.value)
        }
        
    }
}