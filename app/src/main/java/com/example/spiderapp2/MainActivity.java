package com.example.spiderapp2;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.cardemulation.HostApduService;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper myDb;
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCounts = 0;
    private int player1Points = 0;
    private int player2Points = 0;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private Button reset_button;
    private Button restart_button;
    private Button record;
    private Chronometer timerHere;
    private  TextView restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textViewPlayer1 = (TextView) findViewById(R.id.text_view_player1);
        textViewPlayer2 = (TextView) findViewById(R.id.text_view_player2);
        reset_button = (Button) findViewById(R.id.button_reset);
        restart_button=(Button)findViewById(R.id.restart_button);
        record=(Button)findViewById(R.id.button_1);
        timerHere= findViewById(R.id.timerHere);
        myDb=  new DatabaseHelper(this);
        restart=(TextView)findViewById(R.id.restart);
            timerHere.setBase(SystemClock.elapsedRealtime());
            timerHere.start();
        
        restart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHere.stop();
                timerHere.setBase(SystemClock.elapsedRealtime());
                timerHere.start();

                restartGame();
            }
        });


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openactivity();

            }
        });


        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHere.stop();




                    timerHere.setBase(SystemClock.elapsedRealtime());
                      timerHere.start();
                resetGame();
            }
        });


        buttons[0][0] = (Button) findViewById(R.id.button_00);
        buttons[0][1] = (Button) findViewById(R.id.button_01);
        buttons[0][2] = (Button) findViewById(R.id.button_02);
        buttons[1][0] = (Button) findViewById(R.id.button_10);
        buttons[1][1] = (Button) findViewById(R.id.button_11);
        buttons[1][2] = (Button) findViewById(R.id.button_12);
        buttons[2][0] = (Button) findViewById(R.id.button_20);
        buttons[2][1] = (Button) findViewById(R.id.button_21);
        buttons[2][2] = (Button) findViewById(R.id.button_22);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                buttons[i][j].setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Button b = (Button) v;
        if (!b.getText().toString().equals("")) {
            return;
        }
        if (player1Turn) {
            b.setText("X");
            b.setTextColor(Color.RED);

        } else {
            b.setText("O");
            b.setTextColor(Color.rgb(11, 102,35));
        }
        roundCounts++;

        if (checkForWin()) {


                  timerHere.stop();
                  restart.setText("RESTART TO PLAY AGAIN");
                  restart.setTextColor(Color.RED);

            if (player1Turn) {
                int elapsedMillis1=(int)(SystemClock.elapsedRealtime()-timerHere.getBase());
                Boolean isInserted=myDb.insertData("PLAYER 1",elapsedMillis1);
                if(isInserted==true)
                {
                    Toast.makeText(MainActivity.this, "DATA INSERTED", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "DATA not INSERTED", Toast.LENGTH_LONG).show();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                          player1Wins();
                    }
                } ,500);

            } else
            {
                int elapsedMillis2=(int)(SystemClock.elapsedRealtime()-timerHere.getBase());
               Boolean isInserted= myDb.insertData("PLAYER 2",elapsedMillis2);
                if(isInserted==true)
                {
                    Toast.makeText(MainActivity.this, "DATA INSERTED", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "DATA not INSERTED", Toast.LENGTH_LONG).show();
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        player2Wins();
                    }
                }, 500)     ;
                 }
        } else {





            if (roundCounts == 9) {
                timerHere.stop();
                restart.setText("RESTART THE GAME TO PLAY AGAIN");
                restart.setTextColor(Color.RED);
                int elapsedMillis3=(int)(SystemClock.elapsedRealtime()-timerHere.getBase());
                Boolean isInserted= myDb.insertData("DRAW",elapsedMillis3);
                if(isInserted==true)
                {
                    Toast.makeText(MainActivity.this, "DATA INSERTED", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "DATA not INSERTED", Toast.LENGTH_LONG).show();
                }
                timerHere.stop();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                          draw();
                    }
                },500 ) ;
               
            } else {
                player1Turn = !player1Turn;
            }
        }
    }


    public void DeleteData(){


         myDb.deleteData();

    }


     private void openactivity(){

         Intent intent = new Intent(this, Activity2.class);
         startActivity(intent);
     }


    private void player1Wins() {
        player1Points++;


        Toast.makeText(this, "Player 1 Wins!", Toast.LENGTH_SHORT).show();
        updatePointText();
        resetbutt();
    }

    private void updatePointText() {
        textViewPlayer1.setText("Player 1:  "+player1Points);
        textViewPlayer2.setText("Player 2:  "+player2Points);

    }
   private void resetbutt()
   {

       for(int i=0; i<3; i++)
           for(int j=0; j<3; j++)
               buttons[i][j].setText("");
       roundCounts=0;
       player1Turn=true;

   }

    private void resetbutton() {


        restart.setText("....");
        restart.setTextColor(Color.BLACK);
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                buttons[i][j].setText("");
            roundCounts=0;
            player1Turn=true;
    }


    private void player2Wins()
      {
            player2Points++;
            Toast.makeText(this, "Player 2 Wins!", Toast.LENGTH_SHORT).show();
             updatePointText();
             resetbutt();
      }

          private void draw()
          {

                    Toast.makeText(this, "DRAW!", Toast.LENGTH_SHORT).show();
                          resetbutton();    
          }

     private void resetGame()
     {
         DeleteData();
         restart.setText("....");
         restart.setTextColor(Color.BLACK);
         player1Points=0     ;
         player2Points=0;
         updatePointText();
         resetbutton();
          DeleteData();

     }

    private void restartGame()
    {
        updatePointText();
        resetbutton();
    }




    private boolean checkForWin() {

            String[][] field=new String[3][3] ;
            for(int i=0; i<3; i++)
                for(int j=0; j<3; j++)
                    field[i][j]=buttons[i][j].getText().toString();

                for(int i=0; i<3;i++)
                {
                    if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("") )
                    {
                        return true;
                    }
                }
                        for(int i=0; i<3;i++)
                        {
                                     if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("") )
                                     {
                                         return true;
                                     }

                        }


          if(field[0][0].equals(field[1][1])&& field[0][0].equals(field[2][2]) && !field[0][0].equals(""))
          {
              return true;
          }
             if(field[0][2].equals(field[1][1])&& field[0][2].equals(field[2][0]) && !field[0][2].equals(""))
             {
                 return true;
             }


             return false;
    }
}