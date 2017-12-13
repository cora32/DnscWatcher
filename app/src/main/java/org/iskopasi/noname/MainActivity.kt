package org.iskopasi.noname

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import org.iskopasi.noname.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val registry by lazy { LifecycleRegistry(this) }
    private lateinit var binding: ActivityMainBinding
    private val adapter = Adapter(this)

    override fun getLifecycle(): LifecycleRegistry = registry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val model = ViewModelProviders.of(this).get(DataModel::class.java)
        model.liveData.observe(this, Observer<List<DnscItem>> { list ->
            if (list == null) return@Observer

            adapter.dataList.clear()
            adapter.dataList.addAll(list)
        })

        setSupportActionBar(binding.toolbar)

        adapter.setHasStableIds(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = null
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(this, (binding.rv.layoutManager as LinearLayoutManager).orientation))

        binding.srl.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
        binding.srl.setOnRefreshListener {
            model.liveData.value
            binding.srl.isRefreshing = false
        }
    }
}
