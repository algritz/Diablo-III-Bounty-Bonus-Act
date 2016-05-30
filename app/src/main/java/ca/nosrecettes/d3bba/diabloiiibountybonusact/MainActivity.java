package ca.nosrecettes.d3bba.diabloiiibountybonusact;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private String[] bountyArray = {"5, 2, 3, 1, 4", "2, 3, 1, 4, 5", "3, 1, 4, 2, 5", "1, 4, 2, 5, 3", "4, 2, 1, 5, 3", "2, 1, 5, 3, 4", "1, 5, 3, 4, 2", "5, 3, 4, 1, 2", "3, 4, 1, 2, 5", "4, 1, 3, 2, 5", "1, 3, 2, 5, 4", "3, 2, 5, 4, 1", "2, 5, 4, 3, 1", "5, 4, 3, 1, 2", "4, 3, 5, 1, 2", "3, 5, 1, 2, 4", "5, 1, 2, 4, 3", "1, 2, 4, 5, 3", "2, 4, 5, 3, 1", "4, 5, 2, 3, 1"
    };
    private String current_cycle;
    private String next_cycle;

    List<String> remaining_cycle = new ArrayList<String>(bountyArray.length);

    private ListView bountyListView;
    private ArrayAdapter arrayAdapter;

    private TextView currentCycleTextView;
    private TextView nextCycleTextView;


    private int current_cycle_index;
    private int next_cycle_index;
    private int remaining_cycle_index;

    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh_data();
        startTimer();
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();

        Calendar currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long currentTimeInMillis = currentCalendar.getTimeInMillis();
        int hr = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int min = currentCalendar.get(Calendar.MINUTE);
        int sec = currentCalendar.get(Calendar.SECOND);
        int millis = currentCalendar.get(Calendar.MILLISECOND);

        long millisUntilNextHour = (min * 60 * 1000 + sec * 1000 + millis + 299999) / 300000 * 300000 - (min * 60 * 1000 + sec * 1000 + millis);

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, millisUntilNextHour, 36000000);
        //timer.schedule(timerTask, 5000, 5000); // debug version, runs every 5 seconds
    }


    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        refresh_data();
                    }
                });
            }
        };
    }


    public void refresh_data() {
        setContentView(R.layout.activity_main);

        currentCycleTextView = (TextView) findViewById(R.id.current_cycle);
        nextCycleTextView = (TextView) findViewById(R.id.next_cycle);

        long timeStamp = System.currentTimeMillis();

        int current_hour = (int) (timeStamp / 3600000);

        int offset = 6;

        current_cycle_index = ((current_hour + offset) % 20);

        next_cycle_index = (current_hour + offset + 1) % 20;

        remaining_cycle_index = (current_hour + offset + 2) % 20;

        SimpleDateFormat starting_format = new SimpleDateFormat(" HH.00:00");
        SimpleDateFormat ending_format = new SimpleDateFormat(" HH.59:59");

        SimpleDateFormat full_starting_format = new SimpleDateFormat("MMM/dd/yy HH.00:00");

        current_cycle = "Current Cycle | until        " + ending_format.format(timeStamp) + " - " + bountyArray[(int) current_cycle_index];
        next_cycle = "Next Cycle      | Starting " + starting_format.format((timeStamp + 3600000)) + " - " + bountyArray[(int) next_cycle_index];

        currentCycleTextView.setText(current_cycle);
        nextCycleTextView.setText(next_cycle);
        for (int i = (current_hour + offset) % 20, j = 2; i < 100; ++i, ++j) {
            int index = (current_hour + offset + i) % 20;
            int cycle_num = (index % 20);
            if (cycle_num == 0) {
                cycle_num = 20;
            }
            int index_remaining = (current_hour + offset + j) % 20;
            int cycle_remaining = (index_remaining % 20) + 1;
            /* if (cycle_remaining == 0) {
                cycle_remaining = 20;
            } */
            remaining_cycle.add("Cycle # " + String.format("%02d", cycle_remaining) + " - " + full_starting_format.format((timeStamp + (3600000 * (j)))) + " - " + bountyArray[index_remaining]);
        }

        bountyListView = (ListView) findViewById(R.id.bounty_list);
        // this-The current activity context.
        // Second param is the resource Id for list layout row item
        // Third param is input array
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, remaining_cycle);
        bountyListView.setAdapter(arrayAdapter);
        bountyListView.invalidateViews();
    }

}
