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
            
            ivMovementIcon.setImageResource(movement.iconRes)

            val amountColor = if (movement.isPositive) {
                ContextCompat.getColor(root.context, R.color.income_green)
            } else {
                ContextCompat.getColor(root.context, R.color.expense_red)
            }
            tvMovementAmount.setTextColor(amountColor)
            
            // Set icon tint based on positive/negative or keep as is for specific logos
            val isLogo = movement.iconRes == R.drawable.ic_netflix || 
                         movement.iconRes == R.drawable.ic_spotify
            ivMovementIcon.imageTintList = if (isLogo) null else android.content.res.ColorStateList.valueOf(amountColor)
            
            // Update status badge background tint
            tvMovementStatus.backgroundTintList = android.content.res.ColorStateList.valueOf(
                if (movement.isPositive) ContextCompat.getColor(root.context, R.color.income_green).apply { 0x1A } // 10% opacity
                else ContextCompat.getColor(root.context, R.color.expense_red).apply { 0x1A }
            ).withAlpha(26) // ~10% alpha
            tvMovementStatus.setTextColor(amountColor)
        }
    }

    override fun getItemCount() = movements.size
}