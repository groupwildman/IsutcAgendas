package isutc.i32.isutcagendas.controlo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import isutc.i32.isutcagendas.modelo.Evento;
import isutc.i32.isutcagendas.modelo.Utilizador;

public class DatabaseManager {
    private DatabaseManager.DatabaseHelper databaseHelper;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(AppCompatActivity app){
        this.context = app.getApplicationContext();
    }

    public DatabaseManager open(){
        databaseHelper = this.new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    public boolean insertUtilizador(Utilizador utilizador){
        ContentValues contentValues = new ContentValues();
        contentValues.put("utilizador",utilizador.getUtilizador());
        contentValues.put("email",utilizador.getEmail());
        contentValues.put("senha",utilizador.getSenha());
        contentValues.put("nome",utilizador.getNome());
        contentValues.put("apelido",utilizador.getApelido());
        try {
            sqLiteDatabase.insertOrThrow("utilizador",null,contentValues);
            return true;
        }catch (SQLException e){
            return false;
        }
    }
    public boolean insertEvento(Evento evento){
        ContentValues contentValues = new ContentValues();
        contentValues.put("evento_cod",evento.getCodego());
        contentValues.put("tipo",evento.getTipo());
        contentValues.put("descricao",evento.getDescricao());
        contentValues.put("data_inicio",evento.getDataInicio().getTime());
        contentValues.put("data_termino",evento.getDataTermino().getTime());
        try {
            sqLiteDatabase.insertOrThrow("utilizador",null,contentValues);
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public ArrayList selectAllEventos(Utilizador utilizador){

        String [] columns = {"evento_cod","tipo","descricao","data_inicio", "data_termino"};
        Cursor cursor = sqLiteDatabase.query("evento",columns,null,null,null,null,null);

        ArrayList eventos = new ArrayList();

        if (cursor == null){
            return eventos;
        }
        cursor.moveToFirst();
        while (cursor.moveToNext()) {

            if (cursor.getCount()== 0){
                continue;
            }

            Evento evento = new Evento();
            int codego =  cursor.getInt(cursor.getColumnIndex("evento_cod"));
            String tipo =  cursor.getString(cursor.getColumnIndex("tipo"));
            String descricao =  cursor.getString(cursor.getColumnIndex("descricao"));
            Date data_inicio =  new Date(cursor.getLong(cursor.getColumnIndex("data_inicio")));
            Date data_termino =  new Date(cursor.getLong(cursor.getColumnIndex("data_termino")));

            evento.setCodego(codego);
            evento.setTipo(tipo);
            evento.setDescricao(descricao);
            evento.setDataInicio(data_inicio);
            evento.setDataTermino(data_termino);

            eventos.add(evento);
        }

        return eventos;
    }

    public ArrayList selectAllUtilizador(){

        String [] columns = {"utilizador","nome","apelido", "senha", "email"};
        Cursor cursor = sqLiteDatabase.query("utilizador",columns,null,null,null,null,null);

        ArrayList utilizadores = new ArrayList();

        if (cursor == null){
            return utilizadores;
        }
        cursor.moveToFirst();
        while (cursor.moveToNext()) {

            if (cursor.getCount()== 0){
                continue;
            }

            String _utilizador =  cursor.getString(cursor.getColumnIndex("utilizador"));
            String nome =  cursor.getString(cursor.getColumnIndex("nome"));
            String apelido =  cursor.getString(cursor.getColumnIndex("apelido"));
            String senha =  cursor.getString(cursor.getColumnIndex("senha"));
            String email =  cursor.getString(cursor.getColumnIndex("email"));

            Utilizador utilizador = new Utilizador();
            utilizador.setNome(nome);
            utilizador.setEmail(email);
            utilizador.setSenha(senha);
            utilizador.setUtilizador(_utilizador);
            utilizador.setApelido(apelido);

            utilizadores.add(utilizador);
        }

        return utilizadores;
    }

    public void updateEvento(Evento evento){
        ContentValues contentValues = new ContentValues();
        contentValues.put("tipo",evento.getTipo());
        contentValues.put("descricao",evento.getDescricao());
        contentValues.put("data_inicio",evento.getDataInicio().getTime());
        contentValues.put("data_termino",evento.getDataTermino().getTime());
        sqLiteDatabase.update("evento",contentValues,"evento_cod = "+evento.getCodego(),null);
    }
    public void updateUtilizador(Utilizador utilizador){
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome",utilizador.getNome());
        contentValues.put("apelido",utilizador.getApelido());
        contentValues.put("email",utilizador.getEmail());
        contentValues.put("senha",utilizador.getSenha());
        sqLiteDatabase.update("utilizador",contentValues,"utilizador = "+utilizador.getUtilizador(),null);
    }

    public void deleteEvento(Evento evento){
        sqLiteDatabase.delete("evento", "evento_cod = "+evento.getCodego(), null);
    }
    public void deleteUtilizador(Utilizador utilizador){
        sqLiteDatabase.delete("utilizador", "utilizador = "+utilizador.getUtilizador(), null);
    }
    public class DatabaseHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME="i32";
        public static final int DATABASE_VERSION=1;

        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public String createTableEvento(){
            return "create table evento ( evento_cod int unsigned auto_increment primary key, tipo varchar(50) , descricao varchar(255), data_inicio datetime not null, data_termino datetime not null, utilizador varchar(50) not null, foreign key (utilizador) references utilizador(utilizador) )";
        }
        public String createTableUtilizador(){
            return "create table utilizador ( utilizador varchar(50) primary key, senha varchar(255) not null, email varchar(125) not null, nome varchar(75) not null, apelido varchar(75) not null)";
        }
        public String dropTableEvento(){
            return "drop table if exists evento";
        }
        public String dropTableUtilizador(){
            return "drop table if exists utilizador";
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createTableUtilizador());
            db.execSQL(createTableEvento());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(dropTableEvento());
            db.execSQL(dropTableUtilizador());
        }
    }
}
