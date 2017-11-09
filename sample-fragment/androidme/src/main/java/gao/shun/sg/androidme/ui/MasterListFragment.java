package gao.shun.sg.androidme.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import gao.shun.sg.androidme.R;
import gao.shun.sg.androidme.data.AndroidImageAssets;

/**
 * Created by Theodore on 2017/10/22.
 */

public class MasterListFragment extends Fragment {

    private OnImageClickListener onImageClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onImageClickListener = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frg_master_list, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.images_grid_view);
        gridView.setAdapter(new MasterListAdapter(inflater.getContext(), AndroidImageAssets.getAll()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onImageClickListener != null) onImageClickListener.onImageSelected(position);
            }
        });

        return rootView;
    }

    public interface OnImageClickListener {
        void onImageSelected(int position);
    }
}
