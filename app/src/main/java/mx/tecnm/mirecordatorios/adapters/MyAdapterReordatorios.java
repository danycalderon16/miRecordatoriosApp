package mx.tecnm.mirecordatorios.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import mx.tecnm.mirecordatorios.R;
import mx.tecnm.mirecordatorios.models.Recordatorios;
import static  mx.tecnm.mirecordatorios.utils.Util.*;

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
        });  Log.i("########",getRandomColor()+"");
        int j =  (int) (Math.random()* 7) + 1;

        switch (j) {
            case 1:
                viewHolder.view.setBackgroundResource(R.color.amber_primary);
                break;
            case 2:
                viewHolder.view.setBackgroundResource(R.color.pink_primary);
                break;
            case 3:
                viewHolder.view.setBackgroundResource(R.color.green_primary);
                break;
            case 4:
                viewHolder.view.setBackgroundResource(R.color.red_primary);
                break;
            case 5:
                viewHolder.view.setBackgroundResource(R.color.indigo_primary);
                break;
            case 6:
                viewHolder.view.setBackgroundResource(R.color.orange_primary);
                break;
            case 7:
                viewHolder.view.setBackgroundResource(R.color.blue_primary);
                break;
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcion = itemView.findViewById(R.id.descripcion);
            hora = itemView.findViewById(R.id.hora);
            view = itemView.findViewById(R.id.noteColorView);
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(Recordatorios recordatorios, int position);
    }
}
