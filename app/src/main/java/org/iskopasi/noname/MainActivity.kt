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
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import org.iskopasi.noname.adapters.DnsAdapter
import org.iskopasi.noname.databinding.ActivityMainBinding
import org.iskopasi.noname.entities.DnscItem
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {
    private val registry by lazy { LifecycleRegistry(this) }
    private val layoutManager by lazy { ScrollSwitchingLayoutManager(this) }
    private val sortClHeight by lazy { resources.getDimensionPixelSize(R.dimen.sort_cl_height) }
    private val transition by lazy { ChangeBounds() }
    private val adapter = DnsAdapter(this, layoutManager, compareBy(DnscItem::name))
    private lateinit var binding: ActivityMainBinding

    override fun getLifecycle(): LifecycleRegistry = registry

    override fun onCreate(savedInstanceState: Bundle?) {
        //setting main theme to replace splash screen
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Setting up actionbar
        setSupportActionBar(binding.toolbar)

        //Setting up model and observer
        val model = ViewModelProviders.of(this).get(DataModel::class.java)
        model.liveData.observe(this, Observer<List<DnscItem>> { list ->
            binding.srl.isRefreshing = false

            if (list == null) {
                toast(getString(R.string.check_inet))
                return@Observer
            }

            if (list.isNotEmpty()) {
                if (R.id.rv == binding.switcher.nextView.id) {
                    binding.switcher.showNext()
                }

                adapter.dataList.clear()
                adapter.dataList.addAll(list)

                checkOnlineNodes(model)
            } else if (R.id.text_empty == binding.switcher.nextView.id) {
                binding.switcher.showNext()
            }
        })

        //Setting up adapter and recyclerview settings
        adapter.setHasStableIds(true)
        binding.rv.layoutManager = layoutManager
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = null
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(this, (binding.rv.layoutManager as LinearLayoutManager).orientation))

        //Setting up switcher
        binding.switcher.inAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.switcher.outAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        //Setting up radio buttons click actions
        binding.sortRadioGroup.setOnCheckedChangeListener({ _, id ->
            when (id) {
                R.id.name_rb -> adapter.setComparator(compareBy(DnscItem::name))
                R.id.no_logs_rb -> adapter.setComparator(compareByDescending(DnscItem::noLogs).thenBy(DnscItem::name))
                R.id.online_rb -> adapter.setComparator(compareByDescending(DnscItem::online).thenBy(DnscItem::name))
            }
            model.getCachedData()
        })

        //Setting up swiperefreshlayout settings
        binding.srl.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        binding.srl.setOnRefreshListener {
            model.getNewData()
        }
    }

    private fun checkOnlineNodes(model: DataModel) {
        doAsync {
            for (i in 0..adapter.dataList.size()) {
                if (!Utils.isNetworkUp(this@MainActivity)) {
                    toast(getString(R.string.check_inet))
                    break
                }

                val item = adapter.dataList.get(i)
                val isOnline = model.checkOnline(item.ip)
                binding.rv.post({
                    val onlineView: View? = binding.rv.findViewHolderForItemId(i.toLong())
                            ?.itemView?.findViewById(R.id.online_view)
                    if (isOnline) {
                        item.online = 1
                        Utils.animateDrawableColor(this@MainActivity, R.color.checking,
                                R.color.online, onlineView, 1000)
                    } else {
                        item.online = -1
                        Utils.animateDrawableColor(this@MainActivity, R.color.checking,
                                R.color.offline, onlineView, 1000)
                    }
                })
            }
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
