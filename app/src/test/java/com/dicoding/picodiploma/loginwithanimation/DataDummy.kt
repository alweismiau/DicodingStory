package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem>{
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100){
            val story = ListStoryItem(
                i.toString(),
                "Description $i",
                "Title $i",
                "https://picsum.photos/200/300?random=$i"
            )
            items.add(story)
        }
        return items
    }
}