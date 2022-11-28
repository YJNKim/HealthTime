package com.yj.hhhhhhtttttttt;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    LinearLayout timeCountSettingLV, timeCountLV;
    EditText hourET, minuteET, secondET;
    TextView hourTV, minuteTV, secondTV, finishTV, setNum;
    Button startBtn, resetBtn;
    int hour, minute, second;
    private int count = 0;
    private SoundPool soundPool;
    private int sound1, sound2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeCountSettingLV = (LinearLayout)findViewById(R.id.timeCountSettingLV);
        timeCountLV = (LinearLayout)findViewById(R.id.timeCountLV);

        hourET = (EditText)findViewById(R.id.hourET);
        minuteET = (EditText)findViewById(R.id.minuteET);
        secondET = (EditText)findViewById(R.id.secondET);

        hourTV = (TextView)findViewById(R.id.hourTV);
        minuteTV = (TextView)findViewById(R.id.minuteTV);
        secondTV = (TextView)findViewById(R.id.secondTV);
        setNum = (TextView)findViewById(R.id.setNum);

        startBtn = (Button)findViewById(R.id.startBtn);
        resetBtn = (Button)findViewById(R.id.resetBtn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        }

        sound1 = soundPool.load(this,R.raw.sound1,1);
        sound2 = soundPool.load(this,R.raw.sound2,1);

        // 시작버튼 이벤트 1처리
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeCountSettingLV.setVisibility(View.GONE);
                timeCountLV.setVisibility(View.VISIBLE);

                hourTV.setText(hourET.getText().toString());
                minuteTV.setText(minuteET.getText().toString());
                secondTV.setText(secondET.getText().toString());

                hour = Integer.parseInt(hourET.getText().toString());
                minute = Integer.parseInt(minuteET.getText().toString());
                second = Integer.parseInt(secondET.getText().toString());

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        // 반복실행할 구문

                        // 0초 이상이면
                        if(second != 0) {
                            //1초씩 감소
                            second--;

                            // 0분 이상이면
                        } else if(minute != 0) {
                            // 1분 = 60초
                            second = 60;
                            second--;
                            minute--;

                            // 0시간 이상이면
                        } else if(hour != 0) {
                            // 1시간 = 60분
                            second = 60;
                            minute = 60;
                            second--;
                            minute--;
                            hour--;
                        }

                        //시, 분, 초가 10이하(한자리수) 라면
                        // 숫자 앞에 0을 붙인다 ( 8 -> 08 )
                        if(second <= 9){
                            secondTV.setText("0" + second);
                        } else {
                            secondTV.setText(Integer.toString(second));
                        }

                        if(minute <= 9){
                            minuteTV.setText("0" + minute);
                        } else {
                            minuteTV.setText(Integer.toString(minute));
                        }

                        if(hour <= 9){
                            hourTV.setText("0" + hour);
                        } else {
                            hourTV.setText(Integer.toString(hour));
                        }

                        if(hour == 0 && minute == 0 && second == 15){
                            soundPool.play(sound2, 1, 1, 1, 0, 1);
                        }


                        if(hour == 0 && minute == 0 && second == 0) {
                            timer.cancel();//타이머 종료
                            count++;
                            setNum.setText(count + "Set");
                            soundPool.play(sound1, 1, 1, 1, 0, 1);
                        }
                    }
                };

                //타이머를 실행
                timer.schedule(timerTask, 0, 1000); //Timer 실행
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNum.setText(0 + "Set");
            }
        });
    }
}