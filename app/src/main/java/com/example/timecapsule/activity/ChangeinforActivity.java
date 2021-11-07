package com.example.timecapsule.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timecapsule.R;
import com.example.timecapsule.db.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ChangeinforActivity extends AppCompatActivity {

    private EditText nameT;
    private EditText phoneT;
    private EditText locationT;
    private TextView mailT;
    private EditText desT;
    private TextView usernameT;
    private ImageView avatar;

    private String name = "";
    private String phone = "";
    private String location = "";
    private String des = "";

    private User currentUser;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeinfor);

        currentUser = BmobUser.getCurrentUser(User.class);
        ID = currentUser.getObjectId();

        //Initialize user information
        avatar = (ImageView) findViewById(R.id.icon_image);
        usernameT = (TextView) findViewById(R.id.username);
        nameT = (EditText) findViewById(R.id.new_name);
        phoneT = (EditText) findViewById(R.id.new_phone);
        locationT = (EditText) findViewById(R.id.new_location);
        mailT = (TextView) findViewById(R.id.new_mail);
        desT = (EditText) findViewById(R.id.des);

        usernameT.setText(currentUser.getUsername());
        nameT.setText(currentUser.getName());
        phoneT.setText(currentUser.getPhone());
        locationT.setText(currentUser.getLocation());
        mailT.setText(currentUser.getEmail());
        desT.setText(currentUser.getDescription());

        //Change user information
        Button change = (Button) findViewById(R.id.change_infor);
        change.setOnClickListener(v -> {

            name = nameT.getText().toString();
            phone = phoneT.getText().toString();
            location = locationT.getText().toString();
            des = desT.getText().toString();

            User user = new User();
            user.setName(name);
            user.setPhone(phone);
            user.setDescription(des);
            user.setLocation(location);

            user.update(ID, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        finish();
                    } else {
                        Snackbar.make(change, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });


        });


    }
}