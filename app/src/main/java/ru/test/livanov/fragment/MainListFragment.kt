package ru.test.livanov.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import ru.test.livanov.App
import ru.test.livanov.R
import ru.test.livanov.ui.RepoListAdapter
import ru.test.livanov.viewmodel.RepoViewModel
import javax.inject.Inject

class MainListFragment : AbstractListFragment() {

    @Inject
    override lateinit var repoListAdapter: RepoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component.injectsMainFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentLayout = inflater.inflate(R.layout.fragment_main_list, container, false)

        repoListAdapter.context = (activity as Context)
        repoViewModel = ViewModelProviders.of(this).get(RepoViewModel::class.java)

        reposRecycler = fragmentLayout.findViewById(R.id.reposRecycler)
        repoSwipeToRefresh = fragmentLayout.findViewById(R.id.repoSwipeToRefresh)

        initObserve()
        fetchData()

        return fragmentLayout
    }

    override fun fetchData() {
        repoViewModel.fetchRepos()
    }

    override fun onScrollUpdate(lastId: Int) {
        repoViewModel.fetchRepos(lastId)
    }

}