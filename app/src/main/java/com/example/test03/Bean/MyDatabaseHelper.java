package com.example.test03.Bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 1;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Song_Manager";


    // Tên bảng: Songs.
    private static final String TABLE_SONG = "Song";

    private static final String COLUMN_ID ="Id";
    private static final String COLUMN_TITLE ="Title";
    private static final String COLUMN_CONTENT = "Content";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script tạo bảng.
        String script = "CREATE TABLE " + TABLE_SONG + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TITLE + " TEXT,"
                + COLUMN_CONTENT + " TEXT" + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);


        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng Songs chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
    public void createDefaultNotesIfNeed()  {
        int count = this.getNotesCount();
        if(count ==0 ) {
            Songs songs1 = new Songs("We don't talk anymore",
                    "Chalie Puth");
            Songs songs2 = new Songs("Em Của Ngày Hôm Qua",
                    "MTP");
            Songs songs3 = new Songs("Anh Sai Rồi",
                    "MTP");
            Songs songs4 = new Songs("On My Way",
                    "Alan Walker");
            this.addNote(songs1);
            this.addNote(songs2);
            this.addNote(songs3);
            this.addNote(songs4);
        }
    }


    public void addNote(Songs songs) {
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + songs.getSongTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, songs.getSongTitle());
        values.put(COLUMN_CONTENT, songs.getSongContent());


        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_SONG, null, values);


        // Đóng kết nối database.
        db.close();
    }


    public Songs getNote(int id) {
        Log.i(TAG, "MyDatabaseHelper.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SONG, new String[] { COLUMN_ID,
                        COLUMN_TITLE, COLUMN_CONTENT }, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Songs songs = new Songs(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return songs
        return songs;
    }


    public List<Songs> getAllNotes() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<Songs> songsList = new ArrayList<Songs>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SONG;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Songs songs = new Songs();
                songs.setSongId(Integer.parseInt(cursor.getString(0)));
                songs.setSongTitle(cursor.getString(1));
                songs.setNoteContent(cursor.getString(2));

                // Thêm vào danh sách.
                songsList.add(songs);
            } while (cursor.moveToNext());
        }

        // return note list
        return songsList;
    }

    public int getNotesCount() {
        Log.i(TAG, "MyDatabaseHelper.getNotesCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_SONG;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    public int updateNote(Songs songs) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... "  + songs.getSongTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, songs.getSongTitle());
        values.put(COLUMN_CONTENT, songs.getSongContent());

        // updating row
        return db.update(TABLE_SONG, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(songs.getSongId())});
    }

    public void deleteNote(Songs songs) {
        Log.i(TAG, "MyDatabaseHelper.updateNote ... " + songs.getSongTitle() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG, COLUMN_ID + " = ?",
                new String[] { String.valueOf(songs.getSongId()) });
        db.close();
    }
}
