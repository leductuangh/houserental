package com.example.commonframe.data;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.commonframe.exception.DataBaseManagerException;
import com.example.commonframe.util.Utils;

public class DataBaseManager extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "CommonFrameDatabase";

	private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS %s";

	private static final String CREATE_TABLE_SQL = "CREATE TABLE %s ( %s )";

	private static final String COUNT_TABLE_SQL = "SELECT Count(*) FROM %s";

	private static DataBaseManager instance;

	public enum Table {
		CATEGORY {
			@Override
			public String toString() {
				return "Category";
			}
		},
		CURRENT {
			@Override
			public String toString() {
				return "Current";
			}
		};
		public static final String CATEGORY_COLUNM_ID = "id";
		public static final String CATEGORY_COLUNM_NAME = "name";

		public static final String CURRENT_COLUNM_ID = "id";
		public static final String CURRENT_COLUNM_NAME = "name";
		public static final String CURRENT_COLUNM_URL = "url";

		public static String getCreateString(Table index) {
			String result = "";
			try {

				if (Utils.isEmpty(result))
					throw new DataBaseManagerException(
							"Create string not found!");
			} catch (DataBaseManagerException e) {
				e.printStackTrace();
			}

			return result;
		}
	}

	public static DataBaseManager getInstance(Context context) {
		if (instance == null)
			instance = new DataBaseManager(context);
		return instance;
	}

	private DataBaseManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i < Table.values().length; ++i) {
			db.execSQL(String.format(CREATE_TABLE_SQL,
					Table.values()[i].toString(),
					Table.getCreateString(Table.values()[i])));
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop all tables if exist
		for (int i = 0; i < Table.values().length; ++i) {
			db.execSQL(String.format(DROP_TABLE_SQL,
					Table.values()[i].toString()));
		}

		// Re create all tables
		onCreate(db);
	}

	public synchronized long insert(Object record) {
		long result = -1;

		return result;
	}

	public synchronized int delete(Object record) {
		int result = -1;

		return result;
	}

	public synchronized int update(Object record) {
		int result = -1;

		return result;
	}

	public synchronized int count(Table table) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				String.format(COUNT_TABLE_SQL, table.toString()), null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		return count;
	}

	@SuppressWarnings("rawtypes")
	public synchronized List getAll(Table table) {
		List records = null;
		try {
			switch (table) {

			default:
				throw new DataBaseManagerException("getAll: Type not found!");
			}

		} catch (DataBaseManagerException e) {
			e.printStackTrace();
		}
		return records;
	}

	public synchronized Object get(Table table, int id) {
		Object record = null;
		try {
			switch (table) {

			default:
				throw new DataBaseManagerException("get: Type not found!");
			}

		} catch (DataBaseManagerException e) {
			e.printStackTrace();
		}
		return record;
	}

}
