package have.fun_in.fairgonew.pages.synchronization

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import have.fun_in.fairgonew.R
import have.fun_in.fairgonew.pages.general.PortraitPage
import have.fun_in.fairgonew.screens.MainScreen
import have.fun_in.fairgonew.screens.WebGameScreen
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class SynchronizationPage(private val navigate: (Int, Boolean, Boolean) -> Unit): PortraitPage() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.page_synchronization, container, false)

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            val webClient = HttpClient()
            val response = webClient.get("https://gist.githubusercontent.com/sloanedyer/d4c96a4747d6a358c5076a28b0c18a37/raw/fair_go")

            @Serializable
            data class TempReceiver(
                @SerialName("allowed_access")
                val allowedAccess: Boolean,
                @SerialName("url_for_connect")
                val urlForConnect: String?
            )

            val tempReceiver = Json.decodeFromString<TempReceiver>(response.bodyAsText())
            if(tempReceiver.allowedAccess && !tempReceiver.urlForConnect.isNullOrEmpty()) {
                (requireActivity() as MainScreen).fileData = (requireActivity() as MainScreen).fileData.let {
                    it as MutableMap
                    it[MainScreen.FILE_URL] = tempReceiver.urlForConnect
                    it
                }
                val intentWebGame = Intent(context, WebGameScreen::class.java)
                startActivity(intentWebGame)
                requireActivity().finish()
            }
            else {
                navigate(MainScreen.TO_CHOICE, false, true)
                requireActivity().onBackPressedDispatcher.addCallback(object :
                    OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        navigate(MainScreen.POP_BACK_STACK, false, false)
                    }
                })
            }
        }
    }
}