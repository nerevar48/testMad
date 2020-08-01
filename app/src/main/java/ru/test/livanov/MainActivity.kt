package ru.test.livanov

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.test.livanov.viewmodel.RepoViewModel
import ru.test.livanov.model.Repo
import ru.test.livanov.ui.RepoListAdapter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_REPO_OWNER_TO_DETAIL = "repo_owner_to_detail"
        const val KEY_REPO_NAME_TO_DETAIL = "repo_name_to_detail"
    }

    @Inject
    lateinit var repoListAdapter: RepoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.injectsMainActivity(this)

        repoListAdapter.context = this

        val repoViewModel = ViewModelProviders.of(this).get(RepoViewModel::class.java)
        loadReposList(repoViewModel)

        repoSwipeToRefresh.setOnRefreshListener {
            repoSwipeToRefresh.isRefreshing = true
            repoViewModel.cancelAllRequests()
            loadReposList(repoViewModel)
        }

    }

    private fun loadReposList(repoViewModel: RepoViewModel) {
        repoViewModel.fetchRepos().observe(this, Observer { repoViewModelList ->
            repoListAdapter.reposViewModelList = repoViewModelList
            setList()

            repoViewModelList?.forEachIndexed { index, it ->
                it.getLiveData().observe(this, Observer {
                    repoListAdapter.notifyItemChanged(index)
                    if (repoSwipeToRefresh.isRefreshing)
                        repoSwipeToRefresh.isRefreshing = false
                })
                it.fetchRepoDetail()
                it.fetchRepoCommitsCount()
            }
        })
    }

    private fun setList() {
        reposRecycler.adapter = repoListAdapter
        repoListAdapter.callback = object : RepoListAdapter.Callback {
            override fun onItemClicked(context: Context, repo: Repo) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(KEY_REPO_OWNER_TO_DETAIL, repo.ownerName)
                intent.putExtra(KEY_REPO_NAME_TO_DETAIL, repo.name)
                context.startActivity(intent)
            }
        }
        reposRecycler.layoutManager = LinearLayoutManager(this)
        reposRecycler.addItemDecoration(DividerItemDecoration(reposRecycler.context, DividerItemDecoration.VERTICAL))
    }

}