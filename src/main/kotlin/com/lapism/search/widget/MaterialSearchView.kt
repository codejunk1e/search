package com.lapism.search.widget

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.card.MaterialCardView
import com.lapism.search.R
import com.lapism.search.internal.SearchLayout


class MaterialSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : SearchLayout(context, attrs, defStyleAttr, defStyleRes), CoordinatorLayout.AttachedBehavior {

    // *********************************************************************************************
    private var mBehavior: CoordinatorLayout.Behavior<*> = SearchBehavior<MaterialSearchView>()
    private var mStrokeWidth: Int = 0
    private var mElevation: Float = 0f
    private var mRadius: Float = 0f
    private var mTransition: LayoutTransition

    // *********************************************************************************************
    init {
        inflate(context, R.layout.search_view, this)
        init()

        val a = context.obtainStyledAttributes(
            attrs, R.styleable.MaterialSearchView, defStyleAttr, defStyleRes
        )
        navigationIconSupport =
            a.getInteger(
                R.styleable.MaterialSearchView_search_navigation_icon_support,
                NavigationIconSupport.NONE
            )
        a.recycle()

        mTransition = LayoutTransition()
        mTransition.enableTransitionType(LayoutTransition.CHANGING)
        mTransition.addTransitionListener(object : LayoutTransition.TransitionListener {
            override fun startTransition(
                transition: LayoutTransition?,
                container: ViewGroup?,
                view: View?,
                transitionType: Int
            ) {

            }

            override fun endTransition(
                transition: LayoutTransition?,
                container: ViewGroup?,
                view: View?,
                transitionType: Int
            ) {
                if (view is MaterialCardView) {
                    if (hasFocus()) {
                        setBackgroundRadius(
                            resources.getDimensionPixelSize(R.dimen.search_radius_focus).toFloat()
                        )
                        showKeyboard()
                        mSearchAnimationLayout?.layoutTransition = null
                        mMaterialCardView?.layoutTransition = null
                    }
                }
            }
        })

        mSearchAnimationLayout?.layoutTransition = mTransition
        mMaterialCardView?.layoutTransition = mTransition

        // TODO - MORE ATTRIBUTTES IN THE FUTURE RELEASE
        setClearIconImageResource(R.drawable.search_ic_outline_clear_24px)
        elevation = context.resources.getDimensionPixelSize(R.dimen.search_elevation).toFloat()
        setBackgroundRadius(resources.getDimensionPixelSize(R.dimen.search_radius).toFloat())
        setTransitionDuration(
            context.resources.getInteger(R.integer.search_animation_duration).toLong()
        )
    }

    // *********************************************************************************************
    override fun addFocus() {
        mStrokeWidth = getBackgroundStrokeWidth()
        mElevation = elevation
        mRadius = getBackgroundRadius()

        val left = context.resources.getDimensionPixelSize(R.dimen.search_dp_16)
        val params = mSearchEditText?.layoutParams as LinearLayout.LayoutParams
        params.setMargins(left, 0, 0, 0)
        mSearchEditText?.layoutParams = params

        elevation =
            context.resources.getDimensionPixelSize(R.dimen.search_elevation_focus).toFloat()
        setBackgroundStrokeWidth(context.resources.getDimensionPixelSize(R.dimen.search_stroke_width_focus))

        mViewShadow?.visibility = View.VISIBLE
        mOnFocusChangeListener?.onFocusChange(true)
        setLayoutHeight(context.resources.getDimensionPixelSize(R.dimen.search_layout_height_focus))
        margins = Margins.FOCUS

        mViewDivider?.visibility = View.VISIBLE
        mRecyclerView?.visibility = View.VISIBLE
    }

    override fun removeFocus() {
        mSearchAnimationLayout?.layoutTransition = mTransition
        mMaterialCardView?.layoutTransition = mTransition

        val params = mSearchEditText?.layoutParams as LinearLayout.LayoutParams
        params.setMargins(0, 0, 0, 0)
        mSearchEditText?.layoutParams = params

        elevation = mElevation
        setBackgroundStrokeWidth(mStrokeWidth)
        setBackgroundRadius(mRadius)

        mViewShadow?.visibility = View.GONE
        mOnFocusChangeListener?.onFocusChange(false)
        setLayoutHeight(context.resources.getDimensionPixelSize(R.dimen.search_layout_height))
        margins = Margins.NO_FOCUS

        mRecyclerView?.visibility = View.GONE
        mViewDivider?.visibility = View.GONE

        hideKeyboard()
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> {
        return mBehavior
    }

    fun setBehavior(behavior: CoordinatorLayout.Behavior<*>) {
        mBehavior = behavior
    }

    fun setTransitionDuration(duration: Long) {
        mTransition.setDuration(duration)
    }

}