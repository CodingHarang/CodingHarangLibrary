package com.codingharang.log

import android.util.Log

object LOG {

    fun e (tag: String, message: String) {
        if (BuildConfig.DEBUG) Log.e(tag, message) else Unit
    }
}
