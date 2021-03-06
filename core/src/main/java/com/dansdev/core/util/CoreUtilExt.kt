package com.dansdev.core.util

import androidx.lifecycle.MutableLiveData
import com.dansdev.core.CoreViewState
import timber.log.Timber

fun <T : CoreViewState> MutableLiveData<T>.update(
    postUpdateEnabled: Boolean = true,
    forceUpdate: Boolean = false,
    block: T.() -> Unit
) {
    val data = value ?: run {
        Timber.w("You try to update NULL value of livedata")
        return
    }
    if (forceUpdate) {
        block(data)
        postValue(data)
    } else {
        try {
            val hash = data.hashCode()
            block(data)
            val updatedHash = data.hashCode()
            if (postUpdateEnabled && hash != updatedHash) postValue(data)
        } catch (e: Exception) {
            Timber.e(e)
            postValue(data)
        }
    }
}
