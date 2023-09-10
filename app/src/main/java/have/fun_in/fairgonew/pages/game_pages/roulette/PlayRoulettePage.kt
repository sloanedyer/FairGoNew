package have.fun_in.fairgonew.pages.game_pages.roulette

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import have.fun_in.fairgonew.R
import have.fun_in.fairgonew.logic.game.roulette.PlayRouletteLogic
import have.fun_in.fairgonew.pages.game_pages.GamePage

class PlayRoulettePage(navigate: (Int, Boolean, Boolean) -> Unit): GamePage(navigate) {
    override val logic = PlayRouletteLogic()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.page_play_roulette, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logic.roulette = view.findViewById(R.id.image_rouletteBody)
    }
}