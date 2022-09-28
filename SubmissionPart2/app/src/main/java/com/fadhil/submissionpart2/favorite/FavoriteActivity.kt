package com.fadhil.submissionpart2.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhil.submissionpart2.Database.FavoriteUser
import com.fadhil.submissionpart2.User.User
import com.fadhil.submissionpart2.User.UserActivityDetail
import com.fadhil.submissionpart2.User.UserAdapter
import com.fadhil.submissionpart2.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = "Favorite User"

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                intent = Intent (this@FavoriteActivity, UserActivityDetail::class.java)
                with(intent) {
                    putExtra(UserActivityDetail.EXTRA_USERNAME,data.login)
                    putExtra(UserActivityDetail.EXTRA_ID,data.url)
                    putExtra(UserActivityDetail.EXTRA_URL,data.avatar_url)
                }

                startActivity(intent)
            }

        })
        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUser.adapter = adapter
        }
        viewModel.getFavoriteUser()?.observe(this,{
            if (it != null){
                val list = mapList(it)
                adapter.setList(list)
            }
        })
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users) {
            val userMapped = User (
                user.login,
                user.id.toString(),
                user.avatar_url
                    )
            listUsers.add(userMapped)
        }
        return listUsers

    }
}