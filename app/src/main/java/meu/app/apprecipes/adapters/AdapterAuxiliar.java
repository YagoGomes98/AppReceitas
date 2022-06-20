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
import meu.app.apprecipes.models.Auxiliar;

import meu.app.apprecipes.models.Ingrediente;


public class AdapterAuxiliar extends RecyclerView.Adapter<AdapterAuxiliar.DirectionViewHolder> {

    private List<Auxiliar> auxiliarList;
    private boolean isEditable = true;
    private Context mContext;
    private DirectionListener directionListener;

    public AdapterAuxiliar(Context context, List<Auxiliar> auxiliarList) {
        mContext = context;
        this.auxiliarList = auxiliarList;
    }

    public AdapterAuxiliar(Context context, List<Auxiliar> auxiliarList, boolean isEditable) {
        mContext = context;
        this.auxiliarList = auxiliarList;
        this.isEditable = isEditable;
    }

    @Override
    public int getItemViewType(int position) {
        return isEditable ? 0 : 1;
    }

    @Override
    public DirectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewType == 0 ? R.layout.auxiliar_item_row : R.layout.auxiliar_item_nao_editavel,
                        parent, false);
        return new DirectionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DirectionViewHolder holder, int position) {
        Auxiliar auxiliar = auxiliarList.get(position);
        holder.bind(auxiliar);
    }

    @Override
    public int getItemCount() {
        return auxiliarList.size();
    }

    public class DirectionViewHolder extends RecyclerView.ViewHolder {

        TextView directionText;
        ImageView wasteBin;

        public DirectionViewHolder(View itemView) {
            super(itemView);

            directionText = itemView.findViewById(R.id.directionText);
            if (isEditable) {
                wasteBin = itemView.findViewById(R.id.wasteBin);
                wasteBin.setOnClickListener(v -> {
                    if (directionListener != null)
                        directionListener.onDeleteDirection(getAdapterPosition());
                });
            }
        }

        public void bind(Auxiliar auxiliar) {
            directionText.setText(auxiliar.getBody());
        }
    }

    public void setDirectionListener(DirectionListener directionListener) {
        this.directionListener = directionListener;
    }

    public interface DirectionListener {
        void onDeleteDirection(int position);
    }
}
