package com.example.babyfeed;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.babyfeed.DATA.DataBaseHandler;
import com.example.babyfeed.Model.Item;
import com.example.babyfeed.UI.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class listActivity extends AppCompatActivity {
        private RecyclerView recyclerView;
        private RecyclerViewAdapter recyclerViewAdapter;
        private List<Item> itemList;
        private DataBaseHandler dataBaseHandler;
        private FloatingActionButton fab;
        private AlertDialog.Builder builder;
        private AlertDialog alertDialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        dataBaseHandler=new DataBaseHandler(this);
        recyclerView=findViewById(R.id.recyclerView);
        fab=findViewById(R.id.fab);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList=new ArrayList<>();
        itemList=dataBaseHandler.getAllItems();
        for(Item item:itemList){
            Log.d("ReList",item.getItemName());
        }
        recyclerViewAdapter=new RecyclerViewAdapter(this,itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopDialog();

            }
        });
    }

    private void createPopDialog() {
        builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();
        babyItem=view.findViewById(R.id.item);
        itemColor=view.findViewById(R.id.color);
        itemQuantity=view.findViewById(R.id.quantity);
        itemSize=view.findViewById(R.id.size);
        saveButton=view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!babyItem.getText().toString().isEmpty() && !itemColor.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty() && !itemSize.getText().toString().isEmpty())
                    saveItem(v);
                else{
                    Snackbar.make(v,"Empty Fields are not allowed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveItem(View v) {
        Item item=new Item();
        Log.d("DB2","item Added");
        String newItem=babyItem.getText().toString().trim();
        String newColor=itemColor.getText().toString();
        int newQuantity= Integer.parseInt(itemQuantity.getText().toString().trim());
        int newSize= Integer.parseInt(itemSize.getText().toString().trim());
        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemSize(newSize);
        item.setItemQuantity(newQuantity);
        dataBaseHandler.addItem(item);
        Snackbar.make(v,"Item Saved",Snackbar.LENGTH_SHORT).show();
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                startActivity(new Intent(listActivity.this,listActivity.class));
                finish();
            }
        },1300);
    }
}
