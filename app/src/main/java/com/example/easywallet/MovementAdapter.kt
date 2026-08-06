package com.example.easywallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.easywallet.databinding.ItemMovementBinding

class MovementAdapter(private val movements: List<Movement>) :
    RecyclerView.Adapter<MovementAdapter.MovementViewHolder>() {

    class MovementViewHolder(val binding: ItemMovementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {
        val binding = ItemMovementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovementViewHolder, position: Int) {
        val movement = movements[position]
        with(holder.binding) {
            tvMovementType.text = movement.type
            tvMovementDate.text = movement.date
            tvMovementAmount.text = movement.amount
            tvMovementStatus.text = movement.status
            
            // Use incoming arrow for recharges (positive) and transfer icon for expenses
            val iconRes = if (movement.isPositive) R.drawable.ic_arrow_incoming else R.drawable.ic_transfer_modern
            ivMovementIcon.setImageResource(iconRes)

            val amountColor = if (movement.isPositive) {
                ContextCompat.getColor(root.context, R.color.jade_green)
            } else {
                android.graphics.Color.RED
            }
            tvMovementAmount.setTextColor(amountColor)
            
            ivMovementIcon.imageTintList = android.content.res.ColorStateList.valueOf(
                if (movement.isPositive) ContextCompat.getColor(root.context, R.color.jade_green)
                else ContextCompat.getColor(root.context, R.color.petroleum_green)
            )
        }
    }

    override fun getItemCount() = movements.size
}