package com.dam.gmdb;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdapterFilms extends FirestoreRecyclerAdapter<ModelFilms, AdapterFilms.FilmsViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterFilms(@NonNull FirestoreRecyclerOptions<ModelFilms> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FilmsViewHolder holder, int position, @NonNull ModelFilms model) {

    }

    @NonNull
    @Override
    public FilmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class FilmsViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAffiche;
        private TextView tvTitre, tvSynopsis;

        public FilmsViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAffiche = itemView.findViewById(R.id.ivAffiche);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvSynopsis = itemView.findViewById(R.id.tvSysnopis);

            //Gestion du clic
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && filmClickListener != null) {
                        DocumentSnapshot filmSnapshot = getSnapshots().getSnapshot(position);
                        filmClickListener.onItemClick(filmSnapshot, position);
                    }
                }
            });

        }
    }
    /** Interface pour le clic  **/
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    private OnItemClickListener filmClickListener;

    public void setOnItemCLickListener(OnItemClickListener filmClickListener) {
        this.filmClickListener = filmClickListener;
    }
}
