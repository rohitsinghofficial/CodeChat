package com.example.codechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //Ref to Firebase
    private FirebaseAuth myAuth;


    //UI Refs
    private EditText myEmail;
    private EditText myPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Grab Data
        myEmail = (EditText) findViewById(R.id.login_email);
        myPassword =(EditText) findViewById(R.id.login_password);




        //Get firebase instance
        myAuth = FirebaseAuth.getInstance();
    }

    //Sign In button was tapped
    public void signInUser(View v){
        loginUserWithFirebase();
    }



//Login User with firebase
    private  void  loginUserWithFirebase(){

        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();

        //ToDo: implement a check like in Register Activity
        if (email.equals("") || password.equals("")){
            //ToDo:  give user a message to fill fields

            return;
        }

        Toast.makeText(this, "Logging in ....", Toast.LENGTH_SHORT).show();
        myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("FINDCODEL", "User creation was:" +task.isSuccessful());


                if (!task.isSuccessful()){
                    showErrorbox("Tere was a problem in Logging in");
                    Log.i("FINDCODE", "Mesaage:" + task.getException());

                }else{
                    Intent intent =new Intent(MainActivity.this, MainChatActivity.class);
                    finish();
                    startActivity(intent);
                }



            }
        });




    }
    //Move user to register activity
    public void  registerNewUser (View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        finish();
        startActivity(intent);
    }


    //Create errorBox for errors
    private void showErrorbox(String message){
        new AlertDialog.Builder(this)
                .setTitle("Heyy")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


}
