package com.example.filemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileHolder> {
    private List<File> files;
    private onFileClickListener listener;
    public interface onFileClickListener {
        void onFileClick(View view, int position);
    }


    public FileAdapter(File[] files) {
        this.files = Arrays.asList(files);
    }

    public void setOnFileClickListener(FileAdapter.onFileClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        FileHolder holder = new FileHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, final int position) {
        File file = files.get(position);
        if (file.isFile()) {
            holder.icon.setImageResource(R.drawable.file_text);
        }
        else if (file.isDirectory()){
            holder.icon.setImageResource(R.drawable.file_folder);
        }
        else {
            holder.icon.setImageResource(R.mipmap.ic_launcher);
        }
        holder.name.setText(file.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onFileClick(view, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class FileHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        LinearLayout layout;
        public FileHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.file_icon);
            name = itemView.findViewById(R.id.file_name);
            layout = itemView.findViewById(R.id.whole_file_item);

        }
    }

}
