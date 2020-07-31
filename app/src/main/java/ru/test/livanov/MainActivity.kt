package ru.test.livanov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.test.livanov.viewmodel.RepoViewModel
import ru.test.livanov.model.Repo
import ru.test.livanov.ui.RepoListAdapter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repoListAdapter: RepoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.injectsMainActivity(this)

        repoListAdapter.context = this

        val repoViewModel = ViewModelProviders.of(this).get(RepoViewModel::class.java)
        repoViewModel.fetchRepos().observe(this, Observer {repoViewModelList ->
            repoListAdapter.reposViewModelList = repoViewModelList
            setList()
            repoViewModelList?.forEach {
                it.getLiveData().observe(this, Observer {
                    repoListAdapter.notifyDataSetChanged()
                })
                it.fetchRepoDetail()
                it.fetchRepoCommitsCount()
            }
        })

    }

    private fun setList() {
        reposRecycler.adapter = repoListAdapter
        reposRecycler.layoutManager = LinearLayoutManager(this)
    }

}