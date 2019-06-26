package org.techtown.database;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabaseObj;
    EditText editTextName, editTextPhoneNumber,editTextAgeNumber;
    String NameHolder, NumberHolder, AgeHolder, SQLiteDataBaseQueryHolder, databaseName,tableName ;
    TextView status;
    Button EnterData, viewdb,delete;
    Boolean EditTextEmptyHold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EnterData = (Button)findViewById(R.id.button);
        viewdb = (Button)findViewById(R.id.view);
        delete = (Button)findViewById(R.id.delete);
        editTextName = (EditText)findViewById(R.id.editText);
        editTextPhoneNumber = (EditText)findViewById(R.id.editText2);
        editTextAgeNumber = (EditText)findViewById(R.id.editText3);

        EnterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEditTextStatus();
                InsertDataIntoSQLiteDatabase(tableName);
            }
        });

        final EditText databaseNameInput = (EditText) findViewById(R.id.databaseNameInput);
        final EditText tableNameInput = (EditText) findViewById(R.id.tableNameInput);

        Button createDatabaseBtn = (Button) findViewById(R.id.createDatabaseBtn);
        createDatabaseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                databaseName = databaseNameInput.getText().toString();
                if( databaseName.length() < 8 )
                {

                    Toast
                            .makeText(MainActivity.this,"이름은 .db제외 5글자 이상이여야 합니다." , Toast.LENGTH_LONG)
                            .show();
                }else {
                    SQLiteDataBaseBuild(databaseName);
                }
            }
        });

        Button createTableBtn = (Button) findViewById(R.id.createTableBtn);
        createTableBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tableName = tableNameInput.getText().toString();
                SQLiteTableBuild(tableName);
            }
        });

        viewdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                //액티비티 시작!
                intent.putExtra("dbname",databaseName);
                intent.putExtra("tbname",tableName);
                startActivity(intent);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                //액티비티 시작!
                intent.putExtra("dbname",databaseName);
                intent.putExtra("tbname",tableName);
                startActivity(intent);

            }
        });

        status = (TextView) findViewById(R.id.status);


    }

    public void SQLiteDataBaseBuild(String dbName){
        println("creating database [" + dbName + "].");
        try {
            sqLiteDatabaseObj = openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
            println("database is created.");
        }catch (Exception ex) {
            ex.printStackTrace();
            println("database is not created.");
        }
    }


    public void SQLiteTableBuild(String tableName){
        println("creating table [" + tableName + "].");
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, named VARCHAR ,phone_number VARCHAR, age VARCHAR);");

    }

    public void CheckEditTextStatus(){

        NameHolder = editTextName.getText().toString() ;
        NumberHolder = editTextPhoneNumber.getText().toString();
        AgeHolder = editTextAgeNumber.getText().toString();

        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(NumberHolder ) || TextUtils.isEmpty(AgeHolder ) || TextUtils.isEmpty(databaseName ) || TextUtils.isEmpty(tableName )){

            EditTextEmptyHold = false ;

        }
        else {

            EditTextEmptyHold = true ;
            EmptyEditTextAfterDataInsert();
        }
    }

    public void InsertDataIntoSQLiteDatabase(String tableName){

        if(EditTextEmptyHold == true)
        {

            SQLiteDataBaseQueryHolder = "INSERT INTO " + tableName + "(named,phone_number,age) VALUES('"+NameHolder+"', '"+NumberHolder+"', '"+AgeHolder+"');";

            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            Toast.makeText(MainActivity.this,"DB 저장 성공", Toast.LENGTH_LONG).show();
            println("database is saved.");

        }
        else {

            Toast.makeText(MainActivity.this,"빈칸 없이 채워주세요.", Toast.LENGTH_LONG).show();

        }

    }

    public void EmptyEditTextAfterDataInsert(){

        editTextName.getText().clear();

        editTextPhoneNumber.getText().clear();

        editTextAgeNumber.getText().clear();

    }
    private void println(String msg) {
        Log.d("MainActivity", msg);
        status.append("\n" + msg);

    }
}