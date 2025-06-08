package com.example.recipes.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipes.R;
import com.example.recipes.model.Recipe;
import com.example.recipes.view.RecipeDetailsActivity;

import java.util.List;

/**
 * Adapter שמציג רשימת מתכונים ברכיב RecyclerView.
 * אחראי על יצירת התצוגות, מילוי הנתונים, וטיפול באירועים של לחיצה על פריט.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    /** רשימת המתכונים להצגה */
    public static List<Recipe> recipeList;

    /** הקשר (context) של האפליקציה, נדרש לצורך יצירת Intents וכו' */
    private Context context;

    /**
     * בונה Adapter חדש לרשימת מתכונים.
     *
     * @param context הקשר של האפליקציה
     * @param recipeList רשימת המתכונים
     */
    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.title.setText(recipe.getTitle());
        holder.time.setText("זמן הכנה: " + recipe.getPreparationTime());
        holder.cost.setText("עלות: " + recipe.getCost());

        if (recipe.getImageBase64() != null && !recipe.getImageBase64().isEmpty()) {
            byte[] decodedBytes = Base64.decode(recipe.getImageBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            holder.imageView.setImageBitmap(bitmap);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    /**
     * מחלקה פנימית שמייצגת תצוגה אחת של מתכון בתוך הרשימה (ViewHolder).
     */
    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        /** רכיבי תצוגה עבור שם, מרכיבים, זמן הכנה, עלות ותמונה */
        TextView title, ingredients, time, cost;
        ImageView imageView;

        /**
         * יוצר ViewHolder חדש ומאתחל את רכיבי התצוגה.
         *
         * @param itemView התצוגה של פריט אחד ברשימה
         */
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            ingredients = itemView.findViewById(R.id.tvIngredients);
            time = itemView.findViewById(R.id.tvTime);
            cost = itemView.findViewById(R.id.tvCost);
            imageView = itemView.findViewById(R.id.ivRecipeImage);
        }
    }
}