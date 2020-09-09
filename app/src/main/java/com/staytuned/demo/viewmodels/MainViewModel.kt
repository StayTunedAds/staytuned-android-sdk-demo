package com.staytuned.demo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.staytuned.sdk.features.STLists
import com.staytuned.sdk.features.STSections
import com.staytuned.sdk.http.STHttpCallback
import com.staytuned.sdk.models.STSection
import com.staytuned.sdk.models.lists.STContentList
import com.staytuned.sdk.models.lists.STTrackList
import com.staytuned.sdk.models.lists.STGenericList
import com.staytuned.sdk.models.lists.STListType

class MainViewModel: ViewModel() {

    val sections: MutableLiveData<List<STSection>> = MutableLiveData()
    val myContentsList: MutableLiveData<STContentList> = MutableLiveData()
    val myTracksList: MutableLiveData<STTrackList> = MutableLiveData()

    fun getSections() {
        STSections.getInstance()?.getSections(object : STHttpCallback<List<STSection>> {
            override fun onSuccess(data: List<STSection>) {
                sections.postValue(data)
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getLists() {
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

        })
    }

    private fun createContentList() {
        STLists.getInstance()?.create( "MyContents", STListType.Favorite, object : STHttpCallback<STContentList> {
            override fun onSuccess(data: STContentList) {
                myContentsList.postValue(data)
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun createTrackList() {
        STLists.getInstance()?.create("MyTracks", STListType.Favorite, object : STHttpCallback<STTrackList> {
            override fun onSuccess(data: STTrackList) {
                myTracksList.postValue(data)
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}