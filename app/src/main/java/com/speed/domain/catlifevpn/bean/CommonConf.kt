package com.speed.domain.catlifevpn.bean

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/7/11
 */
data class CommonConf(
    var IPWhiteList: List<String?>?,
    var latestClientVersion: String?,
    var detectionConfig :DetectionConfig?,
    val special_ad_policy:Int?,
    val logURL:List<String?>?,
    val git_version : Int?
)