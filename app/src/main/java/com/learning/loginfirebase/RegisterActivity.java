package com.learning.loginfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNama, etEmailreg, etPasswordreg, etPasswordconf;
    private Button btnRegisterreg, btnLoginreg;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etNama = findViewById(R.id.etNama);
        etEmailreg = findViewById(R.id.etEmailreg);
        etPasswordreg = findViewById(R.id.etpasswordreg);
        etPasswordconf = findViewById(R.id.etPasswordconf);
        btnRegisterreg = findViewById(R.id.btnRegisterreg);
        btnLoginreg = findViewById(R.id.btnLoginreg);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan Tunggu");
        progressDialog.setCancelable(false);

        btnLoginreg.setOnClickListener(v -> {
            finish();
        });
        btnRegisterreg.setOnClickListener(v -> {
            if(etNama.getText().length()>0 && etEmailreg.getText().length()>0 && etPasswordreg.getText().length()>0 && etPasswordconf.getText().length()>0){
                if(etPasswordreg.getText().toString().equals(etPasswordconf.getText().toString())){
                    register(etNama.getText().toString(), etEmailreg.getText().toString(), etPasswordreg.getText().toString());
                }else {
                    Toast.makeText(getApplicationContext(),"Password tidak sama!",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),"Silahkan  isi semua data! ",Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void register (String name, String email, String password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult()!=null){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser!=null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Register Gagal",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private  void reload(){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}