package com.dam.gmdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.dam.gmdb.commons.NodesNames.*;

public class HomeActivity extends AppCompatActivity {

    // var globales
    private Context context;
    private RecyclerView rvFilms;
    private AdapterFilms adapterFilms;
    private FirebaseFirestore db;
    private Toolbar toolbar;

    //methode d'initialisation
    private void init() {

        //context = getApplicationContext();

        //init UI
        rvFilms = findViewById(R.id.rvFilms);

        //reserve taille
        rvFilms.setHasFixedSize(true);

        rvFilms.setLayoutManager(new LinearLayoutManagerWrapper(context,
                LinearLayoutManager.VERTICAL, false));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

    }

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }

    /* La gestion du menu */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // La liaison avec le layout du menu
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Liaison avec le widget de recherche
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilm(newText);
                return false;
            }
        });


        return true;
    }

    private void searchFilm(String s) {
        Query query = db.collection(TABLE_FILM);
        if (!String.valueOf(s).equals("")) {
            query = query
                    .orderBy(KEY_TITRE_MINUSCULE)
                    .startAt(s)
                    .endAt(s + "\uf8ff");
        }
        FirestoreRecyclerOptions<ModelFilms> searchFilm =
                new FirestoreRecyclerOptions.Builder<ModelFilms>()
                        .setQuery(query, ModelFilms.class)
                        .build();

        adapterFilms = new AdapterFilms(searchFilm);
        rvFilms.setAdapter(adapterFilms);
        adapterFilms.startListening();

    }

    /* Recuperer les datas */
    private void getDataFromFirestore() {
        Query query = db.collection(TABLE_FILM).orderBy(KEY_TITRE);

        FirestoreRecyclerOptions<ModelFilms> films =
                new FirestoreRecyclerOptions.Builder<ModelFilms>()
                        .setQuery(query, ModelFilms.class)
                        .build();

        adapterFilms = new AdapterFilms(films);

        rvFilms.setAdapter(adapterFilms);
    }

    /* Verifier si les datas sont presentes dans la db */
    private void addSampleData() {
        SharedPreferences sharedPreferences = getSharedPreferences(R.class.getPackage().getName()
                + ".prefs", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(UPLOAD_PREFS, false)) {
            AddSampleDatasToFirebase.addDatasToFireBase(getApplicationContext());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        addSampleData();
        getDataFromFirestore();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(HomeActivity.this, SignInActivity.class));
        } else {
            adapterFilms.startListening();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterFilms.stopListening();
    }
}