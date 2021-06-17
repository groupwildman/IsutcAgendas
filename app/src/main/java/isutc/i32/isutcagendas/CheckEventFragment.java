package isutc.i32.isutcagendas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import isutc.i32.isutcagendas.modelo.Evento;

public class CheckEventFragment extends AppCompatActivity {

    private BottomNavigationView navigationBar;
    private TextInputLayout txtEventoTipo, txtEventoDataInicio, txtEventoDataTermino;
    private Evento evento;

    public CheckEventFragment() {
        evento = (Evento) getIntent().getSerializableExtra("evento");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadComponents();
        fillExtra();
        setListeners();
    }
    public void loadComponents(){
        navigationBar = findViewById(R.id.evento_navigation);
        txtEventoTipo = findViewById(R.id.evento_tipo);
        txtEventoDataInicio = findViewById(R.id.evento_data_inicio);
        txtEventoDataTermino = findViewById(R.id.evento_data_termino);
    }
    public void fillExtra(){

        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sdf_input));
        txtEventoTipo.getEditText().setText(evento.getTipo());
        txtEventoDataInicio.getEditText().setText(sdf.format(evento.getDataInicio()));
        txtEventoDataTermino.getEditText().setText(sdf.format(evento.getDataTermino()));
    }
    public void setListeners(){
        txtEventoDataInicio.setOnClickListener(null);
        txtEventoDataTermino.setOnClickListener(null    );
        navigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.check_evento_voltar){
                    try {
                        if(hasDataSetChanged()){
                            isutc.i32.isutcagendas.controlo.Evento cntrlEvento = new isutc.i32.isutcagendas.controlo.Evento(CheckEventFragment.this);
                            if(cntrlEvento.modificar(evento))
                                Toast.makeText(CheckEventFragment.this, getString(R.string.event_modified), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(CheckEventFragment.this, getString(R.string.event_modified_failed), Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        Toast.makeText(CheckEventFragment.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    public boolean hasDataSetChanged() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sdf_input));
        String new_tipo = String.valueOf(txtEventoTipo.getEditText().getText());
        Date new_data_inicio = sdf.parse(String.valueOf(txtEventoDataInicio.getEditText().getText()));
        Date new_data_termino = sdf.parse(String.valueOf(txtEventoDataTermino.getEditText().getText()));

        if( new_tipo.equals(evento.getTipo()) || new_data_inicio.equals(evento.getDataInicio()) ||
                new_data_termino.equals(evento.getDataTermino()) ){
            evento.setTipo(new_tipo);
            evento.setDataTermino(new_data_termino);
            evento.setDataInicio(new_data_inicio);
            return true;
        }
        return false;
    }
}