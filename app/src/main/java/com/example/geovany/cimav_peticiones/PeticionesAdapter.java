package com.example.geovany.cimav_peticiones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by geova on 03/05/2016.
 */
public class PeticionesAdapter extends ArrayAdapter<Peticion> {

    public PeticionesAdapter(Context context, List<Peticion> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con two_line_list_item.xml
            listItemView = inflater.inflate(
                    R.layout.image_list_item,
                    parent,
                    false);
        }

        //Obteniendo instancias de los text views
        TextView titulo = (TextView)listItemView.findViewById(android.R.id.text1);
        TextView subtitulo = (TextView)listItemView.findViewById(android.R.id.text2);
        ImageView prioridad = (ImageView)listItemView.findViewById(R.id.prioridadImg);

        //Obteniendo instancia de la Tarea en la posición actual
        Peticion item = getItem(position);

        titulo.setText(item.getCodigo());
        subtitulo.setText(item.getNombreCliente());
       // prioridad.setImageResource(R.drawable.prioridad_alta);
       // prioridad.setImageResource(R.mipmap.prioridad_alta);
       /* switch (item.getPrioridad()){
            case 0:
                prioridad.setImageResource(R.mipmap.prioridad_baja);
                break;
            case 1:
                prioridad.setImageResource(R.mipmap.prioridad_media);
                break;
            case 2:
                prioridad.setImageResource(R.mipmap.prioridad_alta);
                break;
        }*/




        //Devolver al ListView la fila creada
        return listItemView;
    }
}