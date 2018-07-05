package com.teamm.friendstracker.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.db.DbManager;
import com.teamm.friendstracker.model.entity.Coordinats;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText Email;
    private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    DbManager.read();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);

                }

            }
        };

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DbManager.id = user.getUid();
            DbManager.read();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        Email = (EditText) findViewById(R.id.etLogin);
        Password = (EditText) findViewById(R.id.etPasswrd);
        Email.getBackground().setColorFilter(getResources().getColor(R.color.colorBasic), PorterDuff.Mode.SRC_ATOP);
        Password.getBackground().setColorFilter(getResources().getColor(R.color.colorBasic), PorterDuff.Mode.SRC_ATOP);

        Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    if(isRightEmail(Email.getText().toString())){
                        Email.getBackground().setColorFilter(getResources().getColor(R.color.colorRight), PorterDuff.Mode.SRC_ATOP);
                    }
                    else {
                        Email.getBackground().setColorFilter(getResources().getColor(R.color.colorWrong), PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
        });

        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    if(isRightPassword(Password.getText().toString())){
                        Password.getBackground().setColorFilter(getResources().getColor(R.color.colorRight), PorterDuff.Mode.SRC_ATOP);
                        //Password.setBackgroundColor(Color.GREEN);
                    }
                    else{
                        Password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                        //Password.setBackgroundColor(Color.RED);
                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bReg: {
                Intent intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.bSignIn: {
                enter(Email.getText().toString(), Password.getText().toString());
                break;
            }
        }

    }

        private boolean isRightPassword(String pass) {
        boolean result = true;
        if (pass.length() < 6 || pass.length() > 30) {
            Toast.makeText(LoginActivity.this, "Пароль не должен быть меньше 6 символов и не больше 30", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (containsIllegalsPassword(pass)) {
            Toast.makeText(LoginActivity.this, "Недопустимые символы в пароле", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    private boolean isRightEmail(String em) {
        boolean result = true;
        if (em.length() < 6 || em.length() > 255) {
            Toast.makeText(LoginActivity.this, "E-mail не должен быть меньше 6 символов и не больше 255", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (isValidEmail(em) == false) {
            Toast.makeText(LoginActivity.this, "E-mail неверно написан", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (containsIllegalsEmail(em)) {
            Toast.makeText(LoginActivity.this, "Недопустимые символы в E-mail", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean containsIllegalsEmail(String toExamine) {
        Pattern pattern = Pattern.compile("[- !№;%:?*_+#$^&{}\\[\\]]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }

    public boolean containsIllegalsPassword(String toExamine) {
        Pattern pattern = Pattern.compile("[- @!№;%:?*_+#$^&{}\\[\\]]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }

    public void enter(String email , String password) {
        if (isRightPassword(password)) {
            if (isRightEmail(email)) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                            DbManager.read();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(LoginActivity.this, "E-mail или пароль неверный", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
        else
            isRightEmail(email);
    }
}