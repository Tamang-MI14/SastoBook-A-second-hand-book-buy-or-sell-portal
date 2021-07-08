package com.buyorsell.sastobook;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.buyorsell.sastobook.model.bookModel;

import java.util.ArrayList;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageList;
    TextView BookName, BookCategory, BookPrice;
    Button update, delete;
    View v;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        Context context;
        ArrayList<bookModel> dataUserArrayList;

        imageList = itemView.findViewById(R.id.imageList);
        BookName = itemView.findViewById(R.id.BookName);
        BookCategory = itemView.findViewById(R.id.BookCategory);
        BookPrice = itemView.findViewById(R.id.BookPrice);
        update = itemView.findViewById(R.id.updateBook);
        delete = itemView.findViewById(R.id.deleteBook);
        v=itemView;


    }
}
