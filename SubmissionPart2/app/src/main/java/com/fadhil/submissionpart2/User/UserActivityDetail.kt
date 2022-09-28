package com.fadhil.submissionpart2.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.fadhil.submissionpart2.Main.MainViewModel
import com.fadhil.submissionpart2.dll.PageAdapter
import com.fadhil.submissionpart2.R
import com.fadhil.submissionpart2.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserActivityDetail : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: MainViewModel

    companion object{
        const val EXTRA_USERNAME= "extra_username"
        const val  EXTRA_ID = "extra_id"
        const val  EXTRA_URL = "extra_url"
        private val TAB_TITTLE = intArrayOf(
            R.string.follower,
            R.string.following


        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = "Detail User"

        viewModel = ViewModelProvider(this).get(
            MainViewModel::class.java)

        val username = intent.getStringExtra(EXTRA_USERNAME)?:"null"
       val id = intent.getIntExtra(EXTRA_ID,0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)?:"null"
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME,username)
        viewModel.setDetail(username)
        Log.d("Detail","Username : $username")
        viewModel.getUserDetail().observe(this,{
           if (it != null) {
               binding.apply {
                   Glide.with(this@UserActivityDetail)
                       .load(it.avatar_url)
                       .circleCrop()
                       .into(circleImageView)

                   textname.text = it.name
                   usernamed.text = it.login
                   textcompany.text =
                       if (it.company != null) it.company else " Tidak Memiliki Perusahaan "
                   textlocation.text = if (it.location != null) it.location else " Tidak Ada Memiliki Lokasi"
                   detailfollower.text = "${it.followers}"
                   detailfollowing.text = "${it.following}"
                   textrepository.text =
                       if (it.bio != null) it.bio else " Tidak Memiliki Repository "

               }
           }
        })

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null){
                    if (count > 0){
                        binding.toggleButton.isChecked = true

                        _isChecked = true
                    } else{
                        binding.toggleButton.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleButton.setOnClickListener{
            _isChecked =! _isChecked
            if (_isChecked){
                viewModel.addToFavorite(username, id, avatarUrl)
            } else{
                viewModel.removeFromFavorite(id)
            }
            binding.toggleButton.isChecked = _isChecked
        }

        val folAdapter = PageAdapter(this,bundle)
        val viewPager: ViewPager2 = findViewById(R.id.viewpager)
        viewPager.adapter = folAdapter
        val tabs: TabLayout = findViewById(R.id.table)
        TabLayoutMediator(tabs,viewPager){tab,position -> tab.text = resources.getString(TAB_TITTLE[position])}.attach()
        supportActionBar?.elevation = 0f


    }
}