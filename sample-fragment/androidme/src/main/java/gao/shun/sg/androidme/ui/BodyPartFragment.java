package gao.shun.sg.androidme.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import gao.shun.sg.androidme.R;

/**
 * Created by Theodore on 2017/10/22.
 */

public class BodyPartFragment extends Fragment {

    private static final String IMAGE_IDS = "IMAGE_IDS";
    private static final String INDEX = "INDEX";

    private List<Integer> imageIds;
    private int index;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            imageIds = savedInstanceState.getIntegerArrayList(IMAGE_IDS);
            index = savedInstanceState.getInt(INDEX);
        }

        View rootView = inflater.inflate(R.layout.frg_body_part, container, false);

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.body_part_image_view);
        if (imageIds != null) {
            imageView.setImageResource(imageIds.get(index));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = ++index % imageIds.size();
                    imageView.setImageResource(imageIds.get(index));
                }
            });
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(IMAGE_IDS, (ArrayList<Integer>) imageIds);
        outState.putInt(INDEX, index);
    }

    public void setImageIds(List<Integer> imageIds) {
        this.imageIds = imageIds;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
