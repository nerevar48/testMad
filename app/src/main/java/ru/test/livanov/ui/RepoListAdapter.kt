package ru.test.livanov.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import ru.test.livanov.MainActivity
import ru.test.livanov.R
import ru.test.livanov.databinding.RepoBinding
import ru.test.livanov.viewmodel.RepoViewModel

class RepoListAdapter: RecyclerView.Adapter<RepoListAdapter.RepoListView>() {

    lateinit var context: Context
    lateinit var reposViewModelList: List<RepoViewModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoListView {
        val layoutInflater = LayoutInflater.from(parent.context)

        val repoBinding: RepoBinding = DataBindingUtil.inflate(layoutInflater, R.layout.repo_item, parent, false)

        return RepoListView(repoBinding)
    }

    override fun getItemCount(): Int {
        return reposViewModelList.size
    }

    override fun onBindViewHolder(holder: RepoListView, position: Int) {
        holder.bind(reposViewModelList[position])
    }

    class RepoListView(val repoBinding: RepoBinding): RecyclerView.ViewHolder(repoBinding.root) {

        fun bind(repoViewModel: RepoViewModel) {
            this.repoBinding.repoModel = repoViewModel
            repoBinding.executePendingBindings()
        }

    }
}