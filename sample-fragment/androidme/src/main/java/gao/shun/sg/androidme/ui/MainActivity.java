package gao.shun.sg.androidme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import gao.shun.sg.androidme.R;
import gao.shun.sg.androidme.data.AndroidImageAssets;

/**
 * Created by Theodore on 2017/10/22.
 */

public class MainActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String HEAD_INDEX = "HEAD_INDEX";
    private static final String BODY_INDEX = "BODY_INDEX";
    private static final String LEG_INDEX = "LEG_INDEX";

    private boolean isTwoPanel;
    private int headIndex;
    private int bodyIndex;
    private int legIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            headIndex = savedInstanceState.getInt(HEAD_INDEX);
            bodyIndex = savedInstanceState.getInt(BODY_INDEX);
            legIndex = savedInstanceState.getInt(LEG_INDEX);
        }

        final Button button = (Button) findViewById(R.id.next_button);

        isTwoPanel = findViewById(R.id.android_me_linear_layout) != null;

        if (isTwoPanel) {
            Log.v(TAG, "2 panel");
            button.setVisibility(View.GONE);

            BodyPartFragment head = new BodyPartFragment();
            head.setImageIds(AndroidImageAssets.getHeads());
            head.setIndex(headIndex);
            getSupportFragmentManager().beginTransaction().add(R.id.head_container, head).commit();

            BodyPartFragment body = new BodyPartFragment();
            body.setImageIds(AndroidImageAssets.getBodies());
            body.setIndex(bodyIndex);
            getSupportFragmentManager().beginTransaction().add(R.id.body_container, body).commit();

            BodyPartFragment leg = new BodyPartFragment();
            leg.setImageIds(AndroidImageAssets.getLegs());
            leg.setIndex(legIndex);
            getSupportFragmentManager().beginTransaction().add(R.id.leg_container, leg).commit();
        }
        else {
            Log.v(TAG, "not 2 panel");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(AndroidMe.HEAD_INDEX, headIndex);
                    bundle.putInt(AndroidMe.BODY_INDEX, bodyIndex);
                    bundle.putInt(AndroidMe.LEG_INDEX, legIndex);
                    Intent intent = new Intent(MainActivity.this, AndroidMe.class);
                    intent.putExtras(bundle);
                    Log.v(TAG, "onClick() " + headIndex + ", " + bodyIndex + ", " + legIndex);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onImageSelected(int position) {

        Log.v(TAG, "onImageSelected() " + position);

        int bodyPartNumber = position / 12;
        int index = position - bodyPartNumber * 12;

        if (isTwoPanel) {
            switch (bodyPartNumber) {
                case 0:
                    headIndex = index;
                    BodyPartFragment head = new BodyPartFragment();
                    head.setImageIds(AndroidImageAssets.getHeads());
                    head.setIndex(headIndex);
                    getSupportFragmentManager().beginTransaction().replace(R.id.head_container, head).commit();
                    break;

                case 1:
                    bodyIndex = index;
                    BodyPartFragment body = new BodyPartFragment();
                    body.setImageIds(AndroidImageAssets.getBodies());
                    body.setIndex(bodyIndex);
                    getSupportFragmentManager().beginTransaction().replace(R.id.body_container, body).commit();
                    break;

                case 2:
                    legIndex = index;
                    BodyPartFragment leg = new BodyPartFragment();
                    leg.setImageIds(AndroidImageAssets.getLegs());
                    leg.setIndex(legIndex);
                    getSupportFragmentManager().beginTransaction().replace(R.id.leg_container, leg).commit();
                    break;

                default:
                    break;
            }
        }
        else {
            switch (bodyPartNumber) {
                case 0:
                    headIndex = index;
                    break;

                case 1:
                    bodyIndex = index;
                    break;

                case 2:
                    legIndex = index;
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(HEAD_INDEX, headIndex);
        outState.putInt(BODY_INDEX, bodyIndex);
        outState.putInt(LEG_INDEX, legIndex);
    }
}
