package com.example.serj.futbol;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AdaptadorPartido extends CursorAdapter{

    public AdaptadorPartido(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.detalle_lista_partido, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1, tv2, tv3, tv4;
        tv1 = (TextView)view.findViewById(R.id.tvPartido);
        tv2 = (TextView)view.findViewById(R.id.tvPJugID);
        tv3 = (TextView)view.findViewById(R.id.tvPVal);
        tv4 = (TextView)view.findViewById(R.id.tvPCont);
        Partido p = GestorPartido.getRow(cursor);
        //Jugador j = GestorJugador.getRow(cursor); mostrar nombre en lugar de id??????????
        tv1.setText("Partido " + p.getId());
        tv2.setText(String.valueOf(p.getIdJugador()));
        tv3.setText(String.valueOf(p.getValoracion()));
        tv4.setText(p.getContrincante());
    }
}
