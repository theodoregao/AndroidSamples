package gao.shun.sg.prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ToolAboutFragment extends Fragment {
    private static final String ARG_TOOL_TYPE = "toolType";

    private ToolType mToolType;

    public ToolAboutFragment() {

    }

    public static ToolAboutFragment newInstance(ToolType toolType) {
        final ToolAboutFragment fragment = new ToolAboutFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_TOOL_TYPE, toolType.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args == null)
            throw new IllegalStateException("No arguments set; use newInstance when constructing!");
        mToolType = ToolType.valueOf(args.getString(ARG_TOOL_TYPE));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tool_about, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.title);
        textView.setText(mToolType.getToolNameResourceId());
        textView = (TextView) rootView.findViewById(R.id.description);
        textView.setText(mToolType.getToolDescriptionResourceId());
        return rootView;
    }
}
