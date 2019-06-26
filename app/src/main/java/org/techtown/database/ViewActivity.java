package org.techtown.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 데이터베이스를 조회하는 방법을 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class ViewActivity extends AppCompatActivity {

    private TextView status;

    public static final String TAG = "ViewActivity";

    private static String DATABASE_NAME = null;
    private static String TABLE_NAME = null;
    private static String JOGUN_NAME = null;
    private static int DATABASE_VERSION = 1;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper2;
    private SQLiteDatabase db2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Intent intent = getIntent();
        String dbname = intent.getStringExtra("dbname");
        String tbname = intent.getStringExtra("tbname");

        //조건검색
        final Button searchBtn = (Button) findViewById(R.id.searchBtn);
        final Button joBtn = (Button) findViewById(R.id.joBtn);
        final EditText searchinput = (EditText) findViewById(R.id.searchinput);


        status = (TextView) findViewById(R.id.status);
        final EditText input01 = (EditText) findViewById(R.id.input01);
        final EditText input02 = (EditText) findViewById(R.id.input02);
        input01.setText(dbname);
        input02.setText(tbname);


        final Button queryBtn = (Button) findViewById(R.id.queryBtn);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DATABASE_NAME = input01.getText().toString();
                TABLE_NAME = input02.getText().toString();
                boolean isOpen = openDatabase();
                if (isOpen) {
                    executeRawQuery();
                    executeRawQueryParam();
                }

            }
        });


        joBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                JOGUN_NAME = searchinput.getText().toString();
                DATABASE_NAME = input01.getText().toString();
                TABLE_NAME = input02.getText().toString();
                boolean isOpen2 = openDatabase();
                if (isOpen2) {
                    //executeRawQuery();
                    executeRawQueryParam2(TABLE_NAME, JOGUN_NAME);
                }
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                searchBtn.setVisibility(View.GONE);
                searchinput.setVisibility(View.VISIBLE);
                joBtn.setVisibility(View.VISIBLE);
                queryBtn.setVisibility(View.GONE);

            }
        });

    }



    private boolean openDatabase() {
        println("데이터 베이스를 오픈합니다. [" + DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        db2 = dbHelper.getReadableDatabase();

        return true;
    }

    private void executeRawQuery() {
        println("\nexecuteRawQuery called.\n");

        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME, null);
        println("cursor count : " + c1.getCount());

        c1.moveToNext();
        println("record count : " + c1.getInt(0));

        c1.close();

    }

    public void executeRawQueryParam() {
        println("\nexecuteRawQueryParam called.\n");

        String SQL = "select named, phone_number, age "
                + " from " + TABLE_NAME
                + " where age > ?";
        String[] args= {"100"};

        Cursor c1 = db.rawQuery(SQL, args);
        int recordCount = c1.getCount();
        println("cursor count : " + recordCount + "\n");

        for (int i = 0; i < recordCount; i++) {
            c1.moveToNext();
            String named = c1.getString(0);
            String phone_number = c1.getString(1);
            String age = c1.getString(2);

            println("Record #" + i + " : " + "이름 : " + named + ", \n"+ "번호 : " + phone_number + ", \n"+ "나이 : " + age);
        }

        c1.close();
    }



    public void executeRawQueryParam2(String tableName, String jogunName) {
        println("\n" + "[" + jogunName + "]" + "이 포함된 내용을 찾습니다.\n");

        String SQL = "select named, phone_number, age "
                + " from " + tableName
                + " where age > ?";
        String[] args= {"100"};

        Cursor c2 = db2.rawQuery(SQL, args);
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
