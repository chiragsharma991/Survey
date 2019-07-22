package com.softtech360.totalservey.room.database;

import androidx.room.Room;

/*
public class DatabaseCopier {
    private static final String TAG = DatabaseCopier.class.getSimpleName();
    private static final String DATABASE_NAME = "chinook.db";

    private AppDatabase mAppDataBase;
    private AppDatabase inMemoryAppDatabase;
    private static Context appContext;

    private static class Holder {
        private static final DatabaseCopier INSTANCE = new DatabaseCopier();
    }

    public static DatabaseCopier getInstance(Context context) {
        appContext = context;
        return Holder.INSTANCE;
    }

    private DatabaseCopier() {
        //call method that check if database not exists and copy prepopulated file from assets
        copyAttachedDatabase(appContext, DATABASE_NAME);

        mAppDataBase = Room.databaseBuilder(appContext,
                AppDatabase.class, DATABASE_NAME)
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build();
        inMemoryAppDatabase = Room.inMemoryDatabaseBuilder(appContext,
                AppDatabase.class).allowMainThreadQueries()
                .build();

    }

    public AppDatabase getRoomDatabase() {
        return mAppDataBase;
    }
    public AppDatabase getRoomRefDatabase() {
        return inMemoryAppDatabase;
    }


    private void copyAttachedDatabase(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        // If the database already exists, return
        if (dbPath.exists()) {
            Log.e("dbPath.exists()-",""+dbPath.exists());
            return;
        }

        // Make sure we have a path to the file
        dbPath.getParentFile().mkdirs();

        // Try to copy database file
        try {
            Log.e("Copy",""+"database is going to copy");

            final InputStream inputStream = context.getAssets().open("databases/" + databaseName);
            final OutputStream output = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputStream.close();
        }
        catch (IOException e) {
            Log.d(TAG, "Failed to open file", e);
            e.printStackTrace();
        }
    }

}*/
