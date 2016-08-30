package com.example.geovany.cimav_peticiones;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DetallePeticion extends AppCompatActivity {
    TextView txtNombreCliente,txtDescripcion, txtSubTotal, txtDescuentoSolicitado, txtTotal,txtCantidadDescontada, txtIva;
    EditText edtxDescuentoAprobado, edtxMotivo;
    double subTotal;
    int descuentoAprobado, descuentoSolicitado, iva, id;
    Intent inRecibir;
    String motivoDescuento;
    CheckBox chbxMotivo;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalle_peticion);


        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Crear los TextView y EditText
        txtNombreCliente = (TextView) findViewById(R.id.txtCliente);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        txtSubTotal = (TextView) findViewById(R.id.txtSubTotal);
        txtDescuentoSolicitado = (TextView) findViewById(R.id.txtDescuentoSolicitado);
        edtxDescuentoAprobado = (EditText) findViewById(R.id.edtxDescuento);
        chbxMotivo = (CheckBox)findViewById(R.id.chbxMotivo);
        txtCantidadDescontada = (TextView)findViewById(R.id.txtCantidadDescontada);
        txtTotal = (TextView)findViewById(R.id.txtTotal) ;
        txtIva = (TextView)findViewById(R.id.txtIva);
        edtxMotivo = (EditText) findViewById(R.id.edtxMotivo);
        chbxMotivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chbxMotivo.isChecked()){
                    edtxMotivo.setVisibility(View.VISIBLE);
                }else{
                    edtxMotivo.setVisibility(View.GONE);
                }
            }
        });


        //recibir el bundle
        inRecibir = getIntent();
        Bundle extras = inRecibir.getExtras();
        id = extras.getInt("id");
        String nombreCliente = extras.getString("nombreCliente");
        String descripcion = extras.getString("descripcion");
        subTotal = extras.getDouble("subTotal");
        descuentoSolicitado = extras.getInt("descuentoSolicitado");
        descuentoAprobado = descuentoSolicitado;
        motivoDescuento = extras.getString("motivoDescuento");


        //mostrar el motivo si es que hay uno
        if (!motivoDescuento.isEmpty()){
            chbxMotivo.setChecked(true);
            edtxMotivo.setText(motivoDescuento);
        }

        //Mostrar los datos por el TextView
        txtNombreCliente.setText(nombreCliente);
        txtDescripcion.setText(descripcion);
        txtSubTotal.setText("$" + subTotal);
        txtDescuentoSolicitado.setText(descuentoSolicitado + "%");
        edtxDescuentoAprobado.setText(descuentoAprobado+"");
        //llenar total, subtotal e iva
        double cantidadDescontada = ((subTotal*descuentoAprobado)/100);
        double iva = (0.16*(subTotal-cantidadDescontada));
        double total = (subTotal-cantidadDescontada)+iva;
        txtCantidadDescontada.setText("- $"+cantidadDescontada);
        //Formato de solo dos decimales
        DecimalFormat df = new DecimalFormat("0.00");
        txtIva.setText("$"+df.format(iva));
        txtTotal.setText("$" + df.format(total));



//listener del editText del descuento
        edtxDescuentoAprobado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
               if (!(edtxDescuentoAprobado.getText().toString()).equals("")) {
                   //decuentoAprobado se toma del editText
                   descuentoAprobado = Integer.parseInt(edtxDescuentoAprobado.getText().toString());
                   if (descuentoAprobado <= 100) {
                       double cantidadDescontada = ((subTotal * descuentoAprobado) / 100);
                       double iva = (0.16*(subTotal-cantidadDescontada));
                       double total = (iva+(subTotal-cantidadDescontada));
                       //Formato de solo dos decimales
                       DecimalFormat df = new DecimalFormat("0.00");
                       txtCantidadDescontada.setText("$" + df.format(cantidadDescontada));
                       txtIva.setText("$"+ df.format(iva));
                       txtTotal.setText("$" + df.format(total));

                       //mostrar alerta si se desea dar un descuento del 100%
                       ImageView imageAlerta = (ImageView) findViewById(R.id.imageAlerta);
                       if ((edtxDescuentoAprobado.getText().toString()).equals("100")) {
                           imageAlerta.setVisibility(View.VISIBLE);
                       } else {
                           imageAlerta.setVisibility(View.INVISIBLE);
                       }

                   } else {
                      Toast toastMenor100 = Toast.makeText(getApplicationContext(), "Inserte un descuento no mayor a 100%", Toast.LENGTH_SHORT);
                       toastMenor100.setGravity(Gravity.CENTER,0,0);
                       toastMenor100.show();
                       edtxDescuentoAprobado.setText("100");
                   }

               }

               }
        });








    }

    public void clickAceptar(View v){

       // Toast.makeText(this,"total: "+(subTotal-((descuentoAprobado*subTotal)/(100))), Toast.LENGTH_SHORT).show();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (chbxMotivo.isChecked()){
                            motivoDescuento = edtxMotivo.getText().toString();
                        }else motivoDescuento = "";

                        final String url = "http://zeus.cimav.edu.mx:3001/vinculacion/aceptar_descuento/"+id;
                        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        Log.d("Response", response);
                                        Toast.makeText(getApplication(),response, Toast.LENGTH_LONG).show();
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        Toast.makeText(getApplication(),error.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("motivo_descuento", motivoDescuento);
                                params.put("descuento_porcentaje", descuentoAprobado+"");

                                return params;
                            }
                        };

                        requestQueue.add(jsonObjReq);
                        finish();
                        Toast.makeText(getApplicationContext(),"Solicitud aceptada", Toast.LENGTH_SHORT).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("¿Desea aceptar un descuento de: "+"<font color='#0000ff'>"+descuentoAprobado+"%"+"</font><br>"+"para: "+"<b>"+txtNombreCliente.getText().toString()+"?</b>")).setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }
    public void clickRechazar(View v){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        if (chbxMotivo.isChecked()){
                            motivoDescuento = edtxMotivo.getText().toString();
                        }else motivoDescuento = "";
                        final String url = "http://zeus.cimav.edu.mx:3001/vinculacion/rechazar_descuento/"+id;


                        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        Log.d("Response", response);
                                        Toast.makeText(getApplication(),response, Toast.LENGTH_LONG).show();
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        Toast.makeText(getApplication(),error.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("motivo_descuento", motivoDescuento);

                                return params;
                            }
                        };

                        requestQueue.add(jsonObjReq);
                        finish();
                        //Toast.makeText(getApplication(),"Solicitud rechazada", Toast.LENGTH_SHORT).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml( "¿Desea rechazar la solicitud para: <br> "   + "<b>"+txtNombreCliente.getText().toString()+"?</b>")).setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();



    }


}
