package com.speed.domain.catlifevpn.bean

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/7/11
 */
data class DetectionConfig(
    var detectionInterval: Int?,
    var detectionServers: List<String?>?,
    var detectionThreshold: Int?
)
