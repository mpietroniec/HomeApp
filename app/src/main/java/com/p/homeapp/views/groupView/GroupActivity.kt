package com.p.homeapp.views.groupView

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.p.homeapp.R
import com.p.homeapp.adapters.GroupAdapter
import com.p.homeapp.entities.Group
import com.p.homeapp.views.groupView.createGroup.CreateGroupActivity


class GroupActivity : AppCompatActivity(), GroupAdapter.OnItemClickListener {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private val groupAdapter = GroupAdapter(initFakeGroups(), this)

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        val groupRecyclerView: RecyclerView = findViewById(R.id.id_group_recyclerView)
        groupRecyclerView.layoutManager = LinearLayoutManager(this)
        groupRecyclerView.adapter = groupAdapter

        val addGroupBtn: FloatingActionButton = findViewById(R.id.add_group_button)
        addGroupBtn.setOnClickListener {
            onAddButtonClicked()
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        val addNewGroup: FloatingActionButton = findViewById(R.id.id_add_new_group)
        val joinToGroup: FloatingActionButton = findViewById(R.id.id_join_to_group)
        if (!clicked) {
            addNewGroup.visibility = View.VISIBLE
            joinToGroup.visibility = View.VISIBLE
        } else {
            addNewGroup.visibility = View.INVISIBLE
            joinToGroup.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        val addNewGroup: FloatingActionButton = findViewById(R.id.id_add_new_group)
        val joinToGroup: FloatingActionButton = findViewById(R.id.id_join_to_group)
        val addGroupBtn: FloatingActionButton = findViewById(R.id.add_group_button)
        if (!clicked) {
            addNewGroup.startAnimation(fromBottom)
            joinToGroup.startAnimation(fromBottom)
            addGroupBtn.startAnimation(rotateOpen)
        } else {
            addNewGroup.startAnimation(toBottom)
            joinToGroup.startAnimation(toBottom)
            addGroupBtn.startAnimation(rotateClose)
        }
    }

    fun startCreateActivity(view: View) {
        val intent = Intent(this, CreateGroupActivity::class.java)
        startActivity(intent)
    }

    private fun initFakeGroups(): ArrayList<Group> {
        val fakeGroupsList = ArrayList<Group>()
        for (i in 1..3) {
            val group = Group()
            group.name = "Name: $i"
            group.description = "Description: $i"
            fakeGroupsList.add(group)
        }
        return fakeGroupsList
    }

    override fun onItemClick(position: Int) {

    }
}