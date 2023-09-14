package com.koalasgamefair.goplay

import android.app.Application
import android.util.Log
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()

        val oneSignalId = getString(R.string.format_text_one_signal_id,
            getString(R.string.text_one_signal_id_1),
            getString(R.string.text_one_signal_id_2),
            getString(R.string.text_one_signal_id_3))
        Log.i("One signal id", oneSignalId)
        OneSignal.initWithContext(this, oneSignalId)
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }
    }
}