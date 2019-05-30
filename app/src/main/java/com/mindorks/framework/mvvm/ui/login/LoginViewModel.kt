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

package com.mindorks.framework.mvvm.ui.login

import android.text.TextUtils
import com.google.firebase.auth.FirebaseUser
import com.mindorks.framework.mvvm.data.DataManager
import com.mindorks.framework.mvvm.data.model.api.LoginRequest
import com.mindorks.framework.mvvm.ui.base.BaseViewModel
import com.mindorks.framework.mvvm.utils.CommonUtils
import com.mindorks.framework.mvvm.utils.rx.SchedulerProvider


/**
 * Created by amitshekhar on 08/07/17.
 */

class LoginViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) : BaseViewModel<LoginNavigator>(dataManager, schedulerProvider) {

    /**
     * The [currentUser] is used to verify our user is still logged in or
     * we need to present the user with the option to Sign In or Sign Up
     */
    fun checkFirebaseLogin(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            //user is still valid and logged in
            navigator.openMainActivity()
        } else {

        }
    }

    fun isEmailAndPasswordValid(email: String, password: String): Boolean {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return false
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false
        }
        return !TextUtils.isEmpty(password)
    }

    fun login(email: String, password: String) {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .doServerLoginApiCall(LoginRequest.ServerLoginRequest(email, password))
                .doOnSuccess { response ->
                    dataManager
                            .updateUserInfo(
                                    response.accessToken,
                                    response.userId,
                                    DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
                                    response.userName,
                                    response.userEmail,
                                    response.googleProfilePicUrl)
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    setIsLoading(false)
                    navigator.openMainActivity()
                }, { throwable ->
                    setIsLoading(false)
                    navigator.handleError(throwable)
                }))
    }

    fun onFbLoginClick() {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest("test3", "test4"))
                .doOnSuccess { response ->
                    dataManager
                            .updateUserInfo(
                                    response.accessToken,
                                    response.userId,
                                    DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
                                    response.userName,
                                    response.userEmail,
                                    response.googleProfilePicUrl)
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    setIsLoading(false)
                    navigator.openMainActivity()
                }, { throwable ->
                    setIsLoading(false)
                    navigator.handleError(throwable)
                }))
    }

    fun onGoogleLoginClick() {
        setIsLoading(true)
        compositeDisposable.add(dataManager
                .doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest("test1", "test1"))
                .doOnSuccess { response ->
                    dataManager
                            .updateUserInfo(
                                    response.accessToken,
                                    response.userId,
                                    DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
                                    response.userName,
                                    response.userEmail,
                                    response.googleProfilePicUrl)
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({
                    setIsLoading(false)
                    navigator.openMainActivity()
                }, { throwable ->
                    setIsLoading(false)
                    navigator.handleError(throwable)
                }))
    }

    fun onServerLoginClick() {
        navigator.login()
    }
}
