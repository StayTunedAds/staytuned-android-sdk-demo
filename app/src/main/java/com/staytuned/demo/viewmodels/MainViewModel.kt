package com.staytuned.demo.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.staytuned.sdk.features.STLists
import com.staytuned.sdk.features.STOffline
import com.staytuned.sdk.features.STSections
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STSection
import com.staytuned.sdk.models.lists.STContentList
import com.staytuned.sdk.models.lists.STTrackList
import com.staytuned.sdk.models.lists.STGenericList
import com.staytuned.sdk.models.lists.STListType
import com.staytuned.sdk.models.offline.STContentLightOfflineItem

class MainViewModel: ViewModel() {

    val sections: MutableLiveData<List<STSection>> = MutableLiveData()
    val myTracksList: MutableLiveData<STTrackList> = MutableLiveData()
    val offlineList: LiveData<List<STContentLightOfflineItem>>? = STOffline.getInstance()?.contents

    fun getSections() {
        STSections.getInstance()?.getSections(object : STHttpCallback<List<STSection>> {
            override fun onSuccess(data: List<STSection>) {
                sections.postValue(data)
            }

            override fun onError(t: Throwable) {
                Log.e(LOG_TAG, "Error getting sections : ", t)
            }
        })
    }
}

private const val LOG_TAG = "MainViewModel"
