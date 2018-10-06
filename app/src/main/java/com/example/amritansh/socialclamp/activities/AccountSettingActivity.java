package com.example.amritansh.socialclamp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AccountSettingActivity extends BaseActivity {

    private FirebaseUser currentUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private StorageReference mStorageRef;

    public static final String STATUS = "status";
    public static final int REQUEST_CODE = 1;
//    public static final int IMAGE_PICK = 2;

    @BindView(R.id.user_avtar)
    CircleImageView userAvtar;
    @BindView(R.id.txt_username)
    TextView mUsername;
    @BindView(R.id.txt_status)
    TextView mStatus;

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected String setActionBarTitle() {
        return null;
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_account_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("Users").child(currentUser.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_image");

        updateUserDetails();
    }


    // update user status and profile pic from firebase
    private void updateUserDetails() {

        mReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();

                mUsername.setText(username);
                mStatus.setText(status);

                if (!image.matches("default")){
                    Picasso.get()
                           .load(image)
                           .resize(50, 50)
                           .centerCrop()
                           .placeholder(R.drawable.useravtar)
                           .into(userAvtar);
                }

                // TODO : dismiss progress bar
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // TODO : add error checks
            }
        });
    }

    @OnClick(R.id.update_img_btn)
    public void updateImage() {
//        Intent imageIntent = new Intent();
//        imageIntent.setType("image/*");
//        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//        startActivityForResult(Intent.createChooser(imageIntent, "SELECT IMAGE"), IMAGE_PICK);

        CropImage.activity()
                 .setAspectRatio(16, 9)
                 .setOutputCompressQuality(100)
                 .setGuidelines(CropImageView.Guidelines.ON)
                 .start(this);
    }

    @OnClick(R.id.update_status_btn)
    public void updateStatus() {
        Intent intent = new Intent(this, UpdateStatusActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String status = data.getStringExtra(STATUS);

            mReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Todo : dismiss progress bar and check for errors
                }
            });
        }

//        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK){
//            Uri imageUri = data.getData();
//
//            CropImage.activity(imageUri)
//                     .setAspectRatio(1, 1)
//                     .start(this);
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                File thumb_file =  new File(resultUri.getPath());
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(100)
                            .compressToBitmap
                            (thumb_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                uploadImage(resultUri, thumb_bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage(Uri resultUri, final Bitmap thumb_bitmap) {
        // file name
        final String fileName = currentUser.getUid() + ".jpg";
        // image storage reference
        final StorageReference reference = mStorageRef.child(currentUser.getUid()).child(fileName);
        // uploading file to firebase
        reference.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                // getting image download url
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadUrl = uri.toString();

                        // getting byte from thumb_image bitmap
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        // storage reference for thumb_image
                        final StorageReference thumbReference = mStorageRef.child(currentUser
                                .getUid()).child("thumb_image").child(fileName);
                        // uploading thumb_image
                        UploadTask uploadTask = thumbReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot>
                                                           thumb_task) {
                                // getting thumb_image download url
                                thumbReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String thumbDownloadUrl = uri.toString();

                                        Map updateDetailsHashmap = new HashMap();
                                        updateDetailsHashmap.put("image", downloadUrl);
                                        updateDetailsHashmap.put("thumb_image", thumbDownloadUrl);

                                        mReference.updateChildren(updateDetailsHashmap)
                                                  .addOnSuccessListener(new OnSuccessListener() {
                                                      @Override
                                                      public void onSuccess(Object o) {

                                                      }
                                                  });
                                    }
                                });
                            }
                        });

//                        mReference.child("image").setValue(downloadUrl).addOnCompleteListener
//                                (new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        // TODO : dismiss progress bar
//
//                                    }
//                                });
                    }
                });
            }
        });
    }
}
