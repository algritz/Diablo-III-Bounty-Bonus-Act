package ca.nosrecettes.d3bba.diabloiiibountybonusact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity {
    private String[] bountyArray = {"5, 2, 3, 1, 4", "2, 3, 1, 4, 5", "3, 1, 4, 2, 5", "1, 4, 2, 5, 3", "4, 2, 1, 5, 3", "2, 1, 5, 3, 4", "1, 5, 3, 4, 2", "5, 3, 4, 1, 2", "3, 4, 1, 2, 5", "4, 1, 3, 2, 5", "1, 3, 2, 5, 4", "3, 2, 5, 4, 1", "2, 5, 4, 3, 1", "5, 4, 3, 1, 2", "4, 3, 5, 1, 2", "3, 5, 1, 2, 4", "5, 1, 2, 4, 3", "1, 2, 4, 5, 3", "2, 4, 5, 3, 1", "4, 5, 2, 3, 1"};
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        current_cycle = "Current Cycle | until       " + ending_format.format(timeStamp) + " - " + bountyArray[(int) current_cycle_index];
        next_cycle = "Next Cycle      | Starting " + starting_format.format((timeStamp + 3600000)) + " - " + bountyArray[(int) next_cycle_index];

        currentCycleTextView.setText(current_cycle);
        nextCycleTextView.setText(next_cycle);

        for (int i = current_cycle_index, j = current_cycle_index + 3, k = current_cycle_index + 2; i < 19; ++i, j++, k++) {
            if (k == 20) {
                k = 0;
            }
            if (j == 21) {
                j = 1;
            }
            remaining_cycle.add("Cycle # " + String.format("%02d", j) + " - " + full_starting_format.format((timeStamp + (3600000 * (i)))) + " - " + bountyArray[k]);
        }

        for (int i = 0, j = current_cycle_index, k = current_cycle_index - 3, l = current_cycle_index - 1; i <= current_cycle_index; i++, j++, k++, l++) {
            remaining_cycle.add("Cycle # " + String.format("%02d", j) + " -- " + full_starting_format.format((timeStamp + (3600000 * (k + 20)))) + " - " + bountyArray[l]);
        }

        bountyListView = (ListView) findViewById(R.id.bounty_list);
        // this-The current activity context.
        // Second param is the resource Id for list layout row item
        // Third param is input array
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, remaining_cycle);
        bountyListView.setAdapter(arrayAdapter);

    }
}
