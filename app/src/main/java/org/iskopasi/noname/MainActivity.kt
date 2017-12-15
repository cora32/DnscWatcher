package org.iskopasi.noname

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.animation.AnimationUtils
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

        setSupportActionBar(binding.toolbar)

        adapter.setHasStableIds(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = null
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(this, (binding.rv.layoutManager as LinearLayoutManager).orientation))

        binding.srl.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
        binding.srl.setOnRefreshListener {
            model.getNewData()
        }
    }
}
