package com.vhalency.students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditStudent extends AppCompatActivity {

    TextInputEditText edtName, edtLastName,editTextObservation;
    Button btnAdd,btndelete;
    CircleImageView imageView;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper sqLiteHelper;

    ArrayList<Student> list;
    Integer idStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        init();

        Bundle bundle = getIntent().getExtras(); // O getArguments() si es un Fragment

        if (bundle != null) {
            idStudent = bundle.getInt("id");
            seletBd(idStudent);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        EditStudent.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    sqLiteHelper.updateData(
                            idStudent,
                            edtName.getText().toString().trim(),
                            edtLastName.getText().toString().trim(),
                            imageViewToByte(imageView),
                            editTextObservation.getText().toString().trim()
                    );
                    Toast.makeText(getApplicationContext(), "update successfully!", Toast.LENGTH_SHORT).show();
                   }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });


    }


    private void init(){
        edtName = findViewById(R.id.edtName);
        edtLastName =  findViewById(R.id.edtLastName);
        editTextObservation =  findViewById(R.id.editTextObservation);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btndelete = (Button) findViewById(R.id.btndelete);
        imageView = (CircleImageView) findViewById(R.id.profile_image);
    }

    private void seletBd(Integer idStudent) {

        sqLiteHelper = new SQLiteHelper(this, "StudentDB.sqlite", null, 1);
        list = new ArrayList<>();
        // get all data from sqlite

        Cursor cursor = sqLiteHelper.getData("SELECT * FROM STUDENT WHERE Id = " + idStudent);
        if (cursor != null && cursor.moveToFirst()) {

            try {

                // Obtener los datos del cursor
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("lastname"));
                String observation = cursor.getString(cursor.getColumnIndexOrThrow("observation"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image")); // Obtener la imagen como blob
                // Mostrar los datos en los EditText correspondientes
                edtName.setText(name);
                edtLastName.setText(lastName);
                editTextObservation.setText(observation);

                Glide.with(getApplicationContext())
                        .load(image)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView);

            }catch (IllegalArgumentException e) {
                // Manejar el error si no se encuentran las columnas
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                // Cerrar el cursor
                cursor.close();
            }
        } else {
            // Manejar el caso donde no se encontraron datos
            Toast.makeText(this, "No se encontraron datos para el ID proporcionado", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] imageViewToByte(CircleImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void deleteStudent(){

        AlertDialog.Builder alert = new AlertDialog.Builder(EditStudent.this);
        alert.setTitle("Eliminar");
        alert.setMessage("Desea eliminar el estudiante?");
        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    sqLiteHelper.deleteStudent(idStudent);
                    Toast.makeText(getApplicationContext(), "delete successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditStudent.this, MainActivity.class);
                    startActivity(intent);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }
}

