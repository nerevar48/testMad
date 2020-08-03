package ru.test.livanov.ui

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min


internal enum class ButtonsState {
    GONE,  RIGHT_VISIBLE
}

/**
 * тут лучше использовать что-то готовое, но т.к в задании про это не сказано то реализация такая
 */
class SwipeController : Callback() {

    var showAdd = true
    var buttonWidth = 600
        get() = if (showAdd) 600 else 300

    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    private var leftButtonInstance: RectF? = null
    private var rightButtonInstance: RectF? = null
    private var swipeBack = false
    private var buttonShowedState: ButtonsState = ButtonsState.GONE

    var buttonsActions: SwipeControllerActions? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                var newDx = dX
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) newDx = min(dX, -buttonWidth.toFloat())
                super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive)
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder
    }

    private fun drawButtons(
        c: Canvas,
        viewHolder: RecyclerView.ViewHolder
    ) {
        var buttonWidthWithoutPadding: Float = buttonWidth.toFloat()
        if (showAdd) {
            buttonWidthWithoutPadding /= 2
        }
        buttonWidthWithoutPadding -= 10.toFloat()
        val corners = 0f
        val itemView: View = viewHolder.itemView
        val p = Paint()

        if (showAdd) {
            val leftButton = RectF(
                itemView.right - buttonWidthWithoutPadding*2,
                itemView.top.toFloat(),
                itemView.right.toFloat() - buttonWidthWithoutPadding,
                itemView.bottom.toFloat()
            )
            p.color = parseColor("#FFC107")
            c.drawRoundRect(leftButton, corners, corners, p)
            drawText("to favorite", c, leftButton, p)
            leftButtonInstance = leftButton
        }



        val rightButton = RectF(
            itemView.right - buttonWidthWithoutPadding,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat()
        )
        p.color = parseColor("#F44336")
        c.drawRoundRect(rightButton, corners, corners, p)
        drawText("favorite out", c, rightButton, p)

        rightButtonInstance = rightButton
    }

    private fun drawText(
        text: String,
        c: Canvas,
        button: RectF,
        p: Paint
    ) {
        val textSize = 50f
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize
        val textWidth = p.measureText(text)
        c.drawText(text, button.centerX() - textWidth / 2, button.centerY() + textSize / 2, p)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        recyclerView.setOnTouchListener { view, motionEvent ->
            swipeBack = motionEvent.action == MotionEvent.ACTION_CANCEL || motionEvent.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE

                if (buttonShowedState != ButtonsState.GONE) {
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    setItemsClickable(recyclerView, false)
                }
            }

            false
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                super@SwipeController.onChildDraw(c, recyclerView, viewHolder, 0f, dY, actionState, isCurrentlyActive)
                recyclerView.setOnTouchListener { view, motionEvent ->
                    false
                }
                setItemsClickable(recyclerView, true)
                swipeBack = false

                if (buttonsActions != null) {
                    if (leftButtonInstance != null && leftButtonInstance!!.contains(motionEvent.x, motionEvent.y)) {
                        buttonsActions!!.onLeftClicked(viewHolder.adapterPosition)
                    }
                    if (rightButtonInstance != null && rightButtonInstance!!.contains(motionEvent.x, motionEvent.y)) {
                        buttonsActions!!.onRightClicked(viewHolder.adapterPosition)
                    }
                }

                buttonShowedState = ButtonsState.GONE
                currentItemViewHolder = null
            }
            false
        }
    }

    fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    fun onDraw(c: Canvas) {
        currentItemViewHolder?.let {
            drawButtons(c, it)
        }
    }
}