package com.example.onsiteone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onsiteone.clock.TimerView;

public class MainActivity2 extends AppCompatActivity {

    private TimerView timerView;
    private int seconds = 0 ;
    private Button play,stop;
    private boolean running = false;
    private TextView hoursText,minutesText,secText;
    private ImageView resetimg,playimg;
    private int hours,minutes,sec;
    private MediaPlayer mediaPlayer1,mediaPlayer2,mediaPlayer3;
    private boolean started;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView ( R.layout.activity_main2 );

        mediaPlayer1 = new MediaPlayer ().create(getApplicationContext (),R.raw.negative);
        mediaPlayer2 = new MediaPlayer ().create(getApplicationContext (),R.raw.rubberone);
        mediaPlayer3 = new MediaPlayer ().create(getApplicationContext (),R.raw.tick);

        timerView = findViewById ( R.id.time_view );
        play = findViewById ( R.id.play );
        stop = findViewById ( R.id.reset );
        hoursText = findViewById ( R.id.hours );
        minutesText = findViewById ( R.id.minutes );
        secText = findViewById ( R.id.seconds );
        resetimg = findViewById ( R.id.reset_img );
        playimg = findViewById ( R.id.play_img );
        started = false;


        if (savedInstanceState != null){
            sec = savedInstanceState.getInt ( "seconds" );
            minutes = savedInstanceState.getInt ( "minutes" );
            hours = savedInstanceState.getInt ( "hours" );
        }

        timeSetter ();

        timeHandler ();

        View.OnClickListener onClickListener = new View.OnClickListener () {
            @Override
            public void onClick( View v ) {
                mediaPlayer2.start ();
                switch (v.getId ()){
                    case R.id.play:
                        if (play.getText ().toString ().equals ("play")){
                            play.setText ( "stop" );
                            playimg.setImageResource ( R.drawable.pause );
                            running = true;
                            started = true;
                            break;
                        }
                        else {
                            playimg.setImageResource ( R.drawable.play );
                            running = false;
                            play.setText ( "play" );
                            break;
                        }
                    case R.id.reset:
                        playimg.setImageResource ( R.drawable.play );
                        play.setText ( "play" );
                        seconds = 0;
                        timerView.invalidate ();
                        running = false;
                        break;
                }
            }
        };

        play.setOnClickListener ( onClickListener );
        stop.setOnClickListener ( onClickListener );

    }

    @Override
    public void onSaveInstanceState( @NonNull Bundle outState , @NonNull PersistableBundle outPersistentState ) {
        outState.putInt ( "seconds",sec );
        outState.putInt ( "minutes",minutes );
        outState.putInt ( "hours",hours );
        super.onSaveInstanceState ( outState , outPersistentState );
    }

    private void timeHandler(){

        HandlerThread thread = new HandlerThread("MyHandlerThread");
        thread.start();
        final Handler handler = new Handler(thread.getLooper());

        handler.post(new Runnable() {
            @Override

            public void run()
            {
                hours = seconds/3600;
                minutes = (seconds % 3600) / 60;
                sec = seconds % 60;
                timerView.setMin ( minutes );
                timerView.setSec ( sec );

                timeSetter ();

                if (running) {
                    seconds++;
                    mediaPlayer3.start ();
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause ();
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume ();
        if (play.getText ().toString () != "play" && started ) {
            running = true;
        }
    }

    private void timeSetter (){
        if (sec <60){
            secText.setText ( String.valueOf ( sec ) );
        }
        else {
            secText.setText ( String.valueOf ( sec%60 ) );
        }
        if (minutes <60){
            minutesText.setText ( String.valueOf ( minutes ) );
        }
        else {
            minutesText.setText ( String.valueOf ( minutes%60 ) );
        }
        if (sec <24){
            hoursText.setText ( String.valueOf ( hours ) );
        }
        else {
            hoursText.setText ( String.valueOf ( hours%24 ) );
        }
    }

    @Override
    public void onBackPressed() {
        exit ();
    }

    private void exit(){
        running = false;
        mediaPlayer1.start ();
        final AlertDialog.Builder builder = new AlertDialog.Builder ( MainActivity2.this,R.style.CustomDialog );
        View view = LayoutInflater.from ( MainActivity2.this ).inflate ( R.layout.dialog,null,false );
        Button yes = view.findViewById ( R.id.yes );
        Button no = view.findViewById ( R.id.no );
        TextView firstText = view.findViewById ( R.id.firstText );
        TextView secondText = view.findViewById ( R.id.secontText );
        ImageView dialogImage = view.findViewById ( R.id.dialog_image );

        firstText.setText ( "Oh noooo !" );
        secondText.setText ( "Wanna move out?" );
        dialogImage.setImageResource ( R.drawable.oh );
        final AlertDialog alertDialog = builder.create ();
        alertDialog.setView ( view );

        no.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View v ) {
                full ();
                alertDialog.cancel ();
                running = true;
                mediaPlayer2.start ();
            }
        } );

        yes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick( View v ) {
                finish ();
                mediaPlayer2.start ();
            }
        } );
        alertDialog.setCanceledOnTouchOutside ( false );
        alertDialog.show ();
    }

    private void full(){
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}