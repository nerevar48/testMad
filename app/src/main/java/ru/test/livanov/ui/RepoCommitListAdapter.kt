package ru.test.livanov.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.test.livanov.R
import ru.test.livanov.databinding.CommitBinding
import ru.test.livanov.viewmodel.RepoCommitViewModel

class RepoCommitListAdapter: RecyclerView.Adapter<RepoCommitListAdapter.RepoCommitListView>() {

    lateinit var reposViewModelList: List<RepoCommitViewModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoCommitListAdapter.RepoCommitListView {
        val layoutInflater = LayoutInflater.from(parent.context)

        val commitBinding: CommitBinding = DataBindingUtil.inflate(layoutInflater, R.layout.repo_commit_item, parent, false)

        return RepoCommitListView(commitBinding)
    }

    override fun getItemCount(): Int {
        return reposViewModelList.size
    }

    override fun onBindViewHolder(holder: RepoCommitListAdapter.RepoCommitListView, position: Int) {
        holder.bind(reposViewModelList[position])
    }

    inner class RepoCommitListView(val commitBinding: CommitBinding): RecyclerView.ViewHolder(commitBinding.root) {

        fun bind(repoCommitViewModel: RepoCommitViewModel) {
            this.commitBinding.repoCommitModel = repoCommitViewModel
            commitBinding.executePendingBindings()
        }

    }
}