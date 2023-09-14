package com.koalasgamefair.goplay.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.koalasgamefair.goplay.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


class WebGameScreen : AppCompatActivity() {
    var arrayValueCallback: ValueCallback<Array<Uri>>? = null
    var uriCallback: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_web_game)
        setWebView(findViewById(R.id.webView_main))()
        lifecycleScope.launch {
            loadUrl(findViewById(R.id.webView_main))
        }
    }

    private fun loadUrl(we: WebView) {
        val url = getUrlFromFile()
        we.loadUrl(url)
        Log.i("Web game screen", "Try to load $url")
    }

    @Suppress("DEPRECATION")
    private fun setAllows(we: WebView) {
        we.settings.allowContentAccess = true
        we.settings.allowFileAccess = true
        we.settings.allowFileAccessFromFileURLs = true
        we.settings.allowUniversalAccessFromFileURLs = true
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setWebView(we: WebView): () -> Unit = {
        val importantObjects = {
            we.webChromeClient = WebChromeClientOwn()
            we.webViewClient = WebViewClientOwn()
        }
        we.settings.domStorageEnabled = true
        we.settings.javaScriptEnabled = true
        we.settings.databaseEnabled = true
        we.settings.javaScriptCanOpenWindowsAutomatically = true
        we.settings.useWideViewPort = true
        we.settings.loadWithOverviewMode = true
        fun nonBooleanSettings() {
            val usrAgent = we.settings.userAgentString
            we.settings.userAgentString = usrAgent.replace("; wv", "")
            we.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            we.settings.cacheMode = WebSettings.LOAD_DEFAULT
        }
        setAllows(we)
        importantObjects()
        CookieManager.getInstance().apply {
            setAcceptCookie(this is CookieManager)
            setAcceptThirdPartyCookies(we, this is CookieManager)
        }
        nonBooleanSettings()
    }

    class WebViewClientOwn : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val uri = request.url.toString()
            return if (uri.contains("/")) {
                Log.i(LOG_TAG, uri)
                !uri.contains("http")
            } else true
        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
            Log.i(LOG_TAG, "Do update visited history")
            super.doUpdateVisitedHistory(view, url, isReload)
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            Log.i(LOG_TAG, "Try to load resource with url \"$url\".")
            super.onLoadResource(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }
        companion object {
            const val LOG_TAG = "Web view client"
        }
    }
    inner class WebChromeClientOwn : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            launcherStringActivityResult.launch(Manifest.permission.CAMERA)
            arrayValueCallback = filePathCallback
            return true
        }

        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            Log.i("Web chrome client", "Window created. ResultMsg: $resultMsg.")
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
        }
    }

    private fun Intent.addCategoryAndType(category: String, type: String): Intent {
        addCategory(category)
        this.type = type
        return this
    }

    private fun Intent.asArray() = arrayOf(this)

    @Suppress("DEPRECATION")
    val launcherStringActivityResult = registerForActivityResult(
        if(this is WebGameScreen) ActivityResultContracts.RequestPermission() else throw IllegalArgumentException("What?")
    ) { _: Boolean? ->
        lifecycleScope.launch {
            var fileImage: File? = null
            launch(Dispatchers.IO) {
                try {
                    fileImage = File.createTempFile(
                        "file_image",
                        ".jpg",
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    )
                    uriCallback = Uri.fromFile(fileImage)
                } catch (ex: IOException) {
                    Log.e("PhotoFile", "Unable to cre", ex)
                }
            }.join()
            launch {
                val intentImageReceive = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage))
                }
                val old = Intent(Intent.ACTION_GET_CONTENT).addCategoryAndType(
                    Intent.CATEGORY_OPENABLE,
                    "*/*"
                )
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                launch {
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, old)
                    chooserIntent.putExtra(
                        Intent.EXTRA_INITIAL_INTENTS,
                        intentImageReceive.asArray()
                    )
                }.join()
                startActivityForResult(chooserIntent, 1)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (arrayValueCallback == null) return
        if(resultCode != -1) {
            arrayValueCallback?.onReceiveValue(null)
        }
        else {
            if (data == null || data.dataString == null) {
                if (uriCallback != null) {
                    arrayValueCallback?.onReceiveValue(arrayOf(uriCallback!!))
                } else {
                    arrayValueCallback?.onReceiveValue(null)
                }
            }
        }
        if (resultCode == -1) {
            if (data != null) {
                val stringData = data.dataString
                stringData?.let {
                    val uriForReceive = Uri.parse(it)
                    arrayValueCallback?.onReceiveValue(arrayOf(uriForReceive))
                }
            }
        }
        arrayValueCallback = null
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        findViewById<WebView>(R.id.webView_main).saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<WebView>(R.id.webView_main).restoreState(savedInstanceState)
    }

    private fun illegalUrlException(): Nothing = throw IllegalArgumentException("Url in WebGameScreen is empty!")

    private fun getUrlFromFile(): String {
        val file = File(applicationContext.filesDir, "saved_data.txt")
        val stringFile = try {
            file.readLines()
        }
        catch(e: FileNotFoundException) {
            illegalUrlException()
        }
        if(stringFile.isEmpty() || stringFile[0].isEmpty()) {
            illegalUrlException()
        }
        return stringFile[2].substring(
                stringFile[2].indexOf("${MainScreen.FILE_URL}:")
                        + MainScreen.FILE_URL.length + 1,
                stringFile[2].length
        )
    }

    companion object {
        const val FILE_URL = "url"
    }
}