package by.godevelopment.thirdtask.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.godevelopment.thirdtask.databinding.ItemContactBinding
import by.godevelopment.thirdtask.domain.models.ContactModel

class ListAdapter(
    @ColorInt
    private val colorMain: Int,
    @ColorInt
    private val colorMark: Int
) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback
            = object : DiffUtil.ItemCallback<ContactModel>() {
        override fun areContentsTheSame(
            oldItem: ContactModel,
            newItem: ContactModel
        ): Boolean {
            return oldItem.taskPhoneNumber == newItem.taskPhoneNumber
        }

        override fun areItemsTheSame(
            oldItem: ContactModel,
            newItem: ContactModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var listItems: List<ContactModel>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = listItems[position]
        holder.binding.apply {
            name.text = item.name
            surname.text = item.surname
            number.text = item.taskPhoneNumber
            email.text = item.email
            if (item.isSelected) {
                root.setBackgroundColor(colorMark)
            } else {
                root.setBackgroundColor(colorMain)
            }
            root.setOnLongClickListener {
                changeSelectedItem(item.id)
                true
            }
        }
    }

    override fun getItemCount(): Int = listItems.size

    private fun changeSelectedItem(key: Int) {
        listItems = listItems.map {
            it.copy(
                isSelected = (it.id == key)
            )
        }
    }
}
