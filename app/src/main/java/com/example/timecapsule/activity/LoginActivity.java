package com.example.timecapsule.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.airbnb.lottie.LottieAnimationView;
import com.example.timecapsule.R;
import com.example.timecapsule.fragment.SearchFragment;
import com.example.timecapsule.db.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.http.I;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button login;
    private Button register;
    private Button change;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private String username;
    private String password;

    private Boolean blank_username;
    private Boolean blank_password;

    private LottieAnimationView animationView;
    private Intent intent = new Intent();

    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "199274fa0ffcc54f2b4f36eb028b0f68");


        //Get current account information
        currentUser = User.getCurrentUser(User.class);

        //If users are already logged in, go directly to the main page
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass = (CheckBox) findViewById(R.id.remember_content);
        usernameEdit = (EditText) findViewById(R.id.username);
        passwordEdit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        animationView = (LottieAnimationView) findViewById(R.id.animationView);
        change = (Button) findViewById(R.id.change);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass = (CheckBox) findViewById(R.id.remember_content);
        animationView.setVisibility(View.GONE);
        setNoBlank(passwordEdit);

        // Function of remembering password
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String username = pref.getString("username","");
            String password = pref.getString("password","");
            usernameEdit.setText(username);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }


        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        change.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChangeActivity.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                username = usernameEdit.getText().toString();
                password = passwordEdit.getText().toString();
                blank_username = (username.replace(" ", "") == "") || username.isEmpty();
                blank_password = (password.replace(" ", "") == "") || password.isEmpty();

                if(blank_username){
                    usernameEdit.setError("Username can't be blank");
                }
                if(blank_password){
                    passwordEdit.setError("Password can't be blank");
                }

                if(!blank_username && !blank_password ){
                    BmobQuery<User> bmobQuery = new BmobQuery<User>();
                    bmobQuery.addWhereEqualTo("username", username);
                    bmobQuery.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> users, BmobException e) {

                            if(e==null){
                                if (users.size()==0) {
                                    usernameEdit.setError("The username does not exits!");

                                } else {

                                    User.loginByAccount(username, password, new LogInListener<User>() {
                                        @Override
                                        public void done(User myUser, BmobException e) {
                                            if (e == null){
                                                //Realize the function of remembering password
                                                editor = pref.edit();
                                                if (rememberPass.isChecked()) {
                                                    editor.putBoolean("remember_password", true);
                                                    editor.putString("username", username);
                                                    editor.putString("password", password);
                                                } else {
                                                    editor.clear();
                                                }
                                                editor.apply();

                                                //Login animation
                                                login.setVisibility(View.GONE);
                                                animationView.setVisibility(View.VISIBLE);
                                                animationView.playAnimation();
                                                animationView.addAnimatorListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animator) {
                                                        intent.setAction(Intent.ACTION_VIEW);
                                                        intent.setClass(getApplicationContext(), MainActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        intent.putExtra(SearchFragment.ACCOUNT,username);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animator) {
                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animator) {
                                                    }
                                                });

                                            }else{
                                                Snackbar.make(login, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                            }

                                        }
                                    });


                                }

                            }else{
                                usernameEdit.setError("Your network has problems!");
                                Snackbar.make(login, e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }

                        }
                    });
                }


            }
        });






    }

    //Make EditText can not enter spaces
    private void setNoBlank(EditText editText){
        TextWatcher noBlank = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().contains(" ")) {
                    String content = charSequence.toString().replace(" ","");
                    editText.setText(content);
                    editText.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        };
        editText.addTextChangedListener(noBlank);
    }
}