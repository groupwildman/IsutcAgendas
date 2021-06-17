  package isutc.i32.isutcagendas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import isutc.i32.isutcagendas.controlo.Evento;

  public class MainActivity extends AppCompatActivity  {

    private FloatingActionButton btnAddDailyEvent;
    private ListView lstVwDaily;
    private CalendarView calendarView;
    private BottomNavigationView navigationView;

    isutc.i32.isutcagendas.controlo.Evento cntrlEvento ;
    private ListviewAdapter lstAdapter;
    private ArrayList lstEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadComponents();
        setAdapter();
        setListeners();
    }
    private void setAdapter(){
        cntrlEvento = new Evento(MainActivity.this);
        try {
            lstEventos = cntrlEvento.selectEventByDate(new Date(calendarView.getDate()));
        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
        lstAdapter = new ListviewAdapter();
        lstVwDaily.setAdapter(lstAdapter);
    }
    private void loadComponents(){
        btnAddDailyEvent = findViewById(R.id.btn_add_daily_event);
        lstVwDaily = findViewById(R.id.daily_event_list);
        calendarView = findViewById(R.id.calendar);
        navigationView = findViewById(R.id.dashboard_navigation);
    }
    private void setListeners(){
        btnAddDailyEvent.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddEventFragment.class);
            startActivity(intent);
        });
        calendarView.setOnDateChangeListener( (view, year, month, dayOfMonth) -> {
            SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sdf_calendar_seletion));
            StringBuilder sb = new StringBuilder();

            sb.append(dayOfMonth+" ");
            sb.append(month+" ");
            sb.append(year+" ");

            try {
                Date date = sdf.parse(sb.toString());

                lstEventos.clear();
                lstEventos.addAll( cntrlEvento.selectEventByDate(date) );
                lstAdapter.notifyDataSetChanged();
            } catch (ParseException e) {
                Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public class ListviewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lstEventos.size();
        }

        @Override
        public Object getItem(int position) {
            return lstEventos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ((isutc.i32.isutcagendas.modelo.Evento) getItem(position) ).getCodego();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Context context = MainActivity.this.getApplicationContext();
            View view = getLayoutInflater().from(context).inflate(R.layout.daily_event_item, parent, false);

            isutc.i32.isutcagendas.modelo.Evento evento = (isutc.i32.isutcagendas.modelo.Evento) getItem(position);
            SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sdf_input));

            TextView evento_tipo = view.findViewById(R.id.item_evento_tipo);
            TextView evento_data_inicio = view.findViewById(R.id.item_evento_data_inicio);
            TextView evento_data_termino = view.findViewById(R.id.item_evento_data_termino);

            evento_tipo.setText(evento.getTipo());
            evento_data_inicio.setText(sdf.format(evento.getDataInicio()));
            evento_data_termino.setText(sdf.format(evento.getDataTermino()));

            view.setOnClickListener(v -> {
               Intent intent = new Intent(getApplicationContext(), CheckEventFragment.class);
               intent.putExtra("evento", evento);
               startActivity(intent);
            });
            return view;
        }
    }

}