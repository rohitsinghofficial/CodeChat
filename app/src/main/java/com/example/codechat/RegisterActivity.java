package com.example.codechat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity<addOnCompleteListener, password, email> extends AppCompatActivity {

    public static final String CHAT_PREF ="ChatPref";
    public static final String DISPLAY_NAME ="UserName";

    //Ref To fields
    private AutoCompleteTextView myUsernameViews;
    private EditText myEmail;
    private EditText myPassword;
    private EditText myPasswordConfirm;

    //firebase Reference
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // get values on Create

        myEmail = (EditText) findViewById(R.id.register_email);
        myUsernameViews =(AutoCompleteTextView) findViewById(R.id.register_username);
        myPassword = (EditText) findViewById(R.id.register_password);
        myPasswordConfirm = (EditText) findViewById(R.id.register_confirm_password);


        //Get A hold Firebase instance
        myAuth = FirebaseAuth.getInstance();

    }

    //Methods called by tapping


    public  void signUp (View view){
        registerUser();
    }


    //Actual registration happens here
    private void registerUser(){

        myEmail.setError(null);
        myPassword.setError(null);


        //grab values
        String email= myEmail.getText().toString();
        String password = myPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;


        //password validation
        if (!TextUtils.isEmpty(password)&& !checkPassword(password)){
            myPassword.setError(getString(R.string.invalid_password));
            focusView= myPassword;
            cancel = true;

        }
        // email validation
        if (TextUtils.isEmpty(email) && !checkEmail(email)){
            myEmail.setError(getString(R.string.invalid_email));
            focusView = myEmail;
            cancel = true;

        }
        if (cancel){
            focusView.requestFocus();
        }else{
            createUser();
        }

    }

    //Vallidation for email
    private boolean checkEmail(String email){
        return email.contains("@");
    }
    //Validation for password
    private boolean checkPassword(String password){
        String confPassword = myPasswordConfirm.getText().toString();
        return confPassword.equals(password)&& password.length()> 4;
    }


    //SignUp a User atfirebase
    private void createUser(){
        //Grab Values
     String email = myEmail.getText().toString();
     String password = myPassword.getText().toString();


        //Call Method from firebase



        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Delete before production
                Log.i("FINDCODE", "User creation was:" +task.isSuccessful());

                if(!task.isSuccessful()){
                    showErrorbox("Oops Registration filed");
                }else{
                    savedUserName();
                // Move user to login screen
                    Intent intent = new Intent (RegisterActivity.this, MainActivity.class);
                    finish();;
                    startActivity(intent);
                }
            }
        });

    }






    //Use Shared prefs for Usernames
    private void savedUserName(){
       String userName = myUsernameViews.getText().toString();
        SharedPreferences pref =getSharedPreferences(CHAT_PREF,0);
        pref.edit().putString(DISPLAY_NAME,userName).apply();
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




