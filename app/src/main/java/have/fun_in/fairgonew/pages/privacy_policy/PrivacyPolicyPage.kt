package have.fun_in.fairgonew.pages.privacy_policy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import have.fun_in.fairgonew.R
import have.fun_in.fairgonew.pages.general.LandscapePage

class PrivacyPolicyPage(navigation: (Int, Boolean, Boolean) -> Unit): LandscapePage(navigation) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.page_privacy_policy, container, false)
}