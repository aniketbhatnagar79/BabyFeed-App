package com.example.babyfeed.DATA;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.babyfeed.Model.Item;
import com.example.babyfeed.Util.Constants;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DataBaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME,null,Constants.DB_VERSION);
        this.context=context;
        //onCreate();
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        String CREATE_BABY_TABLE="CREATE TABLE "+Constants.TABLE_NAME+"("+
                Constants.KEY_ID +" INTEGER PRIMARY KEY,"+Constants.KEY_ITEM+" TEXT,"+
                Constants.KEY_COLOR+" TEXT,"+Constants.KEY_QUANTITY+" INTEGER,"+
                Constants.KEY_SIZE+" INTEGER,"+Constants.KEY_DATE+" LONG );";
        db.execSQL(CREATE_BABY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
    onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addItem(Item item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constants.KEY_ITEM,item.getItemName());
        values.put(Constants.KEY_COLOR,item.getItemColor());
        Log.d("OnColor",item.getItemColor());
        values.put(Constants.KEY_QUANTITY,item.getItemQuantity());
        values.put(Constants.KEY_SIZE,item.getItemSize());
        values.put(Constants.KEY_DATE,java.lang.System.currentTimeMillis());
        db.insert(Constants.TABLE_NAME,null,values);
        Log.d("DB","item Added");


    }
    public Item getItem(int id){

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(Constants.TABLE_NAME,new String[]{
                Constants.KEY_ID,
                Constants.KEY_ITEM,
                Constants.KEY_COLOR,
                Constants.KEY_QUANTITY,
                Constants.KEY_SIZE,
                Constants.KEY_DATE},
                Constants.KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null,null

        );


        if(cursor!=null){
            cursor.moveToFirst();
        }
        Log.d("DB10", String.valueOf(cursor.getPosition()));

        Item item=new Item();
        if(cursor!=null) {
            //item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            item.setId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

            item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
            item.setItemQuantity(cursor.getInt(3));
            item.setItemSize(cursor.getInt(4));
            item.setItemColor(cursor.getString(2));

            DateFormat dateFormat=DateFormat.getDateInstance();
            String formattedDate=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE)))
                    .getTime()
                    );
            item.setDateItemAdded(formattedDate);

        }
        return item;

    }
        public List<Item> getAllItems(){
        SQLiteDatabase db=this.getReadableDatabase();
        List<Item> itemList=new ArrayList<>();
            Cursor cursor=db.query(Constants.TABLE_NAME,new String[]{
                            Constants.KEY_ID,
                            Constants.KEY_ITEM,
                    Constants.KEY_COLOR,
                            Constants.KEY_QUANTITY,
                            Constants.KEY_SIZE,
                            Constants.KEY_DATE},
                    null,
                   null,null,null,Constants.KEY_DATE+" DESC",null

            );
            Log.d("OnId", String.valueOf(cursor.getColumnIndex(Constants.KEY_ID)));
            Log.d("OnId1", String.valueOf(cursor.getColumnIndex(Constants.KEY_COLOR)));
            Log.d("OnId2", String.valueOf(cursor.getColumnIndex(Constants.KEY_SIZE)));
            Log.d("OnId3", String.valueOf(cursor.getColumnIndex(Constants.KEY_QUANTITY)));
            Log.d("OnId4", String.valueOf(cursor.getColumnIndex(Constants.KEY_ITEM)));
            Log.d("OnId5", String.valueOf(cursor.getColumnIndex(Constants.KEY_DATE)));
            if(cursor.moveToFirst()){
                do{
                    Item item=new Item();
                    //item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                    item.setId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                    item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_ITEM)));
                    item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QUANTITY)));
                    item.setItemSize(cursor.getInt(cursor.getColumnIndex(Constants.KEY_SIZE)));
                    item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)));

                    DateFormat dateFormat=DateFormat.getDateInstance();
                    String formattedDate=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE)))
                            .getTime()
                    );
                    item.setDateItemAdded(formattedDate);
                    itemList.add(item);
                }while (cursor.moveToNext());
            }
//            while (cursor.moveToNext()){
//                Item item=new Item();
//                //item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
//                item.setId(cursor.getInt(0));
//
//                item.setItemName(cursor.getString(1));
//                item.setItemQuantity(cursor.getInt(3));
//                item.setItemSize(cursor.getInt(4));
//                item.setItemColor(cursor.getString(2));
//
//                DateFormat dateFormat=DateFormat.getDateInstance();
//                String formattedDate=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE)))
//                        .getTime()
//                );
//                item.setDateItemAdded(formattedDate);
//                itemList.add(item);
//            }
            cursor.close();


       return itemList; }
       public int updateItem(Item item){
        SQLiteDatabase db=this.getWritableDatabase();
           ContentValues values=new ContentValues();
           values.put(Constants.KEY_ITEM,item.getItemName());
           values.put(Constants.KEY_COLOR,item.getItemColor());
           values.put(Constants.KEY_QUANTITY,item.getItemQuantity());
           values.put(Constants.KEY_SIZE,item.getItemSize());
           values.put(Constants.KEY_DATE,java.lang.System.currentTimeMillis());

           return db.update(Constants.TABLE_NAME,values,
                   Constants.KEY_ID+"=?",
                   new String[]{String.valueOf(item.getId())});
       }
       public void deleteItem(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID+"=?",new String[]{
                String.valueOf(id)
        });
        db.close();
       }
       public int getItemCount(){
        String c= "SELECT * FROM "+Constants.TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(c,null);
        return cursor.getCount();
       }



}
