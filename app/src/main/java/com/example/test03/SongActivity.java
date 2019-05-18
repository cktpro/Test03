package com.example.test03;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.test03.Bean.Songs;
import com.example.test03.Bean.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SongActivity extends AppCompatActivity {

    private ListView listView;

    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;


    private static final int MY_REQUEST_CODE = 1000;

    private final List<Songs> songsList = new ArrayList<Songs>();
    private ArrayAdapter<Songs> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);


        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listView);

        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.createDefaultNotesIfNeed();

        List<Songs> list=  db.getAllNotes();
        this.songsList.addAll(list);


        // Định nghĩa một Adapter.
        // 1 - Context
        // 2 - Layout cho các dòng.
        // 3 - ID của TextView mà dữ liệu sẽ được ghi vào
        // 4 - Danh sách dữ liệu.

        this.listViewAdapter = new ArrayAdapter<Songs>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, this.songsList);


        // Đăng ký Adapter cho ListView.
        this.listView.setAdapter(this.listViewAdapter);

        // Đăng ký Context menu cho ListView.
        registerForContextMenu(this.listView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_VIEW , 0, "View Songs");
        menu.add(0, MENU_ITEM_CREATE , 1, "Create Songs");
        menu.add(0, MENU_ITEM_EDIT , 2, "Edit Songs");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete Songs");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Songs selectedSongs = (Songs) this.listView.getItemAtPosition(info.position);

        if(item.getItemId() == MENU_ITEM_VIEW){
            Toast.makeText(getApplicationContext(), selectedSongs.getSongContent(),Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId() == MENU_ITEM_CREATE){
            Intent intent = new Intent(this, AddEditSongActivity.class);

            // Start AddEditSongActivity, có phản hồi.
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        }
        else if(item.getItemId() == MENU_ITEM_EDIT ){
            Intent intent = new Intent(this, AddEditSongActivity.class);
            intent.putExtra("songs", selectedSongs);

            // Start AddEditSongActivity, có phản hồi.
            this.startActivityForResult(intent,MY_REQUEST_CODE);
        }
        else if(item.getItemId() == MENU_ITEM_DELETE){
            // Hỏi trước khi xóa.
            new AlertDialog.Builder(this)
                    .setMessage(selectedSongs.getSongTitle()+". Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteNote(selectedSongs);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {
            return false;
        }
        return true;
    }

    // Người dùng đồng ý xóa một Songs.
    private void deleteNote(Songs songs)  {
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.deleteNote(songs);
        this.songsList.remove(songs);
        // Refresh ListView.
        this.listViewAdapter.notifyDataSetChanged();
    }


    // Khi AddEditSongActivity hoàn thành, nó gửi phản hồi lại.
    // (Nếu bạn đã start nó bằng cách sử dụng startActivityForResult()  )
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE ) {
            boolean needRefresh = data.getBooleanExtra("needRefresh",true);
            // Refresh ListView
            if(needRefresh) {
                this.songsList.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(this);
                List<Songs> list=  db.getAllNotes();
                this.songsList.addAll(list);
                // Thông báo dữ liệu thay đổi (Để refresh ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
