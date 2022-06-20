package meu.app.apprecipes.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import meu.app.apprecipes.R;
import meu.app.apprecipes.models.Ingrediente;


public class AdapterIngrediente extends RecyclerView.Adapter<AdapterIngrediente.IngredientViewHolder> {

    private List<Ingrediente> ingredientList;
    private boolean isEditable = true;
    private Context mContext;
    private IngredientListener ingredientListener;

    public AdapterIngrediente(Context context, List<Ingrediente> ingredientList) {
        mContext = context;
        this.ingredientList = ingredientList;
    }

    public AdapterIngrediente(Context context, List<Ingrediente> ingredientList, boolean isEditable) {
        mContext = context;
        this.ingredientList = ingredientList;
        this.isEditable = isEditable;
    }

    @Override
    public int getItemViewType(int position) {
        return isEditable ? 0 : 1;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewType == 0 ? R.layout.ingrediente_item_row : R.layout.ingrediente_item_row_nao_editavel,
                        parent, false);
        return new IngredientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final IngredientViewHolder holder, int position) {
        Ingrediente ingrediente = ingredientList.get(position);
        holder.bind(ingrediente);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientText;
        ImageView wasteBin;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientText = itemView.findViewById(R.id.ingredientText);
            if (isEditable) {
                wasteBin = itemView.findViewById(R.id.wasteBin);
                wasteBin.setOnClickListener(v -> {
                    if (ingredientListener != null)
                        ingredientListener.onDeleteIngredient(getAdapterPosition());
                });
            }
        }

        public void bind(Ingrediente ingredient) {
            ingredientText.setText(ingredient.getName());
        }
    }

    public void setIngredientListener(IngredientListener ingredientListener) {
        this.ingredientListener = ingredientListener;
    }

    public interface IngredientListener {
        void onDeleteIngredient(int position);
    }
}
