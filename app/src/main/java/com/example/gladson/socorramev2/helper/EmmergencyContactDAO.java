package com.example.gladson.socorramev2.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gladson.socorramev2.model.EmmergencyContact;

import java.util.ArrayList;
import java.util.List;

public class EmmergencyContactDAO implements IEmmergencyContactDAO {

    private SQLiteDatabase write;
    private SQLiteDatabase read;

    public EmmergencyContactDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        write = dbHelper.getWritableDatabase();
        read = dbHelper.getReadableDatabase();
    }

    @Override
    public boolean save(EmmergencyContact emmergencyContact) {

        ContentValues cv = new ContentValues();
        cv.put("name", emmergencyContact.getName());
        cv.put("number", emmergencyContact.getNumber());

        try {
            write.insert(DBHelper.TABLE_CONTACTS, null, cv);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(EmmergencyContact emmergencyContact) {

        try {
            String[] args = {emmergencyContact.getName()};
            write.delete(DBHelper.TABLE_CONTACTS, "name=?", args);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public List<EmmergencyContact> list() {

        List<EmmergencyContact> ecList = new ArrayList<>();

        String sql = "SELECT * FROM " + DBHelper.TABLE_CONTACTS + " ;";
        Cursor c = read.rawQuery(sql, null);

        while (c.moveToNext()) {
            EmmergencyContact ec = new EmmergencyContact();

            String name = c.getString(c.getColumnIndex("name"));
            String number = c.getString(c.getColumnIndex("number"));

            ec.setName(name);
            ec.setNumber(number);

            ecList.add(ec);
        }

        return ecList;
    }
}
