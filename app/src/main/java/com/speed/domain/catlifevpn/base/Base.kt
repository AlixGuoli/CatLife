package com.speed.domain.catlifevpn.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/6/10
 */

// BaseActivity
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM

    abstract fun getViewBinding(): VB
    abstract fun getViewModelClass(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentStatusBar().init()
        binding = getViewBinding()
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        initUI()
        onClick()
    }

    abstract fun initUI()
    abstract fun onClick()
}

// BaseFragment
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {
    protected var _binding: VB? = null
    protected val binding get() = _binding!!
    protected lateinit var viewModel: VM

    abstract fun getViewBinding(): VB
    abstract fun getViewModelClass(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[getViewModelClass()]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// BaseViewModel
open class BaseViewModel : ViewModel() {

}
