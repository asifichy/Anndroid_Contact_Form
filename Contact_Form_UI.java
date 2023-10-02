package com.example.contact_form_2019360115;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Contact_Form_UI extends Activity
{
    EditText etName, etEmail, etPhoneHome, etPhoneOffice;
    Button btnSave, btnCancel;
    ImageView imageView;
    private static final int PICK_IMAGE = 10000;
    private final int GALLERY_REQ_CODE = 1000;
    private Uri imageUri;
    String key = "";
    private String base = "";

    private String inputFilePath = "drawable://" + R.drawable.niloy2_jpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form_ui);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneHome = findViewById(R.id.etPhoneHome);
        etPhoneOffice = findViewById(R.id.etPhoneOffice);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);

                /*Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQ_CODE);*/
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
                //System.exit(0);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phoneHome = etPhoneHome.getText().toString().trim();
                String phoneOffice = etPhoneOffice.getText().toString().trim();

                System.out.println(name);

                //String base = encode();
                //String base = convertImageToBase64();

                //String imageUri = "drawable://" + R.drawable.niloy2_jpeg;

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.niloy2_jpeg);
                //Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                byte[] byteArray = byteArrayOutputStream.toByteArray();
                //System.out.println("-----------");
                //String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                //System.out.println(byteArray);
                String imageToString = Base64.encodeToString(byteArray, Base64.DEFAULT);

                try
                {
                    if (isEmailValid(email))
                    {
                        if (isMobileValid(phoneHome))
                        {
                            if (isMobileValid(phoneOffice))
                            {
                                if (key.length() == 0)
                                {
                                    key = name + System.currentTimeMillis();
                                    System.out.println(key);
                                }

                                String value = name + "----" + email + "----" + phoneHome + "----" + phoneOffice + "----" + imageToString;

                                KeyValueDB keyVDB = new KeyValueDB(Contact_Form_UI.this);
                                keyVDB.insertKeyValue(key, value);

                                Toast.makeText(getApplicationContext(), "Data Stored Successfully!", Toast.LENGTH_SHORT).show();

                                //clear input fields and image view
                                etName.setText("");
                                etEmail.setText("");
                                etPhoneHome.setText("");
                                etPhoneOffice.setText("");
                                imageView.setImageResource(R.drawable.contact_img);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Invalid Office Phone!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Invalid Home Phone!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Invalid Email!!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error occurred!", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }

    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null)
        {
            /*Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);*/

            imageUri = data.getData();

            imageView.setImageURI(imageUri);

            /*try
            {
                String image = convertImageToBase64(selectedImageUri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/
        }
    }

    public static boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        if (email == null)
        {
            return  false;
        }
        //return true;

        return pattern.matcher(email).matches();
    }

    public static boolean isMobileValid(String mobile)
    {
        return mobile.matches("^(?:\\+88|88)?(01[3-9]\\d{8})$");
    }

    /*private String encode() throws IOException
    {
        InputStream imageStream = getContentResolver().openInputStream(imageUri);
        //String imageUri = "drawable://" + R.drawable.niloy2_jpeg;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.niloy2_jpeg);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        //byte[] imageContent = imageUri.getBytes(StandardCharsets.UTF_8);

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        System.out.println("-----------");
        //String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        System.out.println(byteArray);
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }*/

}