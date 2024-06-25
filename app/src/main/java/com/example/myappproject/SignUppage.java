package com.example.myappproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUppage extends AppCompatActivity {

    EditText name, username, password, email;
    TextView loginlink;
    Button signupbtn;
    RadioGroup userTypeRadioGroup, genderRadioGroup;
    RadioButton radioDoctor, radioPatient, radioMale, radioFemale, radioOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_uppage);

        // Initialize views
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        loginlink = findViewById(R.id.loginlink);
        signupbtn = findViewById(R.id.signup);

        // Initialize RadioGroups and RadioButtons
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        radioDoctor = findViewById(R.id.radio_doctor);
        radioPatient = findViewById(R.id.radio_patient);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        radioOthers = findViewById(R.id.radio_others);

        // Handle login link click
        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUppage.this, Loginpage.class);
                startActivity(intent);
                finish();
            }
        });

        // Handle signup button click
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize Firebase components
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users");

                // Retrieve user input
                String nameuser = name.getText().toString().trim();
                String emailuser = email.getText().toString().trim();
                String userusername = username.getText().toString().trim();
                String passuser = password.getText().toString().trim();

                // Validate input fields
                if (nameuser.isEmpty() || emailuser.isEmpty() || userusername.isEmpty() || passuser.isEmpty()) {
                    Toast.makeText(SignUppage.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Determine selected user type
                String userType = "";
                int selectedUserTypeId = userTypeRadioGroup.getCheckedRadioButtonId();
                if (selectedUserTypeId == R.id.radio_doctor) {
                    userType = "Doctor";
                } else if (selectedUserTypeId == R.id.radio_patient) {
                    userType = "Patient";
                }

                // Determine selected gender
                String gender = "";
                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                if (selectedGenderId == R.id.radio_male) {
                    gender = "Male";
                } else if (selectedGenderId == R.id.radio_female) {
                    gender = "Female";
                } else if (selectedGenderId == R.id.radio_others) {
                    gender = "Others";
                }

                // Create a new user object (Model class)
                Model user = new Model(nameuser, emailuser, userusername, passuser, userType,gender);

                // Store user data in Firebase Database
                reference.child(userusername).setValue(user);

                // Display success message
                Toast.makeText(SignUppage.this, "Signup successful", Toast.LENGTH_SHORT).show();

                // Redirect to MainActivity and pass user data
                Intent intent = new Intent(SignUppage.this, MainActivity.class);
                intent.putExtra("name", nameuser);
                intent.putExtra("email", emailuser);
                intent.putExtra("password", passuser);
                intent.putExtra("username", userusername);
                intent.putExtra("gender", gender);
                intent.putExtra("userType", userType);
                startActivity(intent);
                finish();
            }
        });
    }
}
