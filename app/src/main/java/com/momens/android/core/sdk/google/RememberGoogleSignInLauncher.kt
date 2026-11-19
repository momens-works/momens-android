package com.momens.android.core.sdk.google

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.momens.android.core.sdk.google.di.GoogleSignInEntryPoint
import dagger.hilt.android.EntryPointAccessors

@Composable
fun rememberGoogleSignInLauncher(): GoogleSignInLauncher {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    return remember(activity) {
        EntryPointAccessors.fromActivity(
            activity,
            GoogleSignInEntryPoint::class.java,
        ).googleSignInLauncher()
    }
}

private tailrec fun Context.findActivity(): Activity = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> error("Google sign-in requires an Activity context.")
}
