package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the story item
        val story = intent.getParcelableExtra<ListStoryItem>("story")
        story?.let {
            binding.textViewName.text = it.name
            binding.textViewDescription.text = it.description
            // Assume you have an ImageView and want to load the photo
            Glide.with(this)
                .load(it.photoUrl)
                .placeholder(R.drawable.image_dicoding)
                .into(binding.imageView)
        } ?: run {
            Toast.makeText(this, "Story details not available", Toast.LENGTH_SHORT).show()
            finish()  // Finish activity if no data is available
        }
    }
}
