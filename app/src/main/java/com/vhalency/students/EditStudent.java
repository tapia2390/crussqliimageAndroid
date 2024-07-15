package com.vhalency.students;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditStudent extends AppCompatActivity {

    TextInputEditText edtName, edtLastName,editTextObservation;
    Button btnAdd;
    CircleImageView imageView;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper sqLiteHelper;

    ArrayList<Student> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        init();

        Bundle bundle = getIntent().getExtras(); // O getArguments() si es un Fragment

        if (bundle != null) {
            Integer idStudent = bundle.getInt("id");
            seletBd(idStudent);
        }

    }


    private void init(){
        edtName = findViewById(R.id.edtName);
        edtLastName =  findViewById(R.id.edtLastName);
        editTextObservation =  findViewById(R.id.editTextObservation);
        btnAdd = (Button) findViewById(R.id.btnAdd);
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

}

