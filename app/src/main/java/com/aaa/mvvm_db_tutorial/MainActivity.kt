package com.aaa.mvvm_db_tutorial

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.aaa.mvvm_db_tutorial.adapter.RecyclerViewAdapter
import com.aaa.mvvm_db_tutorial.db.UserEntity
import com.aaa.mvvm_db_tutorial.viewmodel.ViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var viewModel: ViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(divider)
        }
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        viewModel.getAllUserObservers().observe(this, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val phone = phoneInput.text.toString()

            if (saveButton.text == "Save") {
                val user =  UserEntity(0, name, email, phone)
                viewModel.insertUserInfo(user)
            } else {
                val user =  UserEntity(nameInput.getTag(nameInput.id).toString().toInt(), name, email, phone)
                viewModel.updateUserInfo(user)
                saveButton.text = "Save"
            }
            nameInput.setText("")
            emailInput.setText("")
            phoneInput.setText("")
        }
    }

    override fun onDeleteUserClickListener(user: UserEntity) {
        viewModel.deleteUserInfo(user)
    }

    @SuppressLint("SetTextI18n")
    override fun onItemClickListener(user: UserEntity) {
        nameInput.setText(user.name)
        emailInput.setText(user.email)
        phoneInput.setText(user.phone)
        nameInput.setTag(nameInput.id, user.id)
        saveButton.text = "Update"
    }
}