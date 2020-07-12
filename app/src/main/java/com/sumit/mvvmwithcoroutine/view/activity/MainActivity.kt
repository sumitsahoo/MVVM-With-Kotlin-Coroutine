package com.sumit.mvvmwithcoroutine.view.activity

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.sumit.mvvmwithcoroutine.R
import com.sumit.mvvmwithcoroutine.model.Resource
import com.sumit.mvvmwithcoroutine.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        context = this
        initViewModel()
        setupEventListeners()
        observeUserDetails()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun setupEventListeners() {
        fab.setOnClickListener {
            fetchUser((1..50).shuffled().first().toString())
        }
    }

    private fun observeUserDetails() {

        viewModel.user.observe(this, Observer {

            when (it) {

                is Resource.Loading<Any> -> {
                    tv_user.text = getString(R.string.msg_loading)
                }

                is Resource.Success<User> -> {
                    Glide
                        .with(context)
                        .load(it.data.avatar)
                        .centerCrop()
                        .placeholder(R.drawable.ic_loading)
                        .apply(RequestOptions.circleCropTransform())
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progress_bar.visibility = GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progress_bar.visibility = GONE
                                return false
                            }
                        })
                        .into(iv_user_profile)

                    tv_user.text = it.data.toString()
                }

                is Resource.Error -> {
                    Glide
                        .with(context)
                        .load(R.mipmap.ic_launcher)
                        .centerCrop()
                        .placeholder(R.drawable.ic_loading)
                        .apply(RequestOptions.circleCropTransform())
                        .into(iv_user_profile)

                    tv_user.text = getString(R.string.msg_network_error)
                }

            }
        })
    }

    private fun fetchUser(userId: String) {
        progress_bar.visibility = VISIBLE
        viewModel.setUserId(userId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.cancelJobs()
    }
}
