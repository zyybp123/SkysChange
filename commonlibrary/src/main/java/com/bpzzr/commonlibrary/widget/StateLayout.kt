package com.bpzzr.commonlibrary.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.bpzzr.commonlibrary.R
import com.bpzzr.commonlibrary.util.LogUtil
import java.util.*
import java.util.jar.Attributes
import kotlin.collections.HashMap

/**
 * 状态管理层
 */
class StateLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {
    private val mTag = "StateLayout"
    val viewsMap: MutableMap<State, View> = EnumMap(State::class.java)
    var currentState: State = State.LOADING

    private val defaultViewHolder: DefaultViewHolder

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    var config: Config? = Config(context)

    init {
        defaultViewHolder = DefaultViewHolder(context)
        addView(defaultViewHolder.rootView)
    }

    /**
     * 状态层统一配置
     */
    data class Config(
        val context: Context,
        var emptyStr: String = context.resources.getString(R.string.cb_load_empty),
        var failStr: String = context.resources.getString(R.string.cb_load_fail),
        var loadingStr: String = context.resources.getString(R.string.cb_loading),
        var emptyImage: Int? = null,
        var failImage: Int? = null,
    )


    fun addSuccessView(view: View) {
        val parent = view.parent
        if (parent is ViewGroup) {
            parent.removeAllViews()
        }
        view.visibility = GONE
        viewsMap[State.SUCCESS] = view
        addView(view)
    }

    fun addSuccessView(@LayoutRes viewLayoutId: Int) {
        val view = inflate(context, viewLayoutId, null)
        viewsMap[State.SUCCESS] = view
        addView(view)
    }

    fun getSuccessView(): View? {
        return viewsMap[State.SUCCESS]
    }

    fun showSuccess() {
        val successView = getSuccessView()
        if (successView != null) {
            currentState = State.SUCCESS
            successView.visibility = VISIBLE
            defaultViewHolder.rootView.visibility = GONE
        }
    }

    fun showLoading(
        stateString: String = config!!.loadingStr
    ): DefaultViewHolder {
        currentState = State.LOADING
        getSuccessView()?.visibility = GONE
        defaultViewHolder.rootView.visibility = VISIBLE
        return defaultViewHolder.showLoading(stateString)
    }

    fun showFail(
        @DrawableRes stateImage: Int? = config?.failImage,
        stateString: String = config!!.failStr
    ): DefaultViewHolder {
        currentState = State.FAIL
        getSuccessView()?.visibility = GONE
        defaultViewHolder.rootView.visibility = VISIBLE
        return defaultViewHolder.showFail(stateImage, stateString)
    }

    fun showEmpty(
        @DrawableRes stateImage: Int? = config?.emptyImage,
        stateString: String = config!!.emptyStr
    ): DefaultViewHolder {
        currentState = State.EMPTY
        getSuccessView()?.visibility = GONE
        defaultViewHolder.rootView.visibility = VISIBLE
        return defaultViewHolder.showEmpty(stateImage, stateString)
    }

    enum class State {
        NO_NET, LOADING, EMPTY, SUCCESS, FAIL, CUSTOM, DEFAULT
    }

    class DefaultViewHolder(val context: Context) {
        var rootView: View = View.inflate(context, R.layout.cb_state_layout, null)

        private var ivState: ImageView
        private var tvState: TextView
        private var tvDesc: TextView
        private var pbLoading: ProgressBar

        init {
            rootView.tag = this
            ivState = rootView.findViewById(R.id.cb_iv_state)
            tvState = rootView.findViewById(R.id.cb_tv_state)
            tvDesc = rootView.findViewById(R.id.cb_tv_desc)
            pbLoading = rootView.findViewById(R.id.cb_pb_loading)
        }

        fun showLoading(
            stateString: String = context.resources.getString(R.string.cb_loading)
        ): DefaultViewHolder {
            pbLoading.visibility = VISIBLE
            ivState.visibility = GONE
            tvState.text = stateString
            tvState.visibility = if (TextUtils.isEmpty(stateString)) GONE else VISIBLE
            return this
        }

        fun showFail(
            @DrawableRes stateImage: Int? = null,
            stateString: String = context.resources.getString(R.string.cb_load_fail)
        ): DefaultViewHolder {
            pbLoading.visibility = GONE
            if (stateImage == null) {
                ivState.visibility = GONE
            } else {
                ivState.visibility = VISIBLE
                ivState.setImageResource(stateImage)
            }
            tvState.text = stateString
            tvState.visibility = if (TextUtils.isEmpty(stateString)) GONE else VISIBLE
            return this
        }

        fun showEmpty(
            @DrawableRes stateImage: Int? = null,
            stateString: String = context.resources.getString(R.string.cb_load_empty)
        ): DefaultViewHolder {
            return showFail(stateImage, stateString)
        }

    }
}