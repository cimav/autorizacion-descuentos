package com.example.geovany.cimav_peticiones;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by geova on 15/04/2016.
 */
public class JSON {

    public static final String strJson =

            "{\"peticiones\": [" +
            "{\"id\":3553,\"codigo\":\"16/0677-A\",\"cliente\":\"HP\",\"subTotal\":14000,\"descuentoSolicitado\":14}," +
            "{\"id\":3555,\"codigo\":\"16/0679-A\",\"cliente\":\"Toshiba\",\"subTotal\":32000,\"descuentoSolicitado\":8}," +
            "{\"id\":3554,\"codigo\":\"16/0678-C\",\"cliente\":\"Dell\",\"subTotal\":45877,\"descuentoSolicitado\":6}]  }";
    public static JSON miJson= null;
    private RequestQueue requestQueue;

    public JSON(Context context){
        requestQueue = Volley.newRequestQueue(context);

    }
    public static JSON getInstance(Context context){
        if (miJson==null){
            miJson = new JSON(context);
        }
        return miJson;
    }
    public RequestQueue getRequestQueue(){
        return requestQueue;

    }

}

