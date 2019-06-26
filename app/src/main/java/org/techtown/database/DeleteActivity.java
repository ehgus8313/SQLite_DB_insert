package org.techtown.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 데이터베이스를 조회하는 방법을 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class DeleteActivity extends AppCompatActivity {

    private TextView status;

    public static final String TAG = "DeleteActivity";

    private static String DATABASE_NAME = null;
    private static String TABLE_NAME = null;
    private static String JOGUN_NAME = null;
    private static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private SQLiteDatabase db3;
    LinearLayout Btlayout;
    Button alldelete,partdelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Intent intent = getIntent();
        String dbname = intent.getStringExtra("dbname");
        String tbname = intent.getStringExtra("tbname");

        //조건검색
        final Button joBtn = (Button) findViewById(R.id.joBtn);
        final EditText searchinput = (EditText) findViewById(R.id.searchinput);
        Btlayout = (LinearLayout) findViewById(R.id.Btlayout);
        alldelete = (Button)findViewById(R.id.alldelete);
        partdelete = (Button)findViewById(R.id.partdelete);



        status = (TextView) findViewById(R.id.status);
        final EditText input01 = (EditText) findViewById(R.id.input01);
        final EditText input02 = (EditText) findViewById(R.id.input02);
        input01.setText(dbname);
        input02.setText(tbname);


        joBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                JOGUN_NAME = searchinput.getText().toString();
                DATABASE_NAME = input01.getText().toString();
                TABLE_NAME = input02.getText().toString();
                boolean isOpen2 = openDatabase();
                if (isOpen2) {
                    //executeRawQuery();
                    executeRawQueryParam2(TABLE_NAME, JOGUN_NAME);
                    joBtn.setVisibility(View.GONE);
                    Btlayout.setVisibility(View.VISIBLE);
                }
            }
        });

        alldelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TABLE_NAME = input02.getText().toString();
                alldelete(TABLE_NAME);
            }
        });

        partdelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                JOGUN_NAME = searchinput.getText().toString();
                DATABASE_NAME = input01.getText().toString();
                TABLE_NAME = input02.getText().toString();
                boolean isOpen2 = openDatabase();
                if (isOpen2) {
                    partdelete(TABLE_NAME, JOGUN_NAME);
                }
                }
        });
    }



    private boolean openDatabase() {
        println("데이터 베이스를 오픈합니다. [" + DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        db3 = dbHelper.getReadableDatabase();

        return true;
    }


    public void executeRawQueryParam2(String tableName, String jogunName) {
        println("\n" + "[" + jogunName + "]" + "이 포함된 내용을 찾습니다.\n");

        String SQL = "select named, phone_number, age "
                + " from " + tableName
                + " where age > ?";
        String[] args= {"100"};

        Cursor c2 = db3.rawQuery(SQL, args);
        int recordCount = c2.getCount();
        //println("cursor count : " + recordCount + "\n");

        for (int i = 0; i < recordCount; i++) {
            c2.moveToNext();
            String named = c2.getString(0);
            String phone_number = c2.getString(1);
            String age = c2.getString(2);
            if(jogunName.equals(c2.getString(2))) {
                println("검색결과" + " : \n" + "이름 : " + named + ", \n" + "번호 : " + phone_number + ", \n" + "나이 : "  + age);
            }
        }

        c2.close();
    }

    public void partdelete(String tableName, String jogunName) {

        println("나이가\n" + "[" + jogunName + "]" + "인 record를 삭제합니다.\n");

        db.delete(tableName,  "age" + "=" + jogunName, null); //이렇게 하면 id가 1인 row를 삭제하게 됩니다.

                println("나이가\n" + "[" + jogunName + "]" + "인 record를 삭제했습니다.\n");

        }
    public void alldelete(String tableName) {
        db.execSQL("delete from " + tableName);
        println("\n테이블" + "[" + tableName + "]" + " 를 삭제했습니다.\n");
    }


    private void println(String msg) {
        Log.d(TAG, msg);
        status.append("\n" + msg);

    }


    private class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {


        }

        public void onOpen(SQLiteDatabase db) {
            //println("opened database [" + DATABASE_NAME + "].");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");

        }
    }

}
