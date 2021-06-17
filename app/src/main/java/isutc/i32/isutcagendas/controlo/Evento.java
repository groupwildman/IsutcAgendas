package isutc.i32.isutcagendas.controlo;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import isutc.i32.isutcagendas.R;
import isutc.i32.isutcagendas.modelo.Utilizador;

public class Evento {

    private Utilizador utilizador;
    private AppCompatActivity app;
    private DatabaseManager dbm ;

    public Evento(AppCompatActivity app){
        this.app = app;
        dbm = new DatabaseManager(app);
    }
    public boolean adicionar(isutc.i32.isutcagendas.modelo.Evento evento){

        if(validarAdicao(evento)){
            dbm.open();
            boolean inserted = dbm.insertEvento(evento);
            dbm.close();
            return inserted;
        }
        return false;
    }
    public boolean validarAdicao(isutc.i32.isutcagendas.modelo.Evento evento){
        ArrayList eventos = dbm.selectAllEventos(utilizador);
        for (Object aux : eventos) {
            Date data_inicio = ((isutc.i32.isutcagendas.modelo.Evento)aux).getDataInicio();
            Date data_termino = ((isutc.i32.isutcagendas.modelo.Evento)aux).getDataTermino();
            if(
                    (data_inicio.after(evento.getDataInicio()) && data_inicio.before(evento.getDataTermino())) ||
                            (data_termino.after(evento.getDataInicio()) && data_termino.before(evento.getDataTermino()))
            ){
                return false;
            }
        }
        return true;
    }
    public ArrayList selectEventByDate(Date date) throws ParseException {

        ArrayList _aux = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat(app.getString(R.string.sdf_calendar_seletion));

        for(Object aux: dbm.selectAllEventos(utilizador)){
            Date compare = sdf.parse(sdf.format(((isutc.i32.isutcagendas.modelo.Evento) aux).getDataInicio()));
            if(date.equals(compare))
                _aux.add(aux);
        }
        return _aux;
    }
    public boolean modificar(isutc.i32.isutcagendas.modelo.Evento evento){
        if(validarAdicao(evento)) {
            dbm.open();
            dbm.updateEvento(evento);
            dbm.close();
            return true;
        }
        return false;
    }
    public void remover(isutc.i32.isutcagendas.modelo.Evento evento){
        dbm.open();
        dbm.deleteEvento(evento);
        dbm.close();
    }
}
