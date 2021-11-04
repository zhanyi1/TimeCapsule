package com.example.timecapsule.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.timecapsule.utils.MailSender;
import com.example.timecapsule.R;
import com.example.timecapsule.db.User;
import com.google.android.material.snackbar.Snackbar;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends AppCompatActivity {

    private Button login;
    private Button register;
    private Button sent;
    private EditText username;
    private EditText password;
    private EditText repassword;
    private String newUsername;
    private String newPassword;
    private String rePassword;
    private Boolean blank_username;
    private Boolean blank_password;
    private Boolean blank_repassword;
    private Boolean same_password;
    private Boolean strong_password;
    private Boolean blank_email;
    private Boolean blank_code;
    private Boolean wrong_email;
    private Boolean same_code;
    private Boolean is_sent = false;
    //Regularization formula to judge the mail input format
    public static final String REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    private EditText email;
    private String semial = "";
    private EditText code;
    private String scode = "";
    private String checkCode = "";
    private TextView time;
    private LottieAnimationView animationView;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;
    private LinearLayout time_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        repassword = (EditText)findViewById(R.id.repassword);
        email = (EditText) findViewById(R.id.email);
        code = (EditText) findViewById(R.id.code);
        sent = (Button) findViewById(R.id.sent);
        time = (TextView) findViewById(R.id.time);
        animationView = (LottieAnimationView) findViewById(R.id.animationView);
        animationView.setVisibility(View.GONE);
        layout1 = (LinearLayout) findViewById(R.id.linear1);
        layout2 = (LinearLayout) findViewById(R.id.linear2);
        layout3 = (LinearLayout) findViewById(R.id.linear3);
        layout4 = (LinearLayout) findViewById(R.id.linear4);
        time_record = (LinearLayout) findViewById(R.id.time_record);



        setNoBlank(password);
        setNoBlank(repassword);

        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                newUsername = username.getText().toString();
                newPassword = password.getText().toString();
                rePassword = repassword.getText().toString();
                semial = email.getText().toString();
                scode = code.getText().toString();

                //Conditions=======
                blank_username = (newUsername.replace(" ", "") == "") || newUsername.isEmpty();
                blank_password = (newPassword.replace(" ", "") == "") || newPassword.isEmpty();
                blank_repassword = (rePassword.replace(" ", "") == "") || rePassword.isEmpty();
                same_password = newPassword.equals(rePassword);
                strong_password = newPassword.length() > 5;

                Pattern p = Pattern.compile(REGEX_EMAIL);
                Matcher matched_to = p.matcher(semial);
                blank_email = (semial.replace(" ","") == "") || semial.isEmpty();
                blank_code = (scode.replace(" ","") == "") || scode.isEmpty();
                wrong_email = (!matched_to.matches());
                same_code = scode.equals(checkCode);
                Log.e(">>>",checkCode);
                //=======Conditions

                if(blank_username){
                    username.setError("Username can't be blank");
                }
                if(blank_password){
                    password.setError("Password can't be blank");
                }
                if(blank_repassword){
                    repassword.setError("Re-Enter can't be blank");
                }
                if(!strong_password){
                    password.setError("Password should be longer than 6");
                }

                if(blank_email){
                    email.setError("Email can't be blank");
                }else{
                    if(wrong_email){
                        email.setError("Please input correct email address");
                    }
                }

                if(blank_code){
                    code.setError("Code can't be blank");
                }else{
                    if(!same_code){
                        code.setError("Please input correct email Code");
                    }
                }

                //Register
                if(!blank_username && !blank_password && !blank_repassword && strong_password && !wrong_email && !blank_code && same_code){
                    BmobQuery<User> bmobQuery = new BmobQuery<User>();
                    bmobQuery.addWhereEqualTo("username", newUsername);
                    bmobQuery.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> users, BmobException e) {
                            if(e==null){
                                if (users.size()==0) {
                                    if(same_password){
                                        User newUser = new User();
                                        newUser.setUsername(newUsername);
                                        newUser.setPassword(newPassword);
                                        newUser.setEmail(semial);
                                        newUser.signUp(new SaveListener<User>() {
                                            @Override
                                            public void done(User objectId,BmobException e) {
                                                if(e==null){
                                                    showAnimation();
                                                    finish();
                                                }else{
                                                    Snackbar.make(register, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }else {
                                        repassword.setError("The passwords you enter are not same!");

                                    }

                                } else {
                                    username.setError("The username exits already!");
                                }
                            }else{

                                User newUser = new User();
                                newUser.setUsername(newUsername);
                                newUser.setPassword(newPassword);
                                newUser.setEmail(semial);
                                newUser.signUp(new SaveListener<String>() {
                                    @Override
                                    public void done(String objectId,BmobException e) {
                                        if(e==null){
                                            showAnimation();
                                            finish();
                                        }else{
                                            Snackbar.make(register, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }

                        }
                    });
                }



            }
        });

        //Email verification code
        sent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                checkCode = randomCode();
                semial = email.getText().toString();
                blank_email = (semial.replace(" ","") == "") || semial.isEmpty();
                Pattern p = Pattern.compile(REGEX_EMAIL);
                Matcher matched_to = p.matcher(semial);
                wrong_email = (!matched_to.matches());
                newUsername = username.getText().toString();
                newPassword = password.getText().toString();
                rePassword = repassword.getText().toString();
                scode = code.getText().toString();

                blank_username = (newUsername.replace(" ", "") == "") || newUsername.isEmpty();
                blank_password = (newPassword.replace(" ", "") == "") || newPassword.isEmpty();
                blank_repassword = (rePassword.replace(" ", "") == "") || rePassword.isEmpty();
                same_password = newPassword.equals(rePassword);
                strong_password = newPassword.length() > 5;


                if(blank_username){
                    username.setError("Username can't be blank");
                }
                if(blank_password){
                    password.setError("Password can't be blank");
                }
                if(blank_repassword){
                    repassword.setError("Re-Enter can't be blank");
                }
                if(!strong_password){
                    password.setError("Password should be longer than 6");
                }

                if(blank_email){
                    email.setError("Email can't be blank");
                }else{
                    if(wrong_email){
                        email.setError("Please input correct email address");
                    }
                }


                //Only under this condition can the thread be called to send mail
                if(!blank_username && !blank_password && !blank_repassword && strong_password && !wrong_email && !is_sent ){
                    is_sent = true;

                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {

                                sent.setClickable(false);
                                sent.setBackgroundColor(android.graphics.Color.parseColor("#CCCECE"));
                                MailSender sender = new MailSender("1669454731@qq.com", "phylmblpgqjwbfaj");
                                sender.sendMail("Welcome to Time Capsule","Your Verification Code is "+checkCode,"1669454731@qq.com",semial);

                            } catch (GeneralSecurityException e) {
                                e.printStackTrace();
                            } catch (AddressException e) {
                                e.printStackTrace();
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                }

                //User need to wait another 30s after sending the verification code every time
                if(is_sent){
                    new CountDownTimer(30000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            time_record.setVisibility(View.VISIBLE);
                            time.setText("    seconds remaining: " + millisUntilFinished / 1000);
                        }
                        public void onFinish() {
                            time.setText("");
                            sent.setClickable(true);
                            time_record.setVisibility(View.GONE);
                            sent.setBackground(getResources().getDrawable(R.drawable.button, null));
                        }
                    }.start();
                    is_sent = false;
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

    //Randomly generate verification code
    public static String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    //Set up animation
    private void showAnimation(){
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        layout4.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

}
