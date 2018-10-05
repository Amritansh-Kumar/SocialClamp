package com.example.amritansh.socialclamp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    @BindView(R.id.login_email)
    EditText email;
    @BindView(R.id.login_pass)
    EditText pass;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;
    private static AuthenticationListner listner;

    public LoginFragment() {
    }

    public static LoginFragment getInstance(Context context){
        listner = (AuthenticationListner) context;
        return new LoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.txt_register)
    void register(){
        listner.register();
    }

    @OnClick(R.id.btn_login)
    void loginUser(){
        String emailId = email.getText().toString();
        String password = pass.getText().toString();

        if (!emailId.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(emailId, password)

                 // TODO : add alert dialog to show loading

                 .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             mUser = mAuth.getCurrentUser();
                             startActivity(HomeActivity.newInstance(getContext()));
                             getActivity().finish();
                         } else {
                             Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });

        }else {
            Toast.makeText(getContext(), "Fill the blank fields", Toast.LENGTH_SHORT).show();

        }
    }

}
