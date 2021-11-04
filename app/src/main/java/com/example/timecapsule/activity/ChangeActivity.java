package com.example.timecapsule.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timecapsule.R;
import com.example.timecapsule.db.User;
import com.google.android.material.snackbar.Snackbar;

//import org.litepal.LitePal;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

public class ChangeActivity extends AppCompatActivity {

    private Button login;
    private Button change;
    private EditText username;
    private EditText password;
    private EditText new_password;
    private String usernameS;
    private String passwordS;
    private String newPassword;
    private Boolean blank_username;
    private Boolean blank_password;
    private Boolean blank_newpassword;
    private Boolean same_password;
    private Boolean strong_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        login = (Button) findViewById(R.id.login);
        change = (Button) findViewById(R.id.change);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        new_password = findViewById(R.id.newpassword);

        //Make EditText can not enter spaces
        setNoBlank(password);
        setNoBlank(new_password);

        change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                usernameS = username.getText().toString();
                passwordS = password.getText().toString();
                newPassword = new_password.getText().toString();

                //Condition to check
                blank_username = (usernameS.replace(" ", "") == "") || usernameS.isEmpty();
                blank_password = (passwordS.replace(" ", "") == "") || passwordS.isEmpty();
                blank_newpassword = (newPassword.replace(" ", "") == "") || newPassword.isEmpty();
                same_password = newPassword.equals(passwordS);
                strong_password = newPassword.length() > 5;

                //Password cannot be changed under the following conditions
                if (blank_username) {
                    username.setError("Username can't be blank");
                }
                if (blank_password) {
                    password.setError("Password can't be blank");
                }
                if (blank_newpassword) {
                    new_password.setError("New Password can't be blank");
                }
                if (same_password) {
                    new_password.setError("Password can't be same");
                }
                if (!strong_password) {
                    new_password.setError("Password should be longer than 6");
                }

                //Can Change Password
                if (!blank_username && !blank_password && !same_password && strong_password) {
                    BmobQuery<User> bmobQuery = new BmobQuery<User>();
                    bmobQuery.addWhereEqualTo("username", usernameS);
                    bmobQuery.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> users, BmobException e) {
                            if (users.size() == 0) {
                                username.setError("The username does not exits!");
                            }
                            else {
                                User.loginByAccount(usernameS, passwordS, new LogInListener<User>() {
                                    @Override
                                    public void done(User myUser, BmobException e) {
                                        if (e == null){
                                            User user = new User();
                                            user.setPassword(newPassword);
                                            user.update(users.get(0).getObjectId().toString(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        Snackbar.make(change,"The password changes successfully!", Snackbar.LENGTH_LONG).show();
                                                        finish();
                                                    } else {
                                                        Snackbar.make(change, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                    }
                                                }

                                            });

                                        }else{
                                            Snackbar.make(change, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                        }

                                    }
                                });


                                        }
                                    }

                                });

                }

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Make EditText can not enter spaces
    private void setNoBlank(EditText editText) {
        TextWatcher noBlank = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().contains(" ")) {
                    String content = charSequence.toString().replace(" ", "");
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