package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<Ingredient> ingredientList;

    // constructor
    public PantryAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This connects the item_pantry.xml layout to this class
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        // This is where the text for each row is set
        Ingredient currentIngredient = ingredientList.get(position);

        holder.tvName.setText(currentIngredient.getName());

        // combining quantity and unit for display (e.g. 5.0 kg)
        String qtyText = currentIngredient.getQuantity() + " " + currentIngredient.getUnit();
        holder.tvQuantity.setText(qtyText);

        // Set expiry date if it exists
        if (currentIngredient.getExpiryDate() != null && !currentIngredient.getExpiryDate().isEmpty()) {
            holder.tvExpiry.setText("Exp: " + currentIngredient.getExpiryDate());
        } else {
            holder.tvExpiry.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    // viewHolder class
    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvExpiry;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            // link java variables to the XML IDs created earlier
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQuantity = itemView.findViewById(R.id.tvIngredientQuantity);
            tvExpiry = itemView.findViewById(R.id.tvIngredientExpiry);
        }
    }
}
