package com.runrick.vplifecat.dialog

import android.content.Context
import com.lxj.xpopup.core.BasePopupView
import com.runrick.vplifecat.R
import com.runrick.vplifecat.databinding.DialogDisconnectBinding

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/7/4
 */
class DisconnectDialog(context: Context, val listener: DisconnectDialogListener) : BasePopupView(context) {

    lateinit var binding: DialogDisconnectBinding

    override fun getInnerLayoutId(): Int {
        return R.layout.dialog_disconnect
    }

    override fun onCreate() {
        super.onCreate()

        binding = DialogDisconnectBinding.bind(rootView.findViewById(R.id.dialogLay))

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnYes.setOnClickListener {
            listener.onConfirm(this)
        }
    }

    interface DisconnectDialogListener {
        fun onConfirm(dialog: DisconnectDialog)
    }
}