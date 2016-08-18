package jeromq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sg.shun.gao.jeromq.server.R;

public class LogAdapter extends ArrayAdapter<LogItem> {

    private static final SimpleDateFormat FORMATER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private List<LogItem> mLogs;
    private Context mContext;

    public LogAdapter(Context context, int resource, List<LogItem> logs) {
        super(context, resource);

        mContext = context;
        mLogs = logs;
    }

    public void log(String message) {
        mLogs.add(0, new LogItem(message, "", true));
        notifyDataSetChanged();
    }

    public void clear() {
        mLogs.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLogs.size();
    }

    @Override
    public LogItem getItem(int position) {
        return mLogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            rowView = inflater.inflate(R.layout.item_log, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textTitle = (TextView) rowView.findViewById(R.id.textTitle);
            viewHolder.textTimeStamp = (TextView) rowView.findViewById(R.id.textTimeStamp);
            viewHolder.textDescription = (TextView) rowView.findViewById(R.id.textDescription);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.textTitle.setText(mLogs.get(position).getTitle());
        holder.textDescription.setText(mLogs.get(position).getDescription());
        if (position == mLogs.size() - 1 || mLogs.get(position).isShowTime()) {
            holder.textTimeStamp.setText(FORMATER.format(new Date(mLogs.get(position).getTimeStamp())));
        } else {
            holder.textTimeStamp.setText((mLogs.get(position).getTimeStamp() - mLogs.get(position + 1).getTimeStamp()) + " ms");
        }

        return rowView;
    }

    static class ViewHolder {
        public TextView textTitle;
        public TextView textTimeStamp;
        public TextView textDescription;
    }
}