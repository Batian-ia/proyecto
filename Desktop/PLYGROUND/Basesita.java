package com.example.playground;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class Basesita extends SQLiteOpenHelper {

    private static final String DB_NAME = "usuarios.db";
    private static final int DB_VERSION = 1;

    public static final String TABLA_USUARIOS = "usuarios";
    public static final String COL_ID = "id_usuario";
    public static final String COL_USUARIO = "usuario";
    public static final String COL_PASSWORD = "password";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLA_USUARIOS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USUARIO + " TEXT UNIQUE NOT NULL, " +
                    COL_PASSWORD + " TEXT NOT NULL" +
                    ")";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLA_USUARIOS;


    public Basesita(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // ==================== OPERACIONES CRUD ====================

    public long insertarUsuario(Usuario usuario) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_USUARIO, usuario.getCorreo());
            values.put(COL_PASSWORD, usuario.getPassword());

            return db.insert(TABLA_USUARIOS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    public boolean usuarioExiste(String correo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String[] columnas = { COL_ID };
            String seleccion = COL_USUARIO + " = ?";
            String[] args = { correo };

            cursor = db.query(TABLA_USUARIOS, columnas, seleccion, args, null, null, null);
            return cursor.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }


    public boolean validarCredenciales(String correo, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String[] columnas = { COL_PASSWORD };
            String seleccion = COL_USUARIO + " = ?";
            String[] args = { correo };

            cursor = db.query(TABLA_USUARIOS, columnas, seleccion, args, null, null, null);

            if (cursor.moveToFirst()) {
                int passwordIndex = cursor.getColumnIndex(COL_PASSWORD);
                if (passwordIndex != -1) {
                    String passwordGuardada = cursor.getString(passwordIndex);
                    return password.equals(passwordGuardada);
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    /**
     * Obtiene un usuario por su correo
     * @param correo El correo del usuario
     * @return Objeto Usuario o null si no existe
     */
    public Usuario obtenerUsuario(String correo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String[] columnas = { COL_ID, COL_USUARIO, COL_PASSWORD };
            String seleccion = COL_USUARIO + " = ?";
            String[] args = { correo };

            cursor = db.query(TABLA_USUARIOS, columnas, seleccion, args, null, null, null);

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COL_ID);
                int usuarioIndex = cursor.getColumnIndex(COL_USUARIO);
                int passwordIndex = cursor.getColumnIndex(COL_PASSWORD);

                if (idIndex != -1 && usuarioIndex != -1 && passwordIndex != -1) {
                    return new Usuario(
                            cursor.getInt(idIndex),
                            cursor.getString(usuarioIndex),
                            cursor.getString(passwordIndex)
                    );
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    /**
     * Obtiene todos los usuarios de la base de datos
     * @return Lista de usuarios
     */
    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLA_USUARIOS, null, null, null, null, null, null);

            int idIndex = cursor.getColumnIndex(COL_ID);
            int usuarioIndex = cursor.getColumnIndex(COL_USUARIO);
            int passwordIndex = cursor.getColumnIndex(COL_PASSWORD);

            if (idIndex != -1 && usuarioIndex != -1 && passwordIndex != -1) {
                while (cursor.moveToNext()) {
                    Usuario usuario = new Usuario(
                            cursor.getInt(idIndex),
                            cursor.getString(usuarioIndex),
                            cursor.getString(passwordIndex)
                    );
                    listaUsuarios.add(usuario);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return listaUsuarios;
    }

    /**
     * Actualiza la contraseña de un usuario
     * @param correo Correo del usuario
     * @param nuevaPassword Nueva contraseña
     * @return Número de filas actualizadas
     */
    public int actualizarPassword(String correo, String nuevaPassword) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_PASSWORD, nuevaPassword);

            String whereClause = COL_USUARIO + " = ?";
            String[] whereArgs = { correo };

            return db.update(TABLA_USUARIOS, values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (db != null) db.close();
        }
    }

    /**
     * Elimina un usuario de la base de datos
     * @param correo Correo del usuario a eliminar
     * @return Número de filas eliminadas
     */
    public int eliminarUsuario(String correo) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String whereClause = COL_USUARIO + " = ?";
            String[] whereArgs = { correo };

            return db.delete(TABLA_USUARIOS, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (db != null) db.close();
        }
    }

    /**
     * Cuenta el número total de usuarios en la base de datos
     * @return Número de usuarios
     */
    public int contarUsuarios() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLA_USUARIOS, null);

            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}