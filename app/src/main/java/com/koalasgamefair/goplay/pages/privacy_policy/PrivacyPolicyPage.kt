package com.koalasgamefair.goplay.pages.privacy_policy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.koalasgamefair.goplay.R
import com.koalasgamefair.goplay.pages.general.LandscapePage

class PrivacyPolicyPage(navigation: (Int, Boolean, Boolean) -> Unit): LandscapePage(navigation) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.page_privacy_policy, container, false)
}