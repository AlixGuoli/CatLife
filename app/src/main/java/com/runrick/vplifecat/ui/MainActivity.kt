package com.runrick.vplifecat.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.VpnService
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.lifecycle.lifecycleScope
import com.elvishew.xlog.XLog
import com.hjq.language.MultiLanguages
import com.hjq.permissions.XXPermissions
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.runrick.vplifecat.R
import com.runrick.vplifecat.base.BaseActivity
import com.runrick.vplifecat.databinding.ActivityMainBinding
import com.runrick.vplifecat.dialog.DisconnectDialog
import com.runrick.vplifecat.service.StatusService
import com.runrick.vplifecat.service.V2RayServiceManager
import com.runrick.vplifecat.utils.CacheValue
import com.runrick.vplifecat.utils.Utils
import com.runrick.vplifecat.utils.hide
import com.runrick.vplifecat.utils.show
import com.runrick.vplifecat.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private var lastLocale: Locale? = null

    companion object {
        val VPN_REQUEST_CODE = 1252
        val ADD_TO_POWER_WHITELIST = 5455
        fun onNewIntent(context: Context, language: Boolean = false): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("language", language)
            return intent
        }
    }

    override fun getViewBinding(): ActivityMainBinding {
        return  ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initUI() {
        checkPermission()
        viewModel.startMsgBroadcast()
        viewModel.startStatusBroadcast()
        XLog.e("isRunning: " + CacheValue.isRunning.value)
        lastLocale = MultiLanguages.getAppLanguage()
    }

    override fun onClick() {
        binding.setting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.touch.setOnClickListener {
            if (CacheValue.isRunning.value != true) {
                startConnect()
            } else {
                showDisDialog()
                //stopConnect()
            }
        }

        CacheValue.isRunning.observe(this) {
            if (it) {
                //startActivity(Intent(this, ConnectResultActivity::class.java))
                startStatusService()
                binding.touchHint.show()
                binding.textConnectType.setText(R.string.connected)
                binding.lottie.setAnimation(R.raw.connected)
                binding.lottie.playAnimation()
            } else {
                stopStatusService()
                binding.touchHint.show()
                binding.textConnectType.setText(R.string.disconnected)
                binding.lottie.setAnimation(R.raw.disconnected)
                binding.lottie.playAnimation()
            }
        }

        viewModel.connectTime.observe(this) {
            binding.connectTime.text = it
        }
    }

    private fun startConnect() {
        startVPN()
    }

    private fun stopConnect() {
        stopVPN()
    }

    fun startVPN() {
        val intent = VpnService.prepare(this)
        if (intent != null) {
            XLog.e("Start 1")
            startActivityForResult(intent, VPN_REQUEST_CODE)
        } else {
            XLog.e("Start 2")
            startVPNService()
        }
    }

    private fun startVPNService() {
        if (checkPowerWhitelist()) {
            binding.touchHint.hide()
            binding.textConnectType.setText(R.string.connecting)
            binding.lottie.setAnimation(R.raw.connecting)
            binding.lottie.playAnimation()
            V2RayServiceManager.startV2Ray(this)
        }
    }

    private fun stopVPN() {
        if (CacheValue.isRunning.value == true) {
            binding.connectTime.text = ""
            binding.touchHint.hide()
            binding.textConnectType.setText(R.string.stopping)
            binding.lottie.setAnimation(R.raw.connecting)
            binding.lottie.playAnimation()
            Utils.stopVService(this)
        }
    }

    private fun showDisDialog() {
        XPopup.Builder(this)
            .isDarkTheme(true)
            .dismissOnTouchOutside(false)
            .isDestroyOnDismiss(true)
            .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
            .asCustom(DisconnectDialog(this, object : DisconnectDialog.DisconnectDialogListener {
                override fun onConfirm(dialog: DisconnectDialog) {
                    stopConnect()
                    dialog.dismiss()
                }
            }))
            .show()
    }

    private fun checkPermission() {
        if (!CacheValue.PERMISSION_NOTIFICATIONS) {
            XLog.e("POST_NOTIFICATIONS false")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                XXPermissions.with(this)
                    .permission(Manifest.permission.POST_NOTIFICATIONS)
                    .request { _, allGranted ->
                        if (!allGranted) {
                            XLog.e("POST_NOTIFICATIONS not agree")
                            return@request
                        }
                        XLog.e("POST_NOTIFICATIONS agree")
                        CacheValue.PERMISSION_NOTIFICATIONS = true
                    }
            }
        }
        else {
            XLog.e("POST_NOTIFICATIONS true")
        }
    }

    private fun checkPowerWhitelist(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = this.getSystemService(POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(this.packageName) &&
                !CacheValue.KEY_IGNORE_POWER_WHITELIST
            ) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.permission_battery_title)
                    .setMessage(R.string.permission_battery_msg)
                    .setCancelable(false)
                    .setPositiveButton(
                        android.R.string.ok
                    ) { dialog, _ ->

                        val intent = Intent(
                            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                            Uri.parse("package:" + packageName)                        )
                        startActivity(intent)
                        onActivityResult(ADD_TO_POWER_WHITELIST, RESULT_OK, null)
                        dialog?.dismiss()

                    }
                    .create().show()

                return false
            }
        }
        return true
    }

    fun startStatusService() {
        startService(Intent(this, StatusService::class.java))
    }

    fun stopStatusService() {
        stopService(Intent(this, StatusService::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            XLog.e("RESULT_OK")
            when(requestCode) {
                VPN_REQUEST_CODE -> {
                    startVPNService()
                }
                ADD_TO_POWER_WHITELIST -> {
//                    startActivity(Intent(this, ConnectActivity::class.java))
//                    VPServiceManager.startV2Ray(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val currentLocale = MultiLanguages.getAppLanguage()
        if (currentLocale != lastLocale) {
            finish()
            startActivity(onNewIntent(this, true))
            lastLocale = currentLocale
        }
    }

}