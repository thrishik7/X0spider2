package com.example.spiderapp2;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.nio.Buffer;

public class Activity2 extends AppCompatActivity {
    DatabaseHelper myDb;
    private Button record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        record=(Button)findViewById(R.id.button_1);
        myDb=  new DatabaseHelper(this);
        viewAll();
    }


    public void viewAll()
    {
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Cursor res= myDb.getAllData();
              if(res.getCount()==0) {
                  showMessage("Error", "Nothing found");
                  return;
              }
                      StringBuffer buffer= new StringBuffer();
              while(res.moveToNext()){
                  buffer.append("id:"+res.getString(0)+"\n");
                  buffer.append("WINNER:"+res.getString(1)+"\n");
                  buffer.append("TIMER(in millisec):"+res.getString(2)+"\n \n");
              }
                 showMessage("RECORD", buffer.toString());
            }

        });

    }

 public void showMessage(String title, String Message){
     AlertDialog.Builder builder = new AlertDialog.Builder(this);
     builder.setCancelable(true);
     builder.setTitle(title);
     builder.setMessage(Message);
     builder.show();
 }
}
