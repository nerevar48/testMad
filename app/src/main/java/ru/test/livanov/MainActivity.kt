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
    lateinit var repoViewModel: RepoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.component.injectsMainActivity(this)

        repoListAdapter.context = this

        repoViewModel = ViewModelProviders.of(this).get(RepoViewModel::class.java)

        fetchList()

    }

    private fun fetchList() {
        repoViewModel.fetchRepos().observe(this, Observer { result ->
            val repoViewModelList = result.first
            if (!result.second) {
                setList()
                if (repoSwipeToRefresh.isRefreshing)
                    repoSwipeToRefresh.isRefreshing = false
            }

            repoViewModelList.forEachIndexed { index, it ->
                repoListAdapter.reposViewModelList.add(it)
                repoListAdapter.notifyItemInserted(repoListAdapter.itemCount-1)

                it.getLiveData().observe(this, Observer {
                    repoListAdapter.notifyItemChanged(index)
                })
                it.fetchRepoDetail()
                it.fetchRepoCommitsCount()
            }
        })

        repoSwipeToRefresh.setOnRefreshListener {
            repoViewModel.cancelAllRequests()
            repoViewModel.fetchRepos()
        }
    }


    private fun setList() {
        reposRecycler.adapter = repoListAdapter
        repoListAdapter.callback = object : RepoListAdapter.Callback {
            override fun onItemClicked(context: Context, repo: Repo) {
                super.onItemClicked(context, repo)
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(KEY_REPO_OWNER_TO_DETAIL, repo.ownerName)
                intent.putExtra(KEY_REPO_NAME_TO_DETAIL, repo.name)
                context.startActivity(intent)
            }

            override fun onScrolledToBottom(lastId: Int) {
                super.onScrolledToBottom(lastId)
                repoViewModel.fetchRepos(lastId)
            }

        }
        reposRecycler.layoutManager = LinearLayoutManager(this)
        reposRecycler.addItemDecoration(DividerItemDecoration(reposRecycler.context, DividerItemDecoration.VERTICAL))
    }

}