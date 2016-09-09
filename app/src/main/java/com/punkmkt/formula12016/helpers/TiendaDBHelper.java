package com.punkmkt.formula12016.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import java.util.HashMap;

/**
 * Created by sebastianmendezgiron on 10/08/16.
 */

public class TiendaDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "f1app_tienda.db";
    public static final String ITEM_PRODUCTOS = "productos";
    public static final String ITEM_ID = "id";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_PRICE = "price";
    public static final String ITEM_QTY = "qty";

    private HashMap hashMap;

    public TiendaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ITEM_PRODUCTOS + " ( " +
                ITEM_ID + " INTEGER PRIMARY KEY, " +
                ITEM_NAME + " TEXT NOT NULL, " +
                ITEM_QTY + " INTEGER NOT NULL, " +
                ITEM_PRICE + " REAL NOT NULL );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ ITEM_PRODUCTOS);
        onCreate(db);
    }
    //name, quantity, price
    public boolean insertItem(String name, int qty, float price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_NAME, name);
        contentValues.put(ITEM_QTY, qty);
        contentValues.put(ITEM_PRICE, price);
        db.insert(ITEM_PRODUCTOS, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ITEM_PRODUCTOS);
        return numRows;
    }

    public boolean updateItem(Integer id, String name, int qty, float price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_NAME, name);
        contentValues.put(ITEM_QTY, qty);
        contentValues.put(ITEM_PRICE, price);
        db.update(ITEM_PRODUCTOS, contentValues, ITEM_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ITEM_PRODUCTOS,
                ITEM_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Cursor getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + ITEM_PRODUCTOS + " WHERE " +
                ITEM_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + ITEM_PRODUCTOS, null );
        return res;
    }

    public double getTotal(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT SUM("+ITEM_QTY+" * "+ITEM_PRICE+") FROM "+ITEM_PRODUCTOS, null);
        if (res.moveToFirst()){
            return res.getDouble(0);
        }else {
            return 0;
        }
        //Cursor res =  db.rawQuery( "SELECT SUM(qty * price) FROM " + ITEM_PRODUCTOS, null );
        //return res;
    }
}
