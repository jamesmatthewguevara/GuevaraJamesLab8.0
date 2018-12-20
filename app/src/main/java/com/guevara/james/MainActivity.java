package com.guevara.james;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    EditText etFullName, etAge, etGender;
    TextView tvFullName, tvAge, tvGender;

    DatabaseReference root;
    FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase
        db = FirebaseDatabase.getInstance();
        root = db.getReference("Information");

        //Edit Text
        etFullName = findViewById(R.id.etFullname);
        etAge = findViewById(R.id.etAge);
        etGender = findViewById(R.id.etGender);

        //Text View
        tvFullName = findViewById(R.id.tvFullname);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);

    }

    public void SEARCH (View v) {
        String fullname = etFullName.getText().toString();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Information").orderByChild("fullname").equalTo(fullname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot Information : dataSnapshot.getChildren()) {
                        tvFullName.setText(Information.child("fullname").getValue().toString());
                        tvAge.setText(Information.child("age").getValue().toString());
                        tvGender.setText(Information.child("gender").getValue().toString());
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Full name cannot found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void SAVE (View v) {
        String fullname = etFullName.getText().toString().trim();
        int age = Integer.parseInt(etAge.getText().toString().trim());
        String gender = etGender.getText().toString().trim();
        String key = root.push().getKey();
        Information info = new Information(fullname,age,gender);
        root.child(key).setValue(info);
        Toast.makeText(this,"Information added/updated to database",Toast.LENGTH_LONG).show();

        if(fullname.isEmpty()){
            etFullName.setError("Full name required");
            etFullName.requestFocus();
            return;
        }

        if(gender.isEmpty()){
            etGender.setError("Gender required");
            etGender.requestFocus();
            return;
        }
    }

    public void writeUserInfo(){
        String name = etFullName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String gender = etGender.getText().toString().trim();

        if(name.isEmpty()){
            etFullName.setError("Full name required");
            etFullName.requestFocus();
            return;
        }
        if(age.isEmpty()){
            etAge.setError("Age required");
            etAge.requestFocus();
            return;
        }
        if(gender.isEmpty()){
            etGender.setError("Gender required");
            etGender.requestFocus();
            return;
        }


    }

}
