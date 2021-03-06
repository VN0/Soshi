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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mindorks.framework.mvvm.ViewModelProviderFactory
import com.mindorks.framework.mvvm.ui.main.MainActivity
import javax.inject.Inject
import com.mindorks.framework.mvvm.ui.base.BasicActivity
import com.mindorks.framework.mvvm.utils.AppConstants


/**
 * Created by amitshekhar on 08/07/17.
 */

class LoginActivity : BasicActivity<LoginViewModel>(), LoginNavigator {

    companion object {

        private val TAG = LoginActivity::class.simpleName

        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    @Inject
    lateinit var factory: ViewModelProviderFactory

    private var loginViewModel: LoginViewModel? = null

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (::factory.isInitialized) {
            loginViewModel = getViewModel()
            loginViewModel!!.navigator = this
        }

        //firebase ui launch
        val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), AppConstants.LOGIN_RESULT_OK)
    }

    override fun onStart() {
        super.onStart()
        getViewModel().checkFirebaseLogin(auth.currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppConstants.LOGIN_RESULT_OK) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                Log.d(TAG, "createUserWithEmail:success")
                //val user = FirebaseAuth.getInstance().currentUser
                //updateUI(user)
                //login(response...)
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.w(TAG, "createUserWithEmail:failure", response?.error)
                handleError("createUserWithEmail:failure ${response?.error}")
            }
        }
    }

    override fun getViewModel(): LoginViewModel {
        loginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
        return loginViewModel as LoginViewModel
    }

    override fun handleError(throwable: Throwable) {
        // handle error
    }

    override fun handleError(error: String) {
        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
    }

    override fun login() {
        /*val email = mActivityLoginBinding!!.etEmail.text!!.toString()
        val password = mActivityLoginBinding!!.etPassword.text!!.toString()
        if (loginViewModel!!.isEmailAndPasswordValid(email, password)) {
            hideKeyboard()
            loginViewModel!!.login(email, password)
        } else {
            Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show()
        }*/
    }

    override fun openMainActivity() {
        val intent = MainActivity.newIntent(this@LoginActivity)
        startActivity(intent)
        finish()
    }

    override fun createNewUserEmailPassword(email: String, password: String) {
       /* auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        handleError("createUserWithEmail:failure ${task.exception}")
                    }

                    // ...
                })*/
    }

    override fun signInExistingUserEmailPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }

                    // ...
                })
    }
}
