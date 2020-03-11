package au.edu.murdoch.takemeaway;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TakeMeAwayDBHelper extends SQLiteOpenHelper {
    //declare variables
    //DB File name
    public static final String DB_NAME = "takemeaway.db";
    //table name
    public static final String TABLE_NAME = "tmaTravelPost";
    //columns
    public static final String COL_PostID = "postID";
    public static final String COL_PostTitle = "postTitle";
    public static final String COL_PostContent = "postContent";
    public static final String COL_PostDateTime = "postDateTime";
    public static final String COL_PostImage = "postImage";
    public static final String COL_PostLocationName = "postLocationName";
    public static final String COL_PostLocationAddr = "postLocationAddress";
    public static final String COL_PostLocationLat = "postLocationLat";
    public static final String COL_PostLocationLng = "postLocationLng";
    public static final String COL_IsDelete = "isDelete";
    //
    //db object
    SQLiteDatabase tmaDB;
    //
    //create statement
    public static final String DB_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            "(" +
                COL_PostID + " INTEGER NOT NULL CONSTRAINT tmaTravelPost_pk PRIMARY KEY AUTOINCREMENT, " +
                COL_PostTitle + " VARCHAR(128), " +
                COL_PostContent + " TEXT, " +
                COL_PostDateTime + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                COL_PostImage + " BLOB, " +
                COL_PostLocationName + " VARCHAR(60), " +
                COL_PostLocationAddr + " VARCHAR(80), " +
                COL_PostLocationLat + " TEXT, " +
                COL_PostLocationLng + " TEXT ," +
                COL_IsDelete + " BOOLEAN NOT NULL DEFAULT 0 " +
            ");";

    //TODO: change the single table to multiple table (e.g. PostImageTable, PostLocationTable, etc. as necessary)

    //
    //default constructor
    public TakeMeAwayDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }


    //only run on the first instance where the table does not exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(DB_CREATE);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //for any database upgrades such as adding of columns
        //Can use code snippet mentioned in https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
    }

    //open connection
    public SQLiteDatabase open() throws SQLException{
        tmaDB = getWritableDatabase();
        tmaDB.beginTransaction();
        return tmaDB;
    }


    //commit
    public void commit(){
        tmaDB.setTransactionSuccessful();
    }

    //close connection
    public void close(){
        tmaDB.endTransaction();
        tmaDB.close();
    }

    public long InsertRow(String title, String content, String datetime, String imageBlob, String locName, String locAddr, String locLat, String locLng){
        ContentValues cvs = new ContentValues();
        cvs.put(COL_PostTitle, title);
        cvs.put(COL_PostContent, content);
        cvs.put(COL_PostDateTime, datetime);
        cvs.put(COL_PostImage, imageBlob);
        cvs.put(COL_PostLocationName, locName);
        cvs.put(COL_PostLocationAddr, locAddr);
        cvs.put(COL_PostLocationLat, locLat);
        cvs.put(COL_PostLocationLng, locLng);


        return tmaDB.insert(TABLE_NAME,null,cvs);
    }

    public int UpdateRow(int postID, String title, String content, String datetime, String imageBlob, String locName, String locAddr, String locLat, String locLng){
        ContentValues cvs = new ContentValues();
        cvs.put(COL_PostTitle, title);
        cvs.put(COL_PostContent, content);
        cvs.put(COL_PostDateTime, datetime);
        cvs.put(COL_PostImage, imageBlob);
        cvs.put(COL_PostLocationName, locName);
        cvs.put(COL_PostLocationAddr, locAddr);
        cvs.put(COL_PostLocationLat, locLat);
        cvs.put(COL_PostLocationLng, locLng);

        return tmaDB.update(TABLE_NAME, cvs, COL_PostID + " = ?" , new String[]{String.valueOf(postID)});
    }

    public Cursor QueryRowsNotFlagDelete(int rowCount, String postDateSortASCorDESC){

        /*TIPS - Getting Values from Cursors
         * Ref: https://developer.android.com/reference/android/database/Cursor#public-methods
         *
         * E.g. c.getString(c.getColumnIndex(COL_NAME)) | where c = Cursor variable
         *
         */

        /*For columns param, use this or set null for all columns*/
        /*String[] queryCol = new String[]{
                COL_PostID, COL_PostTitle, COL_PostContent,
                COL_PostDateTime, COL_PostImage,COL_PostLocationName,
                COL_PostLocationAddr, COL_PostLocationLat, COL_PostLocationLng,
                COL_IsDelete};
        */
        String rowCountStr = null;
        if(rowCount > 0){
            rowCountStr = String.valueOf(rowCount);
        }
        String sortOrder = "DESC";
        if (postDateSortASCorDESC.equalsIgnoreCase("DESC")){
            sortOrder = "DESC";
        }
        else if(postDateSortASCorDESC.equalsIgnoreCase("ASC")){
            sortOrder = "ASC";
        }
        //else desc

        return tmaDB.query(
                TABLE_NAME, null, COL_IsDelete + " = 0",
                null, null,
                null, COL_PostDateTime + " " + sortOrder, rowCountStr );


    }



    //false delete row instead of a true delete -> raise a delete flag in the db, data is hidden but still exists
    public int FlagDeleteRow(int postID){
        ContentValues cvs = new ContentValues();

        // 1 = delete, 0 = not delete.
        cvs.put(COL_IsDelete, 1);

        return tmaDB.update(TABLE_NAME, cvs, COL_PostID + " = ?" , new String[]{String.valueOf(postID)});
    }

    public int DeleteRow(int postID){
        return tmaDB.delete(TABLE_NAME, COL_PostID + " = ?" , new String[]{String.valueOf(postID)});
    }

}
