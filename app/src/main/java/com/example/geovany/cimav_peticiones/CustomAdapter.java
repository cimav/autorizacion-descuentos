package com.example.geovany.cimav_peticiones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geova on 16/05/2016.
 */

public class CustomAdapter extends ArrayAdapter<Peticion> {

    public CustomAdapter(Context context, List<Peticion> objects) {
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
            //Si no existe, entonces inflarlo con image_list_view.xml
            listItemView = inflater.inflate(
                    R.layout.image_list_item,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView titulo = (TextView)listItemView.findViewById(R.id.text1);
        TextView subtitulo = (TextView)listItemView.findViewById(R.id.text2);
        ImageView categoria = (ImageView)listItemView.findViewById(R.id.prioridadImg);
        TextView descuento = (TextView)listItemView.findViewById(R.id.txtListaDescuento);


        //Obteniendo instancia de la Tarea en la posición actual
        Peticion item = getItem(position);

        //llenando los widgets con los valores de la peticion
        titulo.setText(item.getCodigo());
        subtitulo.setText(item.getNombreCliente());
        descuento.setText(item.getDescuentoSolicitado()+"%");
        //poniendo la imagen dependiendo de la prioridad
        switch (item.getPrioridad()){
            case 1:
                categoria.setImageResource(R.mipmap.prioridad_baja);
                break;
            case 2:
                categoria.setImageResource(R.mipmap.prioridad_media);
                break;
            case 3:
                categoria.setImageResource(R.mipmap.prioridad_alta);
                break;

        }


        //Devolver al ListView la fila creada
        return listItemView;

    }
}