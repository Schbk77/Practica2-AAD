package com.example.serj.futbol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Principal extends Activity {

    private GestorJugador gj;
    private GestorPartido gp;
    private AdaptadorJugador adj;
    private AdaptadorPartido adp;
    private EditText etNombre, etTelefono, etFecha, etJugador, etValoracion, etContrincante;
    private ListView lvJugador, lvPartido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        etNombre = (EditText)findViewById(R.id.etNombre);
        etTelefono = (EditText)findViewById(R.id.etTelefono);
        etFecha = (EditText)findViewById(R.id.etFecha);
        etJugador = (EditText)findViewById(R.id.etJugador);
        etValoracion = (EditText)findViewById(R.id.etValoracion);
        etContrincante = (EditText)findViewById(R.id.etContrincante);
        lvJugador = (ListView)findViewById(R.id.lvJugador);
        lvPartido = (ListView)findViewById(R.id.lvPartido);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gj = new GestorJugador(this);
        gp = new GestorPartido(this);
        gj.open();
        gp.open();
        Cursor cj = gj.getCursor();
        Cursor cp = gp.getCursor();
        adj = new AdaptadorJugador(this, cj);
        adp = new AdaptadorPartido(this, cp);
        lvJugador.setAdapter(adj);
        lvPartido.setAdapter(adp);
        lvJugador.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mediaJugador(position);
            }
        });
        registerForContextMenu(lvJugador);
        registerForContextMenu(lvPartido);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gj.close();
        gp.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == lvJugador){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_opciones_jugador, menu);
        } else if(v == lvPartido) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_opciones_partido, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int posicion = info.position;
        if (id == R.id.opBorrarJugador) {
            return borrarJugador(posicion);
        } else if (id == R.id.opBorrarPartido){
            return borrarPartido(posicion);
        }
        return super.onContextItemSelected(item);
    }

    public void altaJugador (View v) {
        // Recoger datos campos de texto
        String nombre, telefono, fnac;
        nombre = etNombre.getText().toString();
        telefono = etTelefono.getText().toString();
        fnac = etFecha.getText().toString();
        // Comprobar jugador es UNIQUE: SELECT *, COUNT (*) FROM jugador WHERE nombre='nombre'
        Cursor c = gj.rawQuery(getString(R.string.query_jugador_unique) + nombre + "'");
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        if(count < 1) {
            // Insertar jugador
            Jugador j = new Jugador(nombre, telefono, fnac);
            long id = gj.insert(j);
            adj.getCursor().close();
            adj.changeCursor(gj.getCursor());
            // Reinicializar campos de texto
            etNombre.setText("");
            etTelefono.setText("");
            etFecha.setText("");
            adj.notifyDataSetChanged();
            Toast.makeText(this, getString(R.string.tostada_insertado), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.tostada_yainsertado), Toast.LENGTH_SHORT).show();
        }
    }

    public void nuevoPartido (View v) {
        // Recoger datos campos de texto
        Float valoracion;
        String jugador, contrincante;
        jugador = etJugador.getText().toString();
        valoracion = Float.valueOf(etValoracion.getText().toString());
        contrincante = etContrincante.getText().toString();
        // Ver si hay jugadores insertados
        Cursor c = gj.rawQuery(getString(R.string.query_jugador_insertado));
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        // Comprobar contrincante es UNIQUE: SELECT *, COUNT (*) FROM partido WHERE contrincante ='contrincante'
        c = gp.rawQuery(getString(R.string.query_contrincante_unique)+contrincante+"'");
        c.moveToFirst();
        int contrincantes = c.getInt(0);
        c.close();
        if (count > 0 ){
            if(contrincantes < 1){
                // Insertar partido
                // SELECT _ID FROM JUGADOR WHERE NOMBRE = jugador
                long idjugador = gj.select(jugador);
                if(idjugador != -1) {
                    Partido p = new Partido(idjugador, valoracion, contrincante);
                    long id = gp.insert(p);
                    adp.getCursor().close();
                    adp.changeCursor(gp.getCursor());
                    Toast.makeText(this, getString(R.string.tostada_insertado2), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.tostada_noexiste), Toast.LENGTH_SHORT).show();
                }
                // Reinicializar campos de texto
                etJugador.setText("");
                etValoracion.setText("");
                etContrincante.setText("");
                adp.notifyDataSetChanged();
            } else {
                Toast.makeText(this, getString(R.string.tostada_insertado3), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.tostada_nojugadores), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean borrarJugador(final int pos){
        //Método que crea un AlertDialog que nos permite borrar un Jugador
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.dialog_message_jugador);
        alert.setTitle(R.string.dialog_title_jugador);
        alert.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Cursor c = (Cursor)lvJugador.getItemAtPosition(pos);
                Jugador j = GestorJugador.getRow(c);
                gj.delete(j);
                adj.changeCursor(gj.getCursor());
                long idJugador = j.getId();
                String[] argumentos = {String.valueOf(idJugador)};
                // BORRAR PARTIDOS CON IDJUGADOR
                List<Partido> listaPartidos = gp.select(Contrato.TablaPartido.IDJUGADOR + " = ?", argumentos, null);
                for (int i=0; i<listaPartidos.size(); i++){
                    gp.delete(listaPartidos.get(i));
                }
                adp.changeCursor(gp.getCursor());
            }
        });
        alert.setNegativeButton(R.string.no,null);
        AlertDialog dialog = alert.create();
        dialog.show();
        return true;
    }

    public boolean borrarPartido(final int pos){
        //Método que crea un AlertDialog que nos permite borrar un Jugador
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.dialog_message_partido);
        alert.setTitle(R.string.dialog_title_partido);
        alert.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Cursor c = (Cursor)lvPartido.getItemAtPosition(pos);
                Partido p = GestorPartido.getRow(c);
                gp.delete(p);
                adp.changeCursor(gp.getCursor());
            }
        });
        alert.setNegativeButton(R.string.no,null);
        AlertDialog dialog = alert.create();
        dialog.show();
        return true;
    }

    public void mediaJugador(final int pos) {
        // Conseguir ID del jugador
        Cursor c = (Cursor)lvJugador.getItemAtPosition(pos);
        Jugador j = GestorJugador.getRow(c);
        long idJugador = j.getId();
        String[] argumentos = {String.valueOf(idJugador)};
        // SELECT * FROM PARTIDO WHERE IDJUGADOR = id
        List<Partido> listaPartidos = gp.select(Contrato.TablaPartido.IDJUGADOR + " = ?", argumentos, null);
        // Sumar valoraciones de los partidos de ese jugador
        float sumaValoracion = 0;
        for (int i=0; i<listaPartidos.size(); i++){
            sumaValoracion = sumaValoracion + listaPartidos.get(i).getValoracion();
        }
        // Obtener nº de partidos de ese jugador
        int cuenta = listaPartidos.size();
        float media = (float) 0;
        if(cuenta > 0) {
            media = sumaValoracion/cuenta;
            Toast.makeText(this, getString(R.string.tostada_valoracion) + String.valueOf(media), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.tostada_nopartidos), Toast.LENGTH_SHORT).show();
        }
    }
}