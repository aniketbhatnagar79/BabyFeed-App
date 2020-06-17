package com.example.babyfeed;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.babyfeed.DATA.DataBaseHandler;
import com.example.babyfeed.Model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private DataBaseHandler dataBaseHandler;

    private void createPopup() {
        builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);
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
        builder.setView(view);
        dialog=builder.create();
        dialog.show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataBaseHandler=new DataBaseHandler(this);
        byPassActivity();

        List<Item> items=dataBaseHandler.getAllItems();

        for(Item im:items)
            Log.d("Oncreate",im.getItemName());
        for(Item im:items)
            Log.d("Oncreate",im.getItemColor());
        for(Item im:items)
            Log.d("Oncreate",im.getDateItemAdded());
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void byPassActivity() {
        if(dataBaseHandler.getItemCount()>0){
            startActivity(new Intent(MainActivity.this,listActivity.class));
            finish();
        }
    }

    private void saveItem(View view){
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
        Snackbar.make(view,"Item Saved",Snackbar.LENGTH_SHORT).show();
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,listActivity.class));
            }
        },1300);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
