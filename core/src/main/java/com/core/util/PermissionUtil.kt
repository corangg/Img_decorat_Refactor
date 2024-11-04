package com.core.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.isDenied(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED

fun Context.isGrantedAll(vararg permissions: String) = permissions.all(::isGranted)
fun Context.isDeniedAny(vararg permissions: String) = permissions.any(::isDenied)