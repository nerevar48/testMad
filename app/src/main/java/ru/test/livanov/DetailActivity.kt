package ru.test.livanov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import ru.test.livanov.databinding.RepoDetailBinding
import ru.test.livanov.ui.RepoCommitListAdapter
import ru.test.livanov.viewmodel.RepoCommitViewModel
import ru.test.livanov.viewmodel.RepoDetailViewModel
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    @Inject
    lateinit var repoCommitListAdapter: RepoCommitListAdapter

    /**
     *  вероятнее всего детальное отображение должно быть реализовано через фрагменты что бы лишний
     *  раз не запрашивать данные, но решил сделать через активити как самый простой и топорный вариант
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        App.component.injectsDetailActivity(this)


        val repoDetailViewModel = ViewModelProviders.of(this).get(RepoDetailViewModel::class.java)
        repoDetailViewModel.ownerName = intent.getStringExtra(MainActivity.KEY_REPO_OWNER_TO_DETAIL)
        repoDetailViewModel.name = intent.getStringExtra(MainActivity.KEY_REPO_NAME_TO_DETAIL)
        val binding: RepoDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        repoDetailViewModel.getLiveData().observe(this, Observer {
            binding.notifyChange()
        })

        repoDetailViewModel.fetchRepoDetail().observe(this, Observer {
            progressBar.visibility = ProgressBar.INVISIBLE
            binding.repoDetailModel = repoDetailViewModel
            binding.executePendingBindings()
            Glide.with(ownerAvatar).load(repoDetailViewModel.repoDetail.owner.avatarUrl).into(ownerAvatar)
        })

        val repoCommitViewModel = ViewModelProviders.of(this).get(RepoCommitViewModel::class.java)
        repoCommitViewModel.ownerName = intent.getStringExtra(MainActivity.KEY_REPO_OWNER_TO_DETAIL)
        repoCommitViewModel.name = intent.getStringExtra(MainActivity.KEY_REPO_NAME_TO_DETAIL)
        repoCommitViewModel.fetchRepoCommits().observe(this, Observer {
            repoCommitListAdapter.reposViewModelList = it
            setList()
        })

    }

    private fun setList() {
        commitsRecycler.adapter = repoCommitListAdapter
        commitsRecycler.layoutManager = LinearLayoutManager(this)
        commitsRecycler.addItemDecoration(DividerItemDecoration(commitsRecycler.context, DividerItemDecoration.VERTICAL))
    }
}