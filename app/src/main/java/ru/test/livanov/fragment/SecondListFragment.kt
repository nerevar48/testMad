package ru.test.livanov.fragment

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.test.livanov.App
import ru.test.livanov.R
import ru.test.livanov.ui.RepoListAdapter
import ru.test.livanov.ui.SwipeController
import ru.test.livanov.ui.SwipeControllerActions
import ru.test.livanov.viewmodel.RepoDbViewModel
import javax.inject.Inject

class SecondListFragment : AbstractListFragment() {

    @Inject
    override lateinit var repoListAdapter: RepoListAdapter
    @Inject
    lateinit var swipeController: SwipeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component.injectsSecondFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentLayout = inflater.inflate(R.layout.fragment_main_list, container, false)

        repoListAdapter.context = (activity as Context)
        repoViewModel = ViewModelProviders.of(this).get(RepoDbViewModel::class.java)

        reposRecycler = fragmentLayout.findViewById(R.id.reposRecycler)
        repoSwipeToRefresh = fragmentLayout.findViewById(R.id.repoSwipeToRefresh)

        initObserve()
        fetchData()

        swipeController.showAdd = false
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(reposRecycler)
        reposRecycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
        swipeController.buttonsActions = object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {
                (repoViewModel as RepoDbViewModel).delete(repoListAdapter.reposViewModelList[position].repo)
                repoListAdapter.reposViewModelList.removeAt(position)
                repoListAdapter.notifyItemRemoved(position)
            }
        }

        return fragmentLayout
    }

    override fun fetchData() {
        (repoViewModel as RepoDbViewModel).fetchAll()
    }

}