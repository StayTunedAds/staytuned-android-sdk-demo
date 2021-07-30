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
    val myContentsList: MutableLiveData<STContentList> = MutableLiveData()
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

    fun getLists() {/*
        STLists.getInstance()?.getLists(object : STHttpCallback<List<STGenericList<*>>> {
            override fun onSuccess(data: List<STGenericList<*>>) {
                val foundContentList = data.find { it.name == "MyContents" }
                val foundTrackList = data.find { it.name == "MyTracks" }


                if (foundContentList == null) {
                    createContentList()
                } else {
                    (foundContentList as STContentList).getItems(object: STHttpCallback<STContentList> {
                        override fun onSuccess(data: STContentList) {
                            myContentsList.postValue(data)
                        }

                        override fun onError(t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
                if (foundTrackList == null) {
                    createTrackList()
                } else {
                    (foundTrackList as STTrackList).pull(object: STHttpCallback<STTrackList> {
                        override fun onSuccess(data: STTrackList) {
                            myTracksList.postValue(data)
                        }

                        override fun onError(t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }

        }) */
    }

    private fun createContentList() {
        STLists.getInstance()?.create( CONTENT_NAME_ST_LIST, STListType.Favorite, object : STHttpCallback<STContentList> {
            override fun onSuccess(data: STContentList) {
                myContentsList.postValue(data)
            }

            override fun onError(t: Throwable) {
                Log.e(LOG_TAG, "Error creating content list $CONTENT_NAME_ST_LIST : ", t)
            }
        })
    }

    private fun createTrackList() {
        STLists.getInstance()?.create("MyTracks", STListType.Favorite, object : STHttpCallback<STTrackList> {
            override fun onSuccess(data: STTrackList) {
                myTracksList.postValue(data)
            }

            override fun onError(t: Throwable) {
                Log.e(LOG_TAG, "Error creating content list $TRACK_NAME_ST_LIST : ", t)
            }
        })
    }
}

private const val LOG_TAG = "MainViewModel"

private const val CONTENT_NAME_ST_LIST = "MyContents"
private const val TRACK_NAME_ST_LIST = "MyTracks"
