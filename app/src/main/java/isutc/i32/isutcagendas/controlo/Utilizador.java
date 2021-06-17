package isutc.i32.isutcagendas.controlo;

import java.util.ArrayList;

public class Utilizador {

    private DatabaseManager dbm;

    public boolean cadastrar(isutc.i32.isutcagendas.modelo.Utilizador utilizador){
        return dbm.insertUtilizador(utilizador);
    }
    public boolean autenticar(String utilizador, String senha){
        ArrayList utilizadores = dbm.selectAllUtilizador();
        for(Object aux : utilizadores){
            if(((isutc.i32.isutcagendas.modelo.Utilizador)aux).getUtilizador().equals(utilizador)){
                if(((isutc.i32.isutcagendas.modelo.Utilizador)aux).getSenha().equals(senha)){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
    public void modificar(isutc.i32.isutcagendas.modelo.Utilizador utilizador){
        dbm.updateUtilizador(utilizador);
    }
    public void remover(isutc.i32.isutcagendas.modelo.Utilizador utilizador){
        dbm.deleteUtilizador(utilizador);
    }
}
