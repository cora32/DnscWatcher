package org.iskopasi.noname

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import org.iskopasi.noname.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val registry by lazy { LifecycleRegistry(this) }
    private lateinit var binding: ActivityMainBinding
    private val adapter = Adapter(this)
    private val sortClHeight by lazy { resources.getDimensionPixelSize(R.dimen.sort_cl_height) }
    private val transition by lazy { ChangeBounds() }

    override fun getLifecycle(): LifecycleRegistry = registry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val model = ViewModelProviders.of(this).get(DataModel::class.java)
        model.liveData.observe(this, Observer<List<DnscItem>> { list ->
            if (list == null) return@Observer

            if (list.isNotEmpty()) {
                if (R.id.rv == binding.switcher.nextView.id) {
                    binding.switcher.showNext()
                }

                adapter.dataList.clear()
                adapter.dataList.addAll(list)
            } else if (R.id.text_empty == binding.switcher.nextView.id) {
                binding.switcher.showNext()
            }

            binding.srl.isRefreshing = false
        })

        binding.switcher.inAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.switcher.outAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        binding.sortRadioGroup.setOnCheckedChangeListener({ _, id ->
            //TODO: use sortlist's compare()
            if (adapter.dataList.size() == 0)
                return@setOnCheckedChangeListener

            val list = ArrayList<DnscItem>()
            (0 until adapter.dataList.size()).mapTo(list) { adapter.dataList.get(it) }
            when (id) {
                R.id.name_rb -> model.sortBy(list, DnscItem::name)
                R.id.no_logs_rb -> model.sortBy(list, DnscItem::noLogs)
                R.id.online_rb -> model.sortBy(list, DnscItem::online)
            }
        })

        setSupportActionBar(binding.toolbar)

        adapter.setHasStableIds(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = null
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(this, (binding.rv.layoutManager as LinearLayoutManager).orientation))

        binding.srl.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        binding.srl.setOnRefreshListener {
            model.getNewData()
        }
    }

    private fun toggleSortLayout() {
        if (binding.sortCl.layoutParams.height == sortClHeight)
            binding.sortCl.layoutParams.height = 0
        else
            binding.sortCl.layoutParams.height = sortClHeight

        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)
        binding.sortCl.requestLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort -> toggleSortLayout()
        }

        return super.onOptionsItemSelected(item)
    }
}
