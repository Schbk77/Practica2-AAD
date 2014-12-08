package com.example.serj.futbol;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AdaptadorJugador extends CursorAdapter{

    public AdaptadorJugador(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.detalle_lista_jugador, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1, tv2, tv3, tv4;
        tv1 = (TextView)view.findViewById(R.id.tvJugador);
        tv2 = (TextView)view.findViewById(R.id.tvJNombre);
        tv3 = (TextView)view.findViewById(R.id.tvJTlf);
        tv4 = (TextView)view.findViewById(R.id.tvJFnac);
        Jugador j = GestorJugador.getRow(cursor);
        tv1.setText("Jugador " + j.getId());
        tv2.setText(j.getNombre());
        tv3.setText(j.getTelefono());
        tv4.setText(j.getFnac());
    }
}