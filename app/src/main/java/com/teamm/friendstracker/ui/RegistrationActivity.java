package com.teamm.friendstracker.ui;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamm.friendstracker.R;
import com.teamm.friendstracker.model.db.DbManager;
import com.teamm.friendstracker.model.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser users = mAuth.getInstance().getCurrentUser();

    private EditText Email;
    private EditText Password;
    private EditText Name;
    private EditText Surname;
    private EditText Password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

            }

        };

        Email = (EditText) findViewById(R.id.etMail);
        Password = (EditText) findViewById(R.id.etPasswrd);
        Name = (EditText) findViewById(R.id.etName);
        Surname = (EditText) findViewById(R.id.etSurname);
        Password2 = (EditText) findViewById(R.id.etPasswrd2);

        Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    if(isRightEmail(Email.getText().toString()))
                        Email.setBackgroundColor(Color.GREEN);
                    else
                        Email.setBackgroundColor(Color.RED);
                }
            }
        });

        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    if(isRightPassword(Password.getText().toString()))
                        Password.setBackgroundColor(Color.GREEN);
                    else
                        Password.setBackgroundColor(Color.RED);
                }
            }
        });

        Password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    if(Password2.getText().toString().equals(Password.getText().toString()))
                        Password2.setBackgroundColor(Color.GREEN);
                    else {
                        Toast.makeText(RegistrationActivity.this, "Пароль не совпадает", Toast.LENGTH_SHORT).show();
                        Password2.setBackgroundColor(Color.RED);
                    }
                }
            }
        });

        Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    if(isRightName(Name.getText().toString()))
                        Name.setBackgroundColor(Color.GREEN);
                    else
                        Name.setBackgroundColor(Color.RED);
                }
            }
        });

        Surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus == false) {
                    if(isRightSurname(Surname.getText().toString()))
                        Surname.setBackgroundColor(Color.GREEN);
                    else
                        Surname.setBackgroundColor(Color.RED);
                }
            }
        });
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bBegin: {
                reg(Email.getText().toString(), Password.getText().toString(), Password2.getText().toString(), Name.getText().toString(), Surname.getText().toString());
                break;
            }

            case R.id.bCancel: {
                finish();
                break;
            }
        }
    }

    private boolean isRightPassword(String pass, String pass2) {
        boolean result = true;
        if (pass.length() < 6 || pass.length() > 30) {
            Toast.makeText(RegistrationActivity.this, "Пароль не должен быть меньше 6 символов и не больше 30", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (containsIllegalsString(pass)) {
            Toast.makeText(RegistrationActivity.this, "Недопустимые символы в пароле", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (pass.equals(pass2) == false) {
            Toast.makeText(RegistrationActivity.this, "Пароль не совпадает", Toast.LENGTH_SHORT).show();
            return false;
        }
        return result;
    }

    private boolean isRightPassword(String pass) {
        boolean result = true;
        if (pass.length() < 6 || pass.length() > 30) {
            Toast.makeText(RegistrationActivity.this, "Пароль не должен быть меньше 6 символов и не больше 30", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (containsIllegalsString(pass)) {
            Toast.makeText(RegistrationActivity.this, "Недопустимые символы в пароле", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    private boolean isRightEmail(String em) {
        boolean result = true;
        if (em.length() < 6 || em.length() > 255) {
            Toast.makeText(RegistrationActivity.this, "E-mail не должен быть меньше 6 символов и не больше 255", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (isValidEmail(em) == false) {
            Toast.makeText(RegistrationActivity.this, "E-mail неверно написан", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if (containsIllegalsEmail(em)) {
            Toast.makeText(RegistrationActivity.this, "Недопустимые символы в E-mail", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    private boolean isRightName(String name) {
        boolean result = true;
        if (name.length() < 1 || name.length() > 255) {
            Toast.makeText(RegistrationActivity.this, "Имя не может быть меньше 1 символа или больше 255", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (containsIllegalsString(name)) {
            Toast.makeText(RegistrationActivity.this, "Недопустимые символы в имени", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }

    private boolean isRightSurname(String surname) {
        boolean result = true;
        if (surname.length() < 1 || surname.length() > 255) {
            Toast.makeText(RegistrationActivity.this, "Фамилия не может быть меньше 1 символа или больше 255", Toast.LENGTH_SHORT).show();
            result = false;
        }

        if (containsIllegalsString(surname)) {
            Toast.makeText(RegistrationActivity.this, "Недопустимые символы в фамилии", Toast.LENGTH_SHORT).show();
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

    public boolean containsIllegalsString(String toExamine) {
        Pattern pattern = Pattern.compile("[- @!№;%:?*_+#$^&{}\\[\\]]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }

    public void reg(final String email, final String password, final String password2, String name, String surname) {
        boolean result = true;
        if (isRightPassword(password, password2))
            result = false;

        if (isRightEmail(email))
            result = false;

        if (isRightName(name))
            result = false;

        if (isRightSurname(surname))
            result = false;

        if (result) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        mAuth.signInWithEmailAndPassword(email, password);
                        writeNewUser(Email.getText().toString(), Name.getText().toString(), Surname.getText().toString());
                        Toast.makeText(RegistrationActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else
                        Toast.makeText(RegistrationActivity.this, "Данный E-mail уже зарегистрирован", Toast.LENGTH_SHORT).show();
                }


            });
        }
    }


    private void writeNewUser(String email, String name, String surname) {
        DbManager.user = new User(email, name, surname, false, true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FriendsTracker");
        myRef.child("Users").child(users.getUid()).child("User").setValue(DbManager.user);
    }
}