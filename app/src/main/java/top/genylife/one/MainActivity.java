package top.genylife.one;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import top.genylife.one.widget.ArcProgress;

public class MainActivity extends AppCompatActivity
{

    ArcProgress progress;
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
        progress.setMaxProgress(60);
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                mHandler.sendEmptyMessage(0);
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(timerTask, 1000, 1000);

        //        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        //        {
        //            @Override
        //            public void onClick(View v)
        //            {
        //                mHandler.sendEmptyMessage(0);
        //            }
        //        });
    }
}
