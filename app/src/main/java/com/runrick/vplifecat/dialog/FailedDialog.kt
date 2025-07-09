package com.runrick.vplifecat.dialog

import android.content.Context
import com.lxj.xpopup.core.BasePopupView
import com.runrick.vplifecat.R
import com.runrick.vplifecat.databinding.DialogFailedBinding

/**
    * @Description: 
    * @Author: Alix
    * @Date: 2025/7/8
 */
class FailedDialog(context: Context, val listener: FailedListener) : BasePopupView(context) {

    lateinit var binding: DialogFailedBinding

    override fun getInnerLayoutId(): Int {
        return R.layout.dialog_failed
    }

    override fun onCreate() {
        super.onCreate()

        binding = DialogFailedBinding.bind(rootView.findViewById(R.id.dialogLay))

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSupport.setOnClickListener {
            listener.onSupport(this)
        }

        binding.btnTg.setOnClickListener {
            listener.onTg(this)
        }
    }


    interface FailedListener {
        fun onSupport(dialog: FailedDialog)

        fun onTg(dialog: FailedDialog)
    }
}