package com.example.geovany.cimav_peticiones;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    String nombreCliente, descripcion, codigo, jsonString, motivoDescuento;
    double subTotal;
    int descuentoSolicitado, id, prioridad, iva;
    Intent inDetalle;
    ListView listView;
    protected RequestQueue fRequestQueue;
    private JSON miJson;
    JSONObject jsonRootObject;
    FrameLayout layoutProgressBar;
    TextView txtVacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        layoutProgressBar = (FrameLayout) findViewById(R.id.layoutProgrresBar);
        layoutProgressBar.setVisibility(View.VISIBLE);
        //txtVacio = (TextView)findViewById(R.id.txtVacio);


        inDetalle = new Intent(this, DetallePeticion.class);
        listView = (ListView)findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.txtVacio));
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

    @Override
    protected void onResume() {
        super.onResume();
        makeRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnRefresh){
           //metodo para refresh
            refreshActivity();

        }
        return super.onOptionsItemSelected(item);
    }
    private void refreshActivity(){
        finish();
        startActivity(getIntent());
    }

    private void makeRequest(){



        String url = "http://zeus.cimav.edu.mx:3001/vinculacion/descuento_solicitado";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(  Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public  void onResponse(JSONObject response) {
                       // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            jsonRootObject =  new JSONObject(response.toString());
                            List<Peticion> listaPeticiones = new ArrayList<>();


                            JSONArray jsonArray = jsonRootObject.optJSONArray("cotizaciones");



                            //Extraer informacion del JSON
                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                id = Integer.parseInt(jsonObject.getString("id").toString());
                                codigo  = jsonObject.getString("codigo").toString();
                                nombreCliente = jsonObject.getString("cliente").toString();
                                descripcion = jsonObject.getString("descripcion").toString();
                                subTotal = Double.parseDouble(jsonObject.getString("subtotal").toString());
                                descuentoSolicitado = Integer.parseInt(jsonObject.getString("descuento_porcentaje").toString());
                                prioridad = Integer.parseInt(jsonObject.getString("tiempo_entrega").toString());
                                motivoDescuento = jsonObject.getString("motivo_descuento").toString();


                                Peticion peticion = new Peticion();
                                peticion.setId(id);
                                peticion.setNombreCliente(nombreCliente);
                                peticion.setDescripcion(descripcion);
                                peticion.setCodigo(codigo);
                                peticion.setSubTotal(subTotal);
                                peticion.setDescuentoSolicitado(descuentoSolicitado);
                                peticion.setPrioridad(prioridad);
                                peticion.setMotivoDescuento(motivoDescuento);


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
                                    bDatos.putInt("id",selected.getId());
                                    bDatos.putString("codigo",selected.getCodigo());
                                    bDatos.putString("nombreCliente", selected.getNombreCliente());
                                    bDatos.putString("descripcion", selected.getDescripcion());
                                    bDatos.putDouble("subTotal", selected.getSubTotal());
                                    bDatos.putInt("descuentoSolicitado", selected.getDescuentoSolicitado());
                                    bDatos.putString("motivoDescuento", selected.getMotivoDescuento());

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
        layoutProgressBar.setVisibility(View.GONE);
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
