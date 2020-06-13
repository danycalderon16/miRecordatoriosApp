package mx.tecnm.mirecordatorios.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import mx.tecnm.mirecordatorios.R;
import mx.tecnm.mirecordatorios.models.Recordatorios;

public class MyAdapterReordatorios extends FirestoreRecyclerAdapter<Recordatorios, MyAdapterReordatorios.ViewHolder> {

    private OnItemClickListener listener;
    private Activity activity;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyAdapterReordatorios(@NonNull FirestoreRecyclerOptions<Recordatorios> options, Activity activity,OnItemClickListener listener) {
        super(options);
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i, @NonNull final Recordatorios recordatorios) {
        viewHolder.descripcion.setText(recordatorios.getDescripcion());
        viewHolder.hora.setText(recordatorios.getHora() + " " + recordatorios.getDia());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(recordatorios, i);
            }
        });
        int j =  recordatorios.getColor();

        switch (j) {
            case 1:
                viewHolder.view.setBackgroundResource(R.color.amber_primary);
                break;
            case 2:
                viewHolder.view.setBackgroundResource(R.color.indigo_primary);
                break;
            case 3:
                viewHolder.view.setBackgroundResource(R.color.green_primary);
                break;
            case 4:
                viewHolder.view.setBackgroundResource(R.color.cyan_primary);
                break;
            case 5:
                viewHolder.view.setBackgroundResource(R.color.orange_primary);
                break;
            case 6:
                viewHolder.view.setBackgroundResource(R.color.black_light);
                break;
            case 7:
                viewHolder.view.setBackgroundResource(R.color.red_primary_light);
                break;
        }

        if(recordatorios.isImportante()) {
            viewHolder.view.setBackgroundResource(R.color.red_primary_dark);
            viewHolder.imageView.setImageResource(R.drawable.ic_importante);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView descripcion;
        TextView hora;
        View view;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcion = itemView.findViewById(R.id.descripcion);
            hora = itemView.findViewById(R.id.hora);
            view = itemView.findViewById(R.id.noteColorView);
            imageView = itemView.findViewById(R.id.img_iportant);
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(Recordatorios recordatorios, int position);
    }
}
