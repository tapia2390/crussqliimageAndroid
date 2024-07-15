package com.vhalency.students;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String name, String lastName, byte[] image, String observation){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO STUDENT VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, lastName);
        statement.bindBlob(3, image);
        statement.bindString(4, observation);

        statement.executeInsert();
    }


    public void updateData(int studentId, String name, String lastName, byte[] image, String observation) {
        SQLiteDatabase database = getWritableDatabase();

        // Construir la parte de la consulta SQL para actualizar los campos específicos
        String sql = "UPDATE STUDENT SET name = ?, lastName = ?, observation = ?";

        // Verificar si se proporcionó una nueva imagen para actualizarla
        if (image != null) {
            sql += ", image = ?";
        }

        sql += " WHERE Id = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        // Enlazar los valores a los parámetros de la consulta SQL
        int index = 1;
        statement.bindString(index++, name);
        statement.bindString(index++, lastName);
        statement.bindString(index++, observation);

        // Si image no es null, enlazarlo al parámetro correspondiente
        if (image != null) {
            statement.bindBlob(index++, image);
        }

        statement.bindLong(index, studentId); // Enlazar el Id del estudiante

        statement.executeUpdateDelete();
    }

    public void deleteStudent(int studentId) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM STUDENT WHERE Id = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, studentId); // Enlazar el ID del estudiante

        statement.executeUpdateDelete();
    }


    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

