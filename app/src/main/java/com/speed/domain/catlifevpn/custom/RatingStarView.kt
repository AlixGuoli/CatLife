package com.speed.domain.catlifevpn.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.speed.domain.catlifevpn.R

/**
 * @Description:
 * @Author: Alix
 * @Date: 2025/7/8
 */
class RatingStarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val starCount = 5
    private var currentRating = 0
    private val starViews = mutableListOf<ImageView>()

    var onRatingChanged: ((Int) -> Unit)? = null

    // 图片资源可改成你自己的
    private val filledStarRes = R.mipmap.ic_star_filled
    private val emptyStarRes = R.mipmap.ic_star_empty

    init {
        orientation = HORIZONTAL
        initStars()
    }

    private fun initStars() {
        val inflater = LayoutInflater.from(context)
        for (i in 0 until starCount) {
            val starView = inflater.inflate(R.layout.item_star, this, false)
            val starLay = starView.findViewById<FrameLayout>(R.id.starLay)
            val imageView = starView.findViewById<ImageView>(R.id.starImage)
            imageView.setImageResource(emptyStarRes)

            starLay.setOnClickListener {
                setRating(i + 1)
                onRatingChanged?.invoke(currentRating)
            }

            addView(starView)
            starViews.add(imageView)
        }
    }

    fun setRating(rating: Int) {
        currentRating = rating.coerceIn(0, starCount)
        updateStars()
    }

    private fun updateStars() {
        for (i in 0 until starCount) {
            starViews[i].setImageResource(
                if (i < currentRating) filledStarRes else emptyStarRes
            )
        }
    }

    fun getRating(): Int = currentRating
}
