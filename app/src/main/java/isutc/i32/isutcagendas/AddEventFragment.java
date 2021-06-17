package isutc.i32.isutcagendas;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import isutc.i32.isutcagendas.modelo.Evento;

public class AddEventFragment extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private TextInputLayout txtEventoTipo, txtEventoDataInicio, txtEventoDataTermino, txtEventoDescricao;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener dataInicioListener, dataTerminoListener;

    public AddEventFragment() {
        calendar = Calendar.getInstance();
        dataInicioListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInputText(txtEventoDataInicio);
        };
        dataTerminoListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInputText(txtEventoDataTermino);
        };
    }

    private void updateDateInputText(TextInputLayout txtInputLayout){
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sdf_input));
        txtInputLayout.getEditText().setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadComponents();
        setListeners();
    }

    private void loadComponents(){
        navigationView = findViewById(R.id.add_evento_navigation);
        txtEventoTipo = findViewById(R.id.add_event_tipo);
        txtEventoDataInicio = findViewById(R.id.add_event_data_inicio);
        txtEventoDataTermino = findViewById(R.id.add_event_data_termino);
        txtEventoDescricao = findViewById(R.id.add_event_descricao);
    }
    private void setListeners(){
        navigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.add_evento_cancelar:
                    clearFields();
                    finish();
                    break;

                case R.id.add_evento_guardar:
                    Date dtInicio, dtTermino;
                    Evento evento = new Evento();
                    isutc.i32.isutcagendas.controlo.Evento cntrlEvento = new isutc.i32.isutcagendas.controlo.Evento(this);

                    SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sdf_input));

                    if(emptyFields()){
                        Toast.makeText(getApplicationContext(), getString(R.string.preencher_campos), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    try {
                        dtInicio = sdf.parse(txtEventoDataInicio.getEditText().getText().toString());
                        dtTermino = sdf.parse(txtEventoDataTermino.getEditText().getText().toString());
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.inserir_valid_data), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    evento.setTipo(txtEventoTipo.getEditText().getText().toString());
                    evento.setDataInicio(dtInicio);
                    evento.setDataTermino(dtTermino);
                    evento.setDescricao(txtEventoDescricao.getEditText().getText().toString());

                    if(!cntrlEvento.adicionar(evento)){
                        Toast.makeText(getApplicationContext(), getString(R.string.erro_cadastro), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    finish();
                    clearFields();

            }
            return false;
        });
        txtEventoDataInicio.setOnClickListener(v -> new DatePickerDialog(getApplicationContext(), dataInicioListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
        txtEventoDataTermino.setOnClickListener(v -> new DatePickerDialog(getApplicationContext(), dataTerminoListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
    }
    public void clearFields(){
        txtEventoTipo.getEditText().setText("");
        txtEventoDataTermino.getEditText().setText("");
        txtEventoDataInicio.getEditText().setText("");
        txtEventoDescricao.getEditText().setText("");
    }
    public boolean emptyFields(){
        return txtEventoTipo.getEditText().getText().toString().trim().length() == 0 || txtEventoDataTermino.getEditText().getText().toString().trim().length() == 0 ||
        txtEventoDataTermino.getEditText().getText().toString().trim().length() == 0;
    }
}