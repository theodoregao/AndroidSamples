package sg.shun.gao.libs.android.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import sg.shun.gao.libs.android.common.R;

/**
 * Created by shun on 11/21/15.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private WeakReference<Context> context;
    private List<File> files;
    private OnItemClickListener onItemClickListener;

    public FileAdapter(final Context context) {
        this.context = new WeakReference<>(context);
        this.files = new ArrayList<>();
    }

    public void add(File file) {
        files.add(file);
        notifyItemInserted(files.size() - 1);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.get()).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        File file = files.get(position);
        holder.fileName.setText(file.getName());
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, File file);
    }

    public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView fileName;

        public FileViewHolder(View itemView) {
            super(itemView);

            fileName = (TextView) itemView.findViewById(R.id.fileName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, files.get(getAdapterPosition()));
        }
    }
}
