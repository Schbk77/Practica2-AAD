package com.example.serj.futbol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GestorJugador {

    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorJugador(Context c) {
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

    public long insert(Jugador j) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaJugador.NOMBRE, j.getNombre());
        valores.put(Contrato.TablaJugador.TELEFONO, j.getTelefono());
        valores.put(Contrato.TablaJugador.FNAC, j.getFnac());
        long id = bd.insert(Contrato.TablaJugador.TABLA, null, valores);
        return id;
    }

    public int delete(Jugador j) {
        String condicion = Contrato.TablaJugador._ID + " = ?";
        String[] argumentos = {String.valueOf(j.getId())};
        int cuenta = bd.delete(Contrato.TablaJugador.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(Jugador j) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaJugador.NOMBRE, j.getNombre());
        valores.put(Contrato.TablaJugador.TELEFONO, j.getTelefono());
        valores.put(Contrato.TablaJugador.FNAC, j.getFnac());
        String condicion = Contrato.TablaJugador._ID + " = ?";
        String[] argumentos = {String.valueOf(j.getId())};
        int cuenta = bd.update(Contrato.TablaJugador.TABLA, valores, condicion, argumentos);
        return cuenta;
    }

    public List<Jugador> select(){
        return select(null, null, null);

    }

    public List<Jugador> select(String condicion, String[] parametros, String orden) {
        List<Jugador> alj = new ArrayList<Jugador>();
        Cursor cursor = bd.query(Contrato.TablaJugador.TABLA, null, condicion, parametros, null, null, orden);
        cursor.moveToFirst();
        Jugador j;
        while (!cursor.isAfterLast()) {
            j = getRow(cursor);
            alj.add(j);
            cursor.moveToNext();
        }
        cursor.close();
        return alj;
    }

    public long select(String jugador) {
        long id;
        String tabla = Contrato.TablaJugador.TABLA;
        String[] columnas = {Contrato.TablaJugador._ID};
        String condicion = Contrato.TablaJugador.NOMBRE + " = ?";
        String[] argumentos = {jugador};
        Cursor cursor = bd.query(tabla, columnas, condicion, argumentos, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() != 0){
            id = getId(cursor);
            cursor.close();
            return id;
        } else {
            id = -1;
            cursor.close();
            return id;
        }

    }

    public static Jugador getRow(Cursor c) {
        Jugador j = new Jugador();
        j.setId(c.getLong(0));
        j.setNombre(c.getString(1));
        j.setTelefono(c.getString(2));
        j.setFnac(c.getString(3));
        return j;
    }

    public static long getId(Cursor c) {
        return c.getLong(0);
    }

    public Jugador getRow(long id) {
        List<Jugador> lj = select(Contrato.TablaJugador._ID + " = ?", new String[]{String.valueOf(id)}, null);
        if (!lj.isEmpty()) {
            return lj.get(0);
        }
        return null;
    }

    public Cursor getCursor(String condicion, String[] parametros, String orden) {
        Cursor cursor = bd.query(Contrato.TablaJugador.TABLA, null, condicion, parametros, null, null, orden);
        return cursor;
    }

    public Cursor getCursor(){
        return getCursor(null, null, null);
    }

    public Cursor rawQuery(String sql){
        Cursor c = bd.rawQuery(sql, null);
        return c;
    }
}