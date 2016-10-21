package top.genylife.one;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import top.genylife.one.widget.ArcProgress;

public class MainActivity extends AppCompatActivity
{

    ArcProgress progress;
    Button mButton;
    int index = 0;
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            int sec = Calendar.getInstance().get(Calendar.SECOND);
            progress.setProgress(sec + 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (ArcProgress) findViewById(R.id.progress);
        mButton = (Button) findViewById(R.id.btn);

        progress.setMaxProgress(60);
        List<String> texts = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            texts.add(String.valueOf(i));
        }
        progress.setDialTexts(texts);
        progress.setDialPointNum(5);
//        TimerTask timerTask = new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                mHandler.sendEmptyMessage(0);
//            }
//        };
//        Timer timer = new Timer(true);
//        timer.schedule(timerTask, 1000, 1000);

        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progress.setDialProgress(index);
                index++;
            }
        });
    }
}
