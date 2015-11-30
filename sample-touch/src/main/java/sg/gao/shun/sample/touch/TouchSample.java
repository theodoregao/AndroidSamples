package sg.gao.shun.sample.touch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TouchSample extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listViewActivity;
    Map<String, Class<?>> activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_sample);

        activities = new HashMap<>();
        activities.put(OnTouchEvent.class.getSimpleName(), OnTouchEvent.class);
        activities.put(GestureDetector.class.getSimpleName(), GestureDetector.class);
        activities.put(VelocityTracker.class.getSimpleName(), VelocityTracker.class);
        activities.put(OverScroll.class.getSimpleName(), OverScroll.class);

        listViewActivity = (ListView) findViewById(R.id.listViewActivity);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, setToArray(activities.keySet()));
        listViewActivity.setAdapter(arrayAdapter);
        listViewActivity.setOnItemClickListener(this);
    }

    private String[] setToArray(Set<String> set) {
        String[] strings = new String[set.size()];
        int sz = 0;
        for (String string : set) strings[sz++] = string;
        return strings;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, activities.get(setToArray(activities.keySet())[position])));
    }
}
