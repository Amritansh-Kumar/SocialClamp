package com.example.amritansh.socialclamp.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.activities.HomeActivity;
import com.example.amritansh.socialclamp.models.interfaces.AuthenticationListner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private static AuthenticationListner listner;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private AlertDialog alertDialog;

    @BindView(R.id.reg_user_name)
    EditText userName;
    @BindView(R.id.reg_email)
    EditText email;
    @BindView(R.id.reg_pass)
    EditText pass;

    public RegisterFragment() {
    }

    public static RegisterFragment getInstance(Context context) {
        listner = (AuthenticationListner) context;
        return new RegisterFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.txt_login)
    void setLogin() {
        listner.login();
    }

    @OnClick(R.id.btn_register)
    void register() {

        String emailId = email.getText().toString();
        String password = pass.getText().toString();
        final String name = userName.getText().toString();


        if (!emailId.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(emailId, password)
                 .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {

                             FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                             String uid = mUser.getUid();
                             DatabaseReference myRef = mDatabase.getReference().child("Users")
                                                                .child(uid);
                             HashMap<String, String> userMap = getDefaultUserData(name);

                             myRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     // TODO : add alert dialog to show loading

                                     startActivity(HomeActivity.newInstance(getContext()));

                                     getActivity().finish();
                                 }
                             });
                         } else {
                             Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
        } else {
            Toast.makeText(getContext(), "Fill the fields", Toast.LENGTH_SHORT).show();

        }
    }

    private HashMap<String, String> getDefaultUserData(String username){
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("status", "Hi! I am using SocialClamp");
        userMap.put("image", "default");
        userMap.put("thumb_image", "default");

        return userMap;
    }
}
