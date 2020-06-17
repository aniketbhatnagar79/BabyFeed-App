package com.example.babyfeed.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.babyfeed.DATA.DataBaseHandler;
import com.example.babyfeed.Model.Item;
import com.example.babyfeed.R;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context=context;
        this.itemList=itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Item item=itemList.get(position);
        holder.itemName.setText(MessageFormat.format("Item:{0}", item.getItemName()));
        holder.itemSize.setText(MessageFormat.format("Item Size:{0}", String.valueOf(item.getItemSize())));
        holder.itemQuantity.setText(MessageFormat.format("Item Quantity:{0}", String.valueOf(item.getItemQuantity())));
        holder.itemColor.setText(MessageFormat.format("Item Color:{0}", item.getItemColor()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView itemSize;
        public TextView itemColor;
        public TextView itemQuantity;
        public Button editButton;
        public Button deleteButton;

        public int id;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            itemName=itemView.findViewById(R.id.item_name);
            itemColor=itemView.findViewById(R.id.item_color);
            itemSize=itemView.findViewById(R.id.item_size);
            itemQuantity=itemView.findViewById(R.id.item_quantity);
            editButton=itemView.findViewById(R.id.edit_button);
            deleteButton=itemView.findViewById(R.id.delete_button);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position;
            position=getAdapterPosition();
            Item item=itemList.get(position);
            switch(v.getId()){
                case R.id.edit_button:

                    editItem(item);


                    break;
                case R.id.delete_button:

                    deleteItem(item.getId());
                    break;
            }
        }

        private void editItem(final Item newItem) {
          //  final Item item=itemList.get(getAdapterPosition());

             Button saveButton;
             final EditText babyItem;
             final EditText itemQuantity;
             final EditText itemColor;
             final EditText itemSize;
          //  TextView title;
            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.popup,null);
            babyItem=view.findViewById(R.id.item);
            itemColor=view.findViewById(R.id.color);
            itemQuantity=view.findViewById(R.id.quantity);
            itemSize=view.findViewById(R.id.size);
            saveButton=view.findViewById(R.id.saveButton);
            saveButton.setText("Update");
//            title=view.findViewById(R.id.title);
//            title.setText("Edit Item");
            babyItem.setText(newItem.getItemName());
            itemQuantity.setText(String.valueOf(newItem.getItemQuantity()));
            itemColor.setText(newItem.getItemColor());
            itemSize.setText(String.valueOf(newItem.getItemSize()));
            builder.setView(view);
            dialog=builder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        DataBaseHandler dataBaseHandler=new DataBaseHandler(context);
                       newItem.setItemName(babyItem.getText().toString());
                    Log.d("new",newItem.getItemName());
                  //  int ip=newItem.getId();
                       newItem.setItemColor(itemColor.getText().toString());
                       newItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                       newItem.setItemSize(Integer.parseInt(itemSize.getText().toString()));

                       if(!babyItem.getText().toString().isEmpty() && !itemColor.getText().toString().isEmpty()
                       && !itemQuantity.getText().toString().isEmpty() && !itemSize.getText().toString().isEmpty()
                       ){
                           dataBaseHandler.updateItem(newItem);
                           notifyItemChanged(getAdapterPosition(),newItem);
                         //  Log.d("new1",dataBaseHandler.getItem(ip).toString());

                       }else{
                           Snackbar.make(v,"Fiels are Empty",Snackbar.LENGTH_SHORT).show();

                       }
                       dialog.dismiss();

                }
            });
        }

        private void deleteItem(final int id) {
            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.confo_pop,null);
            Button noButton=view.findViewById(R.id.con_no_button);
            Button yesButton=view.findViewById(R.id.con_yes_button);
            builder.setView(view);
            dialog=builder.create();
            dialog.show();
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseHandler db=new DataBaseHandler(context);
                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });




        }
    }
}
