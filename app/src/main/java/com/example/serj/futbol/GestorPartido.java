package com.example.serj.futbol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GestorPartido {
    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorPartido(Context c) {
        abd = new Ayudante(c);
    }

    public void open() {
        bd = abd.getWritableDatabase();
    }

    public void openRead() {
        bd = abd.getReadableDatabase();
    }

    public void close() {
        abd.close();
    }

    public long insert(Partido p) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaPartido.IDJUGADOR, p.getIdJugador());
        valores.put(Contrato.TablaPartido.VALORACION, p.getValoracion());
        valores.put(Contrato.TablaPartido.CONTRINCANTE, p.getContrincante());
        long id = bd.insert(Contrato.TablaPartido.TABLA, null, valores);
        return id;
    }

    public int delete(Partido p) {
        String condicion = Contrato.TablaPartido._ID + " = ?";
        String[] argumentos = {String.valueOf(p.getId())};
        int cuenta = bd.delete(Contrato.TablaPartido.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(Partido p) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaPartido.IDJUGADOR, p.getIdJugador());
        valores.put(Contrato.TablaPartido.VALORACION, p.getValoracion());
        valores.put(Contrato.TablaPartido.CONTRINCANTE, p.getContrincante());
        String condicion = Contrato.TablaPartido._ID + " = ?";
        String[] argumentos = {String.valueOf(p.getId())};
        int cuenta = bd.update(Contrato.TablaPartido.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List<Partido> select(){
        return select(null, null, null);

    }

    public List<Partido> select(String condicion, String[] parametros, String orden) {
        List<Partido> alp = new ArrayList<Partido>();
        Cursor cursor = bd.query(Contrato.TablaPartido.TABLA, null, condicion, parametros, null, null, orden);
        cursor.moveToFirst();
        Partido p;
        while (!cursor.isAfterLast()) {
            p = getRow(cursor);
            alp.add(p);
            cursor.moveToNext();
        }
        cursor.close();
        return alp;
    }

    public static Partido getRow(Cursor c) {
        Partido p = new Partido();
        p.setId(c.getLong(0));
        p.setIdJugador(c.getLong(1));
        p.setValoracion(c.getFloat(2));
        p.setContrincante(c.getString(3));
        return p;
    }

    public Partido getRow(long id) {
        List<Partido> lp = select(Contrato.TablaPartido._ID + " = ?", new String[]{String.valueOf(id)}, null);
        if (!lp.isEmpty()) {
            return lp.get(0);
        }
        return null;
    }

    public Cursor getCursor(String condicion, String[] parametros, String orden) {
        Cursor cursor = bd.query(Contrato.TablaPartido.TABLA, null, condicion, parametros, null, null, orden);
        return cursor;
    }

    public Cursor getCursor(){
        return getCursor(null, null, null);
    }

    public Cursor rawQuery(String sql){
        Cursor c = bd.rawQuery(sql, null);
        return c;
    }

    public static long getId(Cursor c) {
        return c.getLong(1);
    }
}