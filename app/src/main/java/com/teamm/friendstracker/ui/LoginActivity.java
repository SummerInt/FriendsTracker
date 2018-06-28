package com.teamm.friendstracker.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.teamm.friendstracker.R;

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
                    Intent intent = new Intent(LoginActivity.this,MapActivity.class);
                    //startActivity(intent);

                }

            }
        };

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            Intent intent = new Intent(LoginActivity.this, MapActivity.class);
            //startActivity(intent);
        }
        Email = (EditText) findViewById(R.id.etLogin);
        Password = (EditText) findViewById(R.id.etPasswrd);
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

            case R.id.bForgetPasswrd:{
                break;
            }
        }

    }

    private boolean isRightPassword(String pass) {
        if (pass.length() < 6 || pass.length() > 30) {
            Toast.makeText(LoginActivity.this, "Пароль не должен быть меньше 6 символов и не больше 30", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (containsIllegalsPassword(pass)) {
                Toast.makeText(LoginActivity.this, "Недопустимые символы в пароле", Toast.LENGTH_SHORT).show();
                return false;
            } else return true;
        }
    }

    private boolean isRightEmail(String em) {
        if (em.length() < 6 || em.length() > 255) {
            Toast.makeText(LoginActivity.this, "E-mail не должен быть меньше 6 символов и не больше 255", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            if(isValidEmail(em)!=false)
                if(containsIllegalsEmail(em)){
                    Toast.makeText(LoginActivity.this, "Недопустимые символы в E-mail", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else return true;
            else{
                Toast.makeText(LoginActivity.this, "E-mail неверно написан", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean containsIllegalsEmail(String toExamine) {
        Pattern pattern = Pattern.compile("[-!№;%:?*_+#$^&{}\\[\\]]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }

    public boolean containsIllegalsPassword(String toExamine) {
        Pattern pattern = Pattern.compile("[-@!№;%:?*_+#$^&{}\\[\\]]");
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
                            Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(LoginActivity.this, "E-mail или пароль неверный", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }
}
