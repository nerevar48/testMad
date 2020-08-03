package ru.test.livanov.fragment

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.test.livanov.DetailActivity
import ru.test.livanov.MainActivity
import ru.test.livanov.model.Repo
import ru.test.livanov.ui.RepoListAdapter
import ru.test.livanov.viewmodel.RepoViewModel

abstract class AbstractListFragment : Fragment() {

    open lateinit var repoListAdapter: RepoListAdapter
    lateinit var repoViewModel: RepoViewModel
    lateinit var repoSwipeToRefresh: SwipeRefreshLayout
    lateinit var reposRecycler: RecyclerView

    abstract fun fetchData()

    fun initObserve() {
        repoViewModel.getListLiveData().observe(this, Observer { result ->
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
            fetchData()
        }
    }

    abstract fun onScrollUpdate(lastId: Int)

    private fun setList() {
        reposRecycler.adapter = repoListAdapter
        repoListAdapter.callback = object : RepoListAdapter.Callback {
            override fun onItemClicked(context: Context, repo: Repo) {
                super.onItemClicked(context, repo)
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(MainActivity.KEY_REPO_OWNER_TO_DETAIL, repo.ownerName)
                intent.putExtra(MainActivity.KEY_REPO_NAME_TO_DETAIL, repo.name)
                context.startActivity(intent)
            }

            override fun onScrolledToBottom(lastId: Int) {
                super.onScrolledToBottom(lastId)
                onScrollUpdate(lastId)
            }
        }
        reposRecycler.layoutManager = LinearLayoutManager(activity as Context)
        reposRecycler.addItemDecoration(DividerItemDecoration(reposRecycler.context, DividerItemDecoration.VERTICAL))
    }


}