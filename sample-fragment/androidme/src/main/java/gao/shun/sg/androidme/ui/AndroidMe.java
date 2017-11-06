package gao.shun.sg.androidme.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import gao.shun.sg.androidme.R;
import gao.shun.sg.androidme.data.AndroidImageAssets;

public class AndroidMe extends AppCompatActivity {

    public static final String HEAD_INDEX = "HEAD_INDEX";
    public static final String BODY_INDEX = "BODY_INDEX";
    public static final String LEG_INDEX = "LEG_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_me);

        Bundle bundle = getIntent().getExtras();

        BodyPartFragment head = new BodyPartFragment();
        head.setImageIds(AndroidImageAssets.getHeads());
        head.setIndex(bundle.getInt(HEAD_INDEX));
        getSupportFragmentManager().beginTransaction().add(R.id.head_container, head).commit();

        BodyPartFragment body = new BodyPartFragment();
        body.setImageIds(AndroidImageAssets.getBodies());
        body.setIndex(bundle.getInt(BODY_INDEX));
        getSupportFragmentManager().beginTransaction().add(R.id.body_container, body).commit();

        BodyPartFragment leg = new BodyPartFragment();
        leg.setImageIds(AndroidImageAssets.getLegs());
        leg.setIndex(bundle.getInt(LEG_INDEX));
        getSupportFragmentManager().beginTransaction().add(R.id.leg_container, leg).commit();
    }
}
