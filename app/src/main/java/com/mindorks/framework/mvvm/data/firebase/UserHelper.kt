package com.mindorks.framework.mvvm.data.firebase

import com.google.firebase.auth.FirebaseUser

/**
 * Created by adrian mohnacs on 2019-05-29
 */
interface UserHelper {

    fun getFirebaseUser(): FirebaseUser?
}