package com.example.moveonotes.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moveonotes.Model.Note;
import com.example.moveonotes.R;
import com.example.moveonotes.Utils.ConverterImage;
import com.example.moveonotes.Utils.GlobalApplicationContext;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    //Variables
    private OnItemClickListener listener;
    private String searchedText;

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getKey() == newItem.getKey();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getNoteTitle().equals(newItem.getNoteTitle()) &&
                    oldItem.getTimeStampCreated() == newItem.getTimeStampCreated() &&
                    oldItem.getNoteBody().equals(newItem.getNoteBody());
        }
    };

    //Constructor
    public NoteAdapter() {
        super(DIFF_CALLBACK);
        this.searchedText="";
    }


     //Adapter Methods
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(GlobalApplicationContext.getContext()).inflate(R.layout.item_view, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        String formatedTime = DateFormat.getDateTimeInstance().format(note.getTimeStampCreated());
        holder.time.setText(formatedTime);
        holder.image = note.getImage();
        String titleHtmlText = note.getNoteTitle().replace(searchedText, "<font color='#FF1449'>" + searchedText + "</font>");
        String bodyHtmlText = note.getNoteBody().replace(searchedText, "<font color='#FF1449'>" + searchedText + "</font>");
        holder.title.setText(Html.fromHtml(titleHtmlText));
        holder.body.setText(Html.fromHtml(bodyHtmlText));
        if(holder.image.length()<2){
            holder.cameraImage.setVisibility(View.INVISIBLE);
            holder.arrowImage.setVisibility(View.INVISIBLE);
        }
//        if (note.getImagePostRAW() != null) {
//            Bitmap bitmap = ConverterImage.getBitmapImage(note.getImagePostRAW());
//            Glide.with(GlobalApplicationContext.getContext()).load(bitmap).into(holder.);
//        } else {
//            holder.imageViewPost.setImageBitmap(null);
//            repositoryApp.downloadImage(post.getKey(), StorageFolder.FEED, new DownloadImageCallback() {
//                @Override
//                public void onImageDownloaded(File file) {
//                    if (file != null) {
//                        Glide.with(context).load(file).into(holder.imageViewPost);
//
//                    } else {
//                        Glide.with(context).clear(holder.imageViewPost);
//                    }
//                }
//            });
//        }
    }

    public void setSearchText(String searchText) {
        this.searchedText = searchText;
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    //Row Item Class
    public class NoteViewHolder extends RecyclerView.ViewHolder {

        //Variables
        private TextView title;
        private TextView body;
        private TextView time;
        private String image;
        private ImageView cameraImage;
        private ImageView arrowImage;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title_text);
            body = itemView.findViewById(R.id.item_body_text);
            time = itemView.findViewById(R.id.item_time_text);
            cameraImage = itemView.findViewById(R.id.item_image_btn);
            arrowImage = itemView.findViewById(R.id.item_arrow_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClick(getItem(position));
                }
            });
        }
    }

    //On Click Handlers
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
