package com.example.geovany.cimav_peticiones;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {
    Button btnAceptar;
    String nombreCliente, descripcion, codigo, jsonString;
    double subTotal;
    int descuentoSolicitado, id, prioridad;
    Intent inDetalle;
    ListView listView;
    protected RequestQueue fRequestQueue;
    private JSON miJson;
    JSONObject jsonRootObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        inDetalle = new Intent(this, DetallePeticion.class);
        listView = (ListView)findViewById(R.id.listView);
        miJson = JSON.getInstance(getApplication().getApplicationContext());
        fRequestQueue = miJson.getRequestQueue();
        //Iniciando la lista donde se almacenaran los objetos

        //verificar que hay conexion a internet
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
           makeRequest();
           // Toast.makeText(this, jsonString, Toast.LENGTH_SHORT).show();

            // Operaciones http



        } else {
            Toast.makeText(this,"No es posible establecer conexion con el servidor",Toast.LENGTH_LONG).show();// Mostrar errores
        }



    }
    private void makeRequest(){
        String url = "http://dominiodeprueba.dx.am/PruebaJson.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(  Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public  void onResponse(JSONObject response) {
                       // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            jsonRootObject =  new JSONObject(response.toString());
                            List<Peticion> listaPeticiones = new ArrayList<>();


                            JSONArray jsonArray = jsonRootObject.optJSONArray("peticiones");



                            //Extraer informacion del JSON
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                id = Integer.parseInt(jsonObject.getString("id").toString());
                                codigo  = jsonObject.getString("codigo").toString();
                                nombreCliente = jsonObject.getString("cliente").toString();
                                descripcion = jsonObject.getString("descripcion").toString();
                                subTotal = Double.parseDouble(jsonObject.getString("subTotal").toString());
                                descuentoSolicitado = Integer.parseInt(jsonObject.getString("descuentoSolicitado").toString());
                                prioridad = Integer.parseInt(jsonObject.getString("prioridad").toString());


                                Peticion peticion = new Peticion();
                                peticion.setId(id);
                                peticion.setNombreCliente(nombreCliente);
                                peticion.setDescripcion(descripcion);
                                peticion.setCodigo(codigo);
                                peticion.setSubTotal(subTotal);
                                peticion.setDescuentoSolicitado(descuentoSolicitado);
                                peticion.setPrioridad(prioridad);

                                listaPeticiones.add(peticion);


                            }
                            //declaracion del arrayAdapter
                           // ArrayAdapter<Peticion> adapter = new ArrayAdapter<Peticion>(getBaseContext(),android.R.layout.simple_list_item_1, listaPeticiones);
                           ArrayAdapter<Peticion> adapter = new CustomAdapter(getBaseContext(), listaPeticiones);
                          // ListAdapter adapter = new CustomAdapter(getApplicationContext(), listaPeticiones);
                            listView.setAdapter(adapter);

                            //Listenner
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                    Peticion selected = (Peticion) parent.getItemAtPosition(position);
                                    //Mandar un bundle con los datos
                                    Bundle bDatos = new Bundle();
                                    bDatos.putString("codigo",codigo);
                                    bDatos.putString("nombreCliente", selected.getNombreCliente());
                                    bDatos.putString("descripcion", selected.getDescripcion());
                                    bDatos.putDouble("subTotal", selected.getSubTotal());
                                    bDatos.putInt("descuentoSolicitado", selected.getDescuentoSolicitado());

                                    inDetalle.putExtras(bDatos);

                                    startActivity(inDetalle);




                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // txtJSON.setText(response.toString());

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        //add request to queue
        fRequestQueue.add(jsonObjectRequest);
    }
    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (fRequestQueue == null)
                fRequestQueue = miJson.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            onPreStartConnection();
            fRequestQueue.add(request);
        }
    }

    public void onPreStartConnection() {
        this.setProgressBarIndeterminateVisibility(true);
    }

    public void onConnectionFinished() {
        this.setProgressBarIndeterminateVisibility(false);
    }

    public void onConnectionFailed(String error) {
        this.setProgressBarIndeterminateVisibility(false);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


}
