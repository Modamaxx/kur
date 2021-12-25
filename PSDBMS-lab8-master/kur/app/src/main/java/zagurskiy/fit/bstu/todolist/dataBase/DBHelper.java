package zagurskiy.fit.bstu.todolist.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import zagurskiy.fit.bstu.todolist.BaseActivity;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DB.db";
    private static final int SCHEMA = 1;
    private static final String TABLE_NAME = "ParameterTable";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME + " (          "
                        + "id integer primary key autoincrement,"
                        + "name_Activity text not null,                    "
                        + "value_Activity text not null                    );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public void initialization(SQLiteDatabase db) {
        for (ActivityType dir : ActivityType.values()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name_Activity", String.valueOf(dir));
            contentValues.put("value_Activity", dir.getDisplayName());
            db.insert(TABLE_NAME, null, contentValues);
        }

    }

    public String selectRaw(SQLiteDatabase db, ActivityType nameActivity, BaseActivity baseActivity) {
        String s ="select value_Activity from ParameterTable  where name_Activity="+"'"+nameActivity+"'";
        Cursor cursor = db.rawQuery(s,
                null);

        if (cursor.moveToFirst()) {
            String value = cursor.getString(cursor.getColumnIndexOrThrow("value_Activity"));
            cursor.close();
            return value;

        } else {
            Toast.makeText(baseActivity, "Такого ключа не существует", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public boolean isExist(SQLiteDatabase db) {
        Cursor cCheckDB = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + "", null);

        if (cCheckDB != null) {
            cCheckDB.moveToFirst();

            if (cCheckDB.getInt(0) == 0) {
                return false;
            }
        }
        return true;
    }

}
