package com.josefdev.savegooglesheets.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.josefdev.savegooglesheets.R;
import com.josefdev.savegooglesheets.adapters.PeopleAdapter;
import com.josefdev.savegooglesheets.models.IGoogleSheets;
import com.josefdev.savegooglesheets.models.Event;
import com.josefdev.savegooglesheets.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultScreen extends AppCompatActivity {
    IGoogleSheets iGoogleSheets;
    private List<Event> eventList;
    private RecyclerView recyclerPeople;
    ProgressDialog progressDialog;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_screen);

        recyclerPeople = findViewById(R.id.recycler_people);

        fab = findViewById(R.id.fab_register);

        eventList = new ArrayList<>();

        iGoogleSheets = Common.iGSGetMethodClient(Common.BASE_URL);
        loadDataFromGoogleSheets();
    }

    private void loadDataFromGoogleSheets() {
        String pathUrl;
        progressDialog = ProgressDialog.show(ConsultScreen.this,
                "Cargando resultados",
                "Espere por favor",
                true,
                false);

        try {
            pathUrl = "exec?spreadsheetId=1RCLu_yGAZARvhVlgIKfLln3G3hGRhX4VmKJuIml1668" + Common.GOOGLE_SHEET_ID + "&sheet=datos" + Common.SHEET_NAME;
            iGoogleSheets.getPeople(pathUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    try {
                        assert response.body() != null;
                        JSONObject responseObject = new JSONObject(response.body());
                        JSONArray peopleArray = responseObject.getJSONArray("Eventos");

                        for (int i = 0; i < peopleArray.length(); i++) {
                            JSONObject object = peopleArray.getJSONObject(i);
                            String id = object.getString("id");
                            String name = object.getString("nombre");
                            String fechaString = object.getString("fecha");

                            Event event = new Event(id, name, fechaString);
                            eventList.add(event);

                            setPeopleAdapter(eventList);
                            progressDialog.dismiss();
                        }

                        int size = eventList.size();
                        goToRegisterScreen(size);

                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPeopleAdapter(List<Event> eventList) {
        LinearLayoutManager manager = new LinearLayoutManager(ConsultScreen.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        PeopleAdapter peopleAdapter = new PeopleAdapter(ConsultScreen.this, eventList);
        recyclerPeople.setLayoutManager(manager);
        recyclerPeople.setAdapter(peopleAdapter);
    }

    private void goToRegisterScreen(int size) {
        fab.setOnClickListener(v -> startActivity(new Intent(ConsultScreen.this, RegisterScreen.class)
                .putExtra("count", size)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
    }

}