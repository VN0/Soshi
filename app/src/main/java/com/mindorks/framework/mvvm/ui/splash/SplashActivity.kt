/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.mindorks.framework.mvvm.ui.splash

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle

import com.mindorks.framework.mvvm.BR
import com.mindorks.framework.mvvm.R
import com.mindorks.framework.mvvm.ViewModelProviderFactory
import com.mindorks.framework.mvvm.databinding.ActivitySplashBinding
import com.mindorks.framework.mvvm.ui.base.BaseActivity
import com.mindorks.framework.mvvm.ui.login.LoginActivity
import com.mindorks.framework.mvvm.ui.main.MainActivity
import javax.inject.Inject

/**
 * Created by amitshekhar on 08/07/17.
 */

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashNavigator {

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private var splashViewModel: SplashViewModel? = null

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.activity_splash

    override fun getViewModel() = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)

    override fun openLoginActivity() {

        val intent = LoginActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    override fun openMainActivity() {
        val intent = MainActivity.newIntent(this@SplashActivity)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (::factory.isInitialized) {
            splashViewModel = viewModel

            splashViewModel?.navigator = this
            splashViewModel?.startSeeding()
        }
    }
}
