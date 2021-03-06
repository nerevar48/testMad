package ru.test.livanov.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.test.livanov.R
import ru.test.livanov.databinding.RepoBinding
import ru.test.livanov.model.Repo
import ru.test.livanov.viewmodel.AbstractRepoViewModel

class RepoListAdapter: RecyclerView.Adapter<RepoListAdapter.RepoListView>() {

    lateinit var callback: Callback
    lateinit var context: Context
    var reposViewModelList: MutableList<AbstractRepoViewModel> = mutableListOf()

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
        /**
         * выбран как самый простой вариант, в идеале конечно нужно грузить заранее что бы разрыв был минимульным для пользователя
         */
        if (position == itemCount-1) {
            callback.onScrolledToBottom(reposViewModelList[itemCount -1].repo.id)
        }
    }

    inner class RepoListView(val repoBinding: RepoBinding): RecyclerView.ViewHolder(repoBinding.root) {

        fun bind(repoViewModel: AbstractRepoViewModel) {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    callback.onItemClicked(itemView.context, reposViewModelList[adapterPosition].repo)

            }
            this.repoBinding.repoModel = repoViewModel
            repoBinding.executePendingBindings()
        }

    }

    interface Callback {
        fun onItemClicked(context: Context, repo: Repo) {}
        fun onScrolledToBottom(lastId: Int) {}
    }

}