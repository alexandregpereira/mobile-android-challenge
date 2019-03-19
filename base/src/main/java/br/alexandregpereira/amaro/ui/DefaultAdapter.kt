package br.alexandregpereira.amaro.ui

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView

abstract class DefaultAdapter<T, V : RecyclerView.ViewHolder>(
    context: Context? = null,
    items: MutableList<T> = mutableListOf(),
    var listener: ((T) -> Unit)? = null
) : RecyclerView.Adapter<V>() {

    private var mOnBottomItemListener: (() -> Unit)? = null
    private var mHandler: Handler? = null
    val resources: Resources?
    protected val mItems: MutableList<T> = items
    private var lastPageBottomPosition: Int = 0
    private var pageSize: Int = 0

    open var items: List<T>
        get() = mItems
        set(items) = setItemsInRange(0, mItems.size, items)

    init {
        resources = context?.resources
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun replace(t: T) {
        val i = mItems.indexOf(t)
        if (i >= 0) {
            mItems[i] = t
            notifyItemChanged(i, t)
        }
    }

    fun remove(t: T) {
        val i = mItems.indexOf(t)
        if (i < 0) return
        mItems.removeAt(i)
        notifyItemRemoved(i)
    }

    fun setItem(t: T) {
        val i = mItems.indexOf(t)
        if (i >= 0) {
            mItems[i] = t
            notifyItemChanged(i, t)
        } else {
            this.mItems.add(t)
            notifyItemInserted(mItems.size - 1)
        }
    }

    fun setItemsInRange(startIndex: Int, toIndex: Int, items: List<T>) {
        var index = startIndex
        for (item in items) {
            setItemAtIndex(index++, item)
        }

        val allItems = mItems

        val toIndexReal: Int
        toIndexReal = if (allItems.size - startIndex < toIndex) {
            startIndex + allItems.size - startIndex
        } else
            allItems.size
        if (toIndexReal <= startIndex) return
        val itemsInRange = allItems.subList(startIndex, toIndexReal)
        val itemsToRemove = ArrayList<T>()
        if (items.size < itemsInRange.size) {
            for (item in itemsInRange) {
                val i = items.indexOf(item)
                if (i < 0) {
                    itemsToRemove.add(item)
                }
            }

            for (itemToRemove in itemsToRemove) {
                remove(itemToRemove)
            }
        }
    }

    open fun setItemAtIndex(index: Int, t: T) {
        val i = mItems.indexOf(t)
        if (i >= 0) {
            if (i != index) {
                mItems.removeAt(i)
                notifyItemRemoved(i)
                val indexReal = if (index > mItems.size) mItems.size else index
                mItems.add(indexReal, t)
                notifyItemInserted(indexReal)
                return
            }
            mItems[i] = t
            notifyItemChanged(i, t)
        } else {
            val indexReal = if (index > mItems.size) mItems.size else index
            this.mItems.add(indexReal, t)
            notifyItemInserted(indexReal)
        }
    }

    fun addItem(position: Int, t: T) {
        this.mItems.add(position, t)
        notifyItemInserted(position)
    }

    fun setOnBottomItemListener(pageSize: Int, handler: Handler, onBottomItemListener: () -> Unit) {
        this.pageSize = pageSize
        mHandler = handler
        mOnBottomItemListener = onBottomItemListener
    }

    fun clearLastPageBottomPosition() {
        lastPageBottomPosition = 0
    }

    private fun isPagePositionBottom(position: Int): Boolean {
        if (pageSize == 0) return false
        val currentPosition = position + 1
        return currentPosition % (pageSize - 2) == 0
    }
}