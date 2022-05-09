package com.example.a1022project;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dbUtil.wordGenerate;
import dbUtil.writeTxt;



public class MainActivity extends AppCompatActivity {
    public int wins = 0; // find a way to store these locally
    public int matches = 0;
    public int losses = 0;
    public float winRate = 0.0F;
    public Wordle m;
    public String guess = "";
    public int difficulty;
    public int result1 = 0; // 0-undecided 1-lost 2-win
    private final Handler mHandler = new Handler();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button close;
    Time theTime = new Time();

    private static final String FILE_NAME = "savedInfo.txt";
    public String txt = "wins,0,losses,0,matches,0,winRate,0.0";



    private final Runnable resultsRunnable = new Runnable(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            setContentView(R.layout.results);
            TextView timePlayed = (TextView) findViewById(R.id.timeMessage2);
            timePlayed.setText(theTime.getTimer());
            TextView result = (TextView) findViewById(R.id.resultMessage);
            TextView resultWord = (TextView) findViewById(R.id.resultMessage3);
            TextView resultStat = (TextView) findViewById(R.id.resultMessage4);
            TextView resultNew = (TextView) findViewById(R.id.resultMessage5);

            TextView resultRate = (TextView) findViewById(R.id.resultWinrate);
            TextView resultRate2 = (TextView) findViewById(R.id.resultWinrate2);
            resultWord.setText(String.valueOf(m.answer));
            float fOld=(((float)wins/matches)*100);
            float roundedFloatOld = (float) ((float)Math.round(fOld * 100.0) / 100.0);
            if(result1 == 1){ // lose
                matches++;
                losses++;
                result.setText(String.valueOf("You Lost!"));
                resultStat.setText(String.valueOf("+1 Loss"));
                result.setBackgroundResource(R.drawable.wrong);
                resultWord.setBackgroundResource(R.drawable.wrong);
                resultStat.setBackgroundResource(R.drawable.wrong);
                resultRate2.setBackgroundResource(R.drawable.wrong);
                resultNew.setBackgroundResource(R.drawable.wrong);
                float f1=(((float)wins/matches)*100);
                float roundedFloat1 = (float) ((float)Math.round(f1 * 100.0) / 100.0);
                float roundedFloat2 = roundedFloatOld-roundedFloat1;
                float roundedFloat3 = (float) ((float)Math.round(roundedFloat2 * 100.0) / 100.0);
                resultRate2.setText("-"+String.valueOf(roundedFloat3)+"%");
            }else if (result1 == 2){ // win
                matches++;
                wins++;
                result.setText(String.valueOf("You Won!"));
                resultStat.setText(String.valueOf("+1 Win"));
                result.setBackgroundResource(R.drawable.correct);
                resultWord.setBackgroundResource(R.drawable.correct);
                resultStat.setBackgroundResource(R.drawable.correct);
                resultRate2.setBackgroundResource(R.drawable.correct);
                resultNew.setBackgroundResource(R.drawable.correct);
                float f1=(((float)wins/matches)*100);
                float roundedFloat1 = (float) ((float)Math.round(f1 * 100.0) / 100.0);
                float roundedFloat2 = roundedFloat1-roundedFloatOld;
                float roundedFloat3 = (float) ((float)Math.round(roundedFloat2 * 100.0) / 100.0);
                resultRate2.setText("+"+String.valueOf(roundedFloat3)+"%");
            }
            float f=(((float)wins/matches)*100);
            float roundedFloat = (float) ((float)Math.round(f * 100.0) / 100.0);
            resultRate.setText(String.valueOf(roundedFloat)+"%");
            winRate = roundedFloat;
            saveAsTxt();
        }
    };

    private final Runnable wordInvalidRunnable = new Runnable(){
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "Word not in word list.", Toast.LENGTH_SHORT).show();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTxt();
        setContentView(R.layout.start);
    }

    public void closeInfo(View v){
        setContentView(R.layout.game);
    }

    public void moveStart(View v){
        setContentView(R.layout.start);
    }

    public void moveDifficulty(View v){
        setContentView(R.layout.difficulty);
    }

    public void moveOptions(View v){
        setContentView(R.layout.options);
    }
    @SuppressLint("SetTextI18n")
    public void moveStats(View v){
        setContentView(R.layout.statistics);
        TextView winV = (TextView) findViewById(R.id.wins);
        TextView winrateV = (TextView) findViewById(R.id.winrate);
        TextView matchesV = (TextView) findViewById(R.id.matches);
        TextView lossesV = (TextView) findViewById(R.id.losses);
        winV.setText(String.valueOf(this.wins));
        matchesV.setText(String.valueOf(this.matches));
        lossesV.setText(String.valueOf(this.matches-this.wins));
        float f=(((float)this.wins/this.matches)*100);
        float roundedFloat = (float) ((float)Math.round(f * 100.0) / 100.0);
        winrateV.setText(String.valueOf(roundedFloat)+"%");
    }
    public void moveTut(View v){
        dialogBuilder = new AlertDialog.Builder(this);
        final View PopupView = getLayoutInflater().inflate(R.layout.howtoplay,null);
        close = (Button) PopupView.findViewById(R.id.close);

        dialogBuilder.setView(PopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void moveGame(View v) throws Exception {
        setContentView(R.layout.game);
        this.result1 = 0; //resets result to 0
        this.guess = ""; // resets guess to 0

        Button b = (Button)v;
        String buttonText = b.getText().toString();


        if(buttonText.equals("NORMAL")){

            wordGenerate wG = new wordGenerate();
            String word = wG.getNormalWord();
            //system.out.println(word);
            this.m = new Wordle(word);
            this.difficulty = 5;
            initTime();
            change2Normal(v);
        } else if(buttonText.equals("EASY")){

            wordGenerate wG = new wordGenerate();
            String word = wG.getEasyWord();
            this.m = new Wordle(word);
            this.difficulty = 4;
            initTime();
            change2Easy(v);
        } else if(buttonText.equals("HARD")){
            wordGenerate wG = new wordGenerate();
            String word = wG.getHardWord();
            this.m = new Wordle(word);
            initTime();
            this.difficulty = 6;
            //change2Hard(v);
        } else if(buttonText.equals("CUSTOM")){
            customWord(v);
            //this.difficulty = 65535;
        }
    }

    public void quitGame(View v){

        AlertDialog.Builder builder
                = new AlertDialog.Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to EXIT?");
        builder.setTitle("Attention");
        builder.setCancelable(false);

        builder.setNegativeButton(
                        "LEAVE!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quitYes();
                            }
                        });

        builder.setPositiveButton(
                        "Try Again?",

                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // If user click no then dialog box is canceled
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.RED);
        Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.GREEN);


    }

    public void quitYes() {
        this.result1 = 1; // set lose
        mHandler.postDelayed(resultsRunnable,100);
    }

    @SuppressLint("SetTextI18n")
    public void clickEnter(View v) {
        if (this.guess.length() == this.difficulty) { // if length of guess is good
            if (this.m.inWordL(this.guess)){ //checks if word is in wordlist
                TextView[] l = new TextView[this.difficulty];
                Button[] b = new Button[26];
                b[0] = (Button) findViewById(R.id.A);
                b[1] = (Button) findViewById(R.id.B);
                b[2] = (Button) findViewById(R.id.C);
                b[3] = (Button) findViewById(R.id.D);
                b[4] = (Button) findViewById(R.id.E);
                b[5] = (Button) findViewById(R.id.F);
                b[6] = (Button) findViewById(R.id.G);
                b[7] = (Button) findViewById(R.id.H);
                b[8] = (Button) findViewById(R.id.I);
                b[9] = (Button) findViewById(R.id.J);
                b[10] = (Button) findViewById(R.id.K);
                b[11] = (Button) findViewById(R.id.L);
                b[12] = (Button) findViewById(R.id.M);
                b[13] = (Button) findViewById(R.id.N);
                b[14] = (Button) findViewById(R.id.O);
                b[15] = (Button) findViewById(R.id.P);
                b[16] = (Button) findViewById(R.id.Q);
                b[17] = (Button) findViewById(R.id.R);
                b[18] = (Button) findViewById(R.id.S);
                b[19] = (Button) findViewById(R.id.T);
                b[20] = (Button) findViewById(R.id.U);
                b[21] = (Button) findViewById(R.id.V);
                b[22] = (Button) findViewById(R.id.W);
                b[23] = (Button) findViewById(R.id.X);
                b[24] = (Button) findViewById(R.id.Y);
                b[25] = (Button) findViewById(R.id.Z);

                this.m.setGuess(this.guess);
                char[] format = this.m.compare();
                if (this.m.getCLine() == 1) {
                    l[0] = (TextView) findViewById(R.id.L1_1);
                    l[1] = (TextView) findViewById(R.id.L1_2);
                    l[2] = (TextView) findViewById(R.id.L1_3);
                    l[3] = (TextView) findViewById(R.id.L1_4);
                    if(this.difficulty == 5) {
                        l[4] = (TextView) findViewById(R.id.L1_5);
                    } else if(this.difficulty == 6) {
                        l[4] = (TextView) findViewById(R.id.L1_5);
                        l[5] = (TextView) findViewById(R.id.L1_6);

                    }

                } else if (this.m.getCLine() == 2) {
                    l[0] = (TextView) findViewById(R.id.L2_1);
                    l[1] = (TextView) findViewById(R.id.L2_2);
                    l[2] = (TextView) findViewById(R.id.L2_3);
                    l[3] = (TextView) findViewById(R.id.L2_4);
                    //l[4] = (TextView) findViewById(R.id.L2_5);
                    if(this.difficulty == 5) {
                        l[4] = (TextView) findViewById(R.id.L2_5);
                    } else if(this.difficulty == 6) {
                        l[4] = (TextView) findViewById(R.id.L2_5);
                        l[5] = (TextView) findViewById(R.id.L2_6);
                        //////
                    }
                } else if (this.m.getCLine() == 3) {
                    l[0] = (TextView) findViewById(R.id.L3_1);
                    l[1] = (TextView) findViewById(R.id.L3_2);
                    l[2] = (TextView) findViewById(R.id.L3_3);
                    l[3] = (TextView) findViewById(R.id.L3_4);
                    //l[4] = (TextView) findViewById(R.id.L3_5);
                    if(this.difficulty == 5) {
                        l[4] = (TextView) findViewById(R.id.L3_5);
                    } else if(this.difficulty == 6) {
                        l[4] = (TextView) findViewById(R.id.L3_5);
                        l[5] = (TextView) findViewById(R.id.L3_6);
                        //////
                    }
                } else if (this.m.getCLine() == 4) {
                    l[0] = (TextView) findViewById(R.id.L4_1);
                    l[1] = (TextView) findViewById(R.id.L4_2);
                    l[2] = (TextView) findViewById(R.id.L4_3);
                    l[3] = (TextView) findViewById(R.id.L4_4);
                    //l[4] = (TextView) findViewById(R.id.L4_5);
                    if(this.difficulty == 5) {
                        l[4] = (TextView) findViewById(R.id.L4_5);
                    } else if(this.difficulty == 6) {
                        l[4] = (TextView) findViewById(R.id.L4_5);
                        l[5] = (TextView) findViewById(R.id.L4_6);
                        //////
                    }
                } else if (this.m.getCLine() == 5) {
                    l[0] = (TextView) findViewById(R.id.L5_1);
                    l[1] = (TextView) findViewById(R.id.L5_2);
                    l[2] = (TextView) findViewById(R.id.L5_3);
                    l[3] = (TextView) findViewById(R.id.L5_4);
                    //l[4] = (TextView) findViewById(R.id.L5_5);
                    if(this.difficulty == 5) {
                        l[4] = (TextView) findViewById(R.id.L5_5);
                    } else if(this.difficulty == 6) {
                        l[4] = (TextView) findViewById(R.id.L5_5);
                        l[5] = (TextView) findViewById(R.id.L5_6);
                        //////
                    }
                } else if (this.m.getCLine() == 6) {
                    l[0] = (TextView) findViewById(R.id.L6_1);
                    l[1] = (TextView) findViewById(R.id.L6_2);
                    l[2] = (TextView) findViewById(R.id.L6_3);
                    l[3] = (TextView) findViewById(R.id.L6_4);
                    //l[4] = (TextView) findViewById(R.id.L6_5);
                    if(this.difficulty == 5) {
                        l[4] = (TextView) findViewById(R.id.L6_5);
                    } else if(this.difficulty == 6) {
                        l[4] = (TextView) findViewById(R.id.L6_5);
                        l[5] = (TextView) findViewById(R.id.L6_6);
                        //////
                    }
                }
                l[0].setText(this.guess.substring(0, 1));
                l[1].setText(this.guess.substring(1, 2));
                l[2].setText(this.guess.substring(2, 3));
                l[3].setText(this.guess.substring(3, 4));

                if(this.difficulty == 5) {
                    l[4].setText(this.guess.substring(4, 5));
                } else if(this.difficulty == 6) {
                    l[4].setText(this.guess.substring(4, 5));
                    l[5].setText(this.guess.substring(5, 6));
                    //////
                }
                String[] ans = this.m.getAnswer().split("");
                String[] gues = this.m.getGuess().split("");
                for (int i = 0; i < format.length; i++) {
                    if (format[i] == 'G') {
                        l[i].setBackgroundResource(R.drawable.correct);
                    } else if (format[i] == 'Y') {
                        l[i].setBackgroundResource(R.drawable.wrong);
                    } else {
                        l[i].setBackgroundResource(R.drawable.incorrect);
                    }
                }
                for (int x = 0; x < b.length; x++) {
                    String buttonText = b[x].getText().toString();
                    for (int y = 0; y < this.difficulty; y++) {
                        if (buttonText.equals(gues[y])) {
                            b[x].setBackgroundColor(Color.parseColor("#3a3a3c")); // grey
                            for (int z = 0; z < this.difficulty; z++) {
                                if (ans[z].equals(gues[y])) {
                                    b[x].setBackgroundColor(Color.parseColor("#b59f3b")); //yellow
                                }
                            }
                        }
                    }
                }

                this.guess = "";
                String m = ans[1];
                String n = ans[2];
                String poggers = String.valueOf(m.equals(n));
                TextView guessText = (TextView) findViewById(R.id.guessPog);
                guessText.setText(String.valueOf(this.guess));
                if(this.m.checkWin()){
                    guessText.setText("YOU WON!");
                    guessText.setBackgroundResource(R.drawable.correct);
                    this.result1 = 2; // set  win
                    mHandler.postDelayed(resultsRunnable,2000);
                }else if(this.m.checkLost()){
                    guessText.setText("YOU LOST!");
                    guessText.setBackgroundResource(R.drawable.lost);
                    this.result1 = 1; // set lose
                    mHandler.postDelayed(resultsRunnable,2000);
                }
                this.m.addLine();

            }else{
                mHandler.postDelayed(wordInvalidRunnable,300);
                TextView guessText = (TextView) findViewById(R.id.guessPog);
                this.guess = "";
                guessText.setText(String.valueOf(this.guess));
            }
        }
    }
    public void clickRemove(View v){
        if(this.guess.length() >0){
            this.guess = this.guess.substring(0,this.guess.length()-1);
        }

        TextView guessText = (TextView)findViewById(R.id.guessPog);
        guessText.setText(this.guess);
    }
    public void clickLetter(View v){
        Button b = (Button)v;
        String buttonText = b.getText().toString();
        if(this.guess.length() < this.difficulty){
            this.guess += buttonText;
        }

        TextView guessText = (TextView)findViewById(R.id.guessPog);
        guessText.setText(this.guess);
    }


    //change game page UI to 6*4
    public void change2Easy(View v) {
        setContentView(R.layout.game);
        findViewById(R.id.L1_5).setVisibility(View.GONE);
        findViewById(R.id.L2_5).setVisibility(View.GONE);
        findViewById(R.id.L3_5).setVisibility(View.GONE);
        findViewById(R.id.L4_5).setVisibility(View.GONE);
        findViewById(R.id.L5_5).setVisibility(View.GONE);
        findViewById(R.id.L6_5).setVisibility(View.GONE);

        findViewById(R.id.L1_6).setVisibility(View.GONE);
        findViewById(R.id.L2_6).setVisibility(View.GONE);
        findViewById(R.id.L3_6).setVisibility(View.GONE);
        findViewById(R.id.L4_6).setVisibility(View.GONE);
        findViewById(R.id.L5_6).setVisibility(View.GONE);
        findViewById(R.id.L6_6).setVisibility(View.GONE);

        findViewById(R.id.L1_5);


//        for (int i = 1; i <= 6; i++) {
//            List<TextView> eList = new ArrayList<>(4);
//            for(int j = 1; j <= 4; j++) {
//                //String idArray = "R.id.L"+i+"_"+j;
//                int id = getResources().getIdentifier("R.id.L"+i+"_"+j, "id", getPackageName());
//                eList.add(findViewById(id));
//            }
//            //findViewById(eList).setForegroundGravity(Gravity: center);
//
//        }

    }

    //change game page UI to 6*5
    public void change2Normal(View v) {
        setContentView(R.layout.game);
        findViewById(R.id.L1_6).setVisibility(View.GONE);
        findViewById(R.id.L2_6).setVisibility(View.GONE);
        findViewById(R.id.L3_6).setVisibility(View.GONE);
        findViewById(R.id.L4_6).setVisibility(View.GONE);
        findViewById(R.id.L5_6).setVisibility(View.GONE);
        findViewById(R.id.L6_6).setVisibility(View.GONE);


    }

    //change game page UI to 6*6
    public void change2Hard(View v) {
    //nothing to change
    }

    //custom mode
    public void customWord(View v) {


        String newWord = "EMPTY";
        final EditText inputServer = new EditText(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Input a Word (4-6 letters):").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            moveDifficulty(v);
                        }
                    });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                public void onClick(DialogInterface dialog, int which) {
                    inputServer.getText().toString();
                    String newWord = inputServer.getText().toString();

                    if (!isValid(newWord)) {
                        AlertDialog.Builder b
                                = new AlertDialog.Builder(MainActivity.this);
                        b.setTitle("Attention! ");

                        b.setMessage("Your Word is Invalid! \nPlease Enter a 4-6 Letters Word!")
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                moveDifficulty(v);
                            }
                        });

                        b.show();
//                        builder.setCancelable(true);
//                        final AlertDialog dlg = builder.create();
//                        dlg.show();
//                        final Timer t = new Timer();
//                        t.schedule(new TimerTask() {
//                            public void run() {
//                                Intent i = new Intent();
//                                i.setClass(MainActivity.this, MainActivity.class);
//                                startActivity(i);
//                                finish();
//                                dlg.dismiss();
//                                t.cancel();
//                            }
//                        }, 3500);
                        //hold for 3.5 seconds

                        //link to difficulty page
                        moveDifficulty(v);
                    }

                    System.out.println(newWord);
                    writeTxt wt = new writeTxt();
                    try {
                        wt.writeTxtFile(newWord);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        moveCustomGame(v, newWord.length(), newWord.toUpperCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            builder.show();


//        newWord = String.valueOf(inputServer.getText());
//        System.out.println(newWord);
        //System.out.println();

    }

    //check if input word is valid(4-6 words) or not
    public boolean isValid(String inputWord) {
        boolean flag = false;
        if(inputWord.length() == 4 || inputWord.length() == 5 || inputWord.length() == 6) {
            flag = true;
        }
        return flag;
    }

    //move from input popup window to the specific game page
    public void moveCustomGame(View v, int wordLength, String newWord) throws Exception {
        setContentView(R.layout.game);
        this.result1 = 0; //resets result to 0
        this.guess = ""; // resets guess to 0
        if(wordLength == 5){

            //system.out.println(word);
            this.m = new Wordle(newWord);
            this.difficulty = 5;
            change2Normal(v);
        } else if(wordLength == 4){

            this.m = new Wordle(newWord);
            this.difficulty = 4;
            change2Easy(v);
        } else if(wordLength == 6){

            this.m = new Wordle(newWord);
            this.difficulty = 6;
            change2Hard(v);
        }
    }

    public void initTime(){
        theTime.startTimer();
        String empty = theTime.getTimer();
        theTime.startTimer();
    }

    public void loadTxt() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            txt = sb.toString();
            String[] values = txt.split(",");
            this.wins = Integer.parseInt(values[1]);
            this.losses = Integer.parseInt(values[3]);
            this.matches = Integer.parseInt(values[5]);
            this.winRate = Float.parseFloat(values[7]);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)

    public void resetStats(View v) {
        this.wins = 0; // find a way to store these locally
        this.matches = 0;
        this.losses = 0;
        this.winRate = 0.0F;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveAsTxt() {
        String[] values = txt.split(",");
        values[1] = String.valueOf(this.wins);
        values[3] = String.valueOf(this.losses);
        values[5] = String.valueOf(this.matches);
        values[7] = String.valueOf(this.winRate);
        this.txt = String.join(",", values);


        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(this.txt.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}