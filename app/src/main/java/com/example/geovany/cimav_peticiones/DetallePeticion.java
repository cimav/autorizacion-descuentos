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

import java.text.DecimalFormat;

public class DetallePeticion extends AppCompatActivity {
    TextView txtNombreCliente,txtDescripcion, txtSubTotal, txtDescuentoSolicitado, txtTotal,txtCantidadDescontada;
    EditText edtxDescuentoAprobado, edtxMotivo;
    double subTotal;
    int descuentoAprobado, descuentoSolicitado;
    Intent inRecibir;
    CheckBox chbxMotivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalle_peticion);




        //Crear los TextView y EditText
        txtNombreCliente = (TextView) findViewById(R.id.txtCliente);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        txtSubTotal = (TextView) findViewById(R.id.txtSubTotal);
        txtDescuentoSolicitado = (TextView) findViewById(R.id.txtDescuentoSolicitado);
        edtxDescuentoAprobado = (EditText) findViewById(R.id.edtxDescuento);
        chbxMotivo = (CheckBox)findViewById(R.id.chbxMotivo);
        txtCantidadDescontada = (TextView)findViewById(R.id.txtCantidadDescontada);
        txtTotal = (TextView)findViewById(R.id.txtTotal) ;
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
        String nombreCliente = extras.getString("nombreCliente");
        String descripcion = extras.getString("descripcion");
        subTotal = extras.getDouble("subTotal");
        descuentoSolicitado = extras.getInt("descuentoSolicitado");
        descuentoAprobado = descuentoSolicitado;

        //Mostrar los datos por el TextView
        txtNombreCliente.setText(nombreCliente);
        txtDescripcion.setText(descripcion);
        txtSubTotal.setText("$" + subTotal);
        txtDescuentoSolicitado.setText(descuentoSolicitado + "%");
        edtxDescuentoAprobado.setText(descuentoAprobado+"");
        //llenar total y subtotal
        double cantidadDescontada = ((subTotal*descuentoAprobado)/100);
        txtCantidadDescontada.setText("- $"+cantidadDescontada);
        double total = (subTotal-((descuentoAprobado*subTotal)/(100)));
        //Formato de solo dos decimales
        DecimalFormat df = new DecimalFormat("0.00");
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
                       txtCantidadDescontada.setText("$" + cantidadDescontada);
                       double total = (subTotal - ((descuentoAprobado * subTotal) / (100)));
                       //Formato de solo dos decimales
                       DecimalFormat df = new DecimalFormat("0.00");
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
                        //Yes button clicked
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
                        finish();
                        Toast.makeText(getApplicationContext(),"Solicitud rechazada", Toast.LENGTH_SHORT).show();
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
