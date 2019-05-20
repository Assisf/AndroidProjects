package com.example.firebaseteste;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    EditText textUser, textPass;
    Button bt;
    ListView list_dados;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private List<Usuario> users = new ArrayList<Usuario>();
    private ArrayAdapter<Usuario> arrayAdapteruser;

    Usuario userSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textUser = (EditText) findViewById(R.id.editText);
        textPass = (EditText) findViewById(R.id.editText2);
        list_dados = (ListView) findViewById(R.id.list_dados);



        iniciarFirebase();
        eventoDatabase();
        list_dados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSelect = (Usuario)parent.getItemAtPosition(position);
                textUser.setText(userSelect.getUser());
                textPass.setText(userSelect.getPasswd());
            }
        });

    }

    private void eventoDatabase(){
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            //percorrer os usuarios do firebase
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot objSnap:dataSnapshot.getChildren()){
                    Usuario u = objSnap.getValue(Usuario.class);
                    users.add(u);
                }
                arrayAdapteruser = new ArrayAdapter<Usuario>(MainActivity.this,
                        android.R.layout.simple_list_item_1, users);
                list_dados.setAdapter(arrayAdapteruser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void iniciarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_novo){
            Usuario user = new Usuario();
            user.setId(UUID.randomUUID().toString());
            user.setUser(textUser.getText().toString());
            user.setPasswd(textPass.getText().toString());
            databaseReference.child("Usuario").child(user.getId()).setValue(user);
            clearText();
            Toast.makeText(this, "Cadastrado!", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.menu_atualizar){
            Usuario u = new Usuario();
            u.setId(userSelect.getId());
            u.setUser((textUser.getText().toString().trim()));
            u.setPasswd(textPass.getText().toString().trim());
            databaseReference.child("Usuario").child(u.getId()).setValue(u);
            Toast.makeText(this, "Usuario atualizado", Toast.LENGTH_SHORT).show();
            clearText();
        }
        else if(id == R.id.menu_remove){
            Usuario udel = new Usuario();
            udel.setId(userSelect.getId());
            databaseReference.child("Usuario").child(udel.getId()).removeValue();
            clearText();
            Toast.makeText(this, "Usuario Removido", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void clearText(){
        textPass.setText("");
        textUser.setText("");
    }

    //    public boolean Cadastrar(View view){
//
//        u.setUser(textUser.getText().toString());
//        u.setPasswd(textPass.getText().toString());
//        databaseReference.child("Usuario").setValue(u);
//    }
}
