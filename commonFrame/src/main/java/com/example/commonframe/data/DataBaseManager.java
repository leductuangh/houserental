package com.example.commonframe.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.commonframe.exception.DataBaseManagerException;
import com.example.commonframe.model.local.Category;
import com.example.commonframe.model.local.Current;
import com.example.commonframe.util.Utils;

public class DataBaseManager extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "CommonFrameDatabase";

	private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS %s";

	private static final String CREATE_TABLE_SQL = "CREATE TABLE %s ( %s )";

	private static final String COUNT_TABLE_SQL = "SELECT Count(*) FROM %s";

	private static final String SELECT_ALL_SQL = "SELECT * FROM %s";

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
				switch (index) {
				case CATEGORY:
					result = CATEGORY_COLUNM_ID
							+ "INTEGER PRIMARY KEY AUTOINCREMENT, "
							+ CATEGORY_COLUNM_NAME + " TEXT";
					break;
				case CURRENT:
					result = CURRENT_COLUNM_ID
							+ "INTEGER PRIMARY KEY AUTOINCREMENT, "
							+ CURRENT_COLUNM_NAME + " TEXT,"
							+ CURRENT_COLUNM_URL + " INTEGER";
					break;
				default:
					throw new DataBaseManagerException("Table not found!");
				}
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
		try {
			if (record instanceof Current) {
				result = insertCurrent((Current) record);
			} else if (record instanceof Category) {
				result = insertCategory((Category) record);
			} else
				throw new DataBaseManagerException("insert: Type not found!");
		} catch (DataBaseManagerException e) {
			e.printStackTrace();
		}
		return result;
	}

	public synchronized int delete(Object record) {
		int result = -1;
		try {
			if (record instanceof Current) {
				result = deleteCurrent((Current) record);
			} else if (record instanceof Category) {
				result = deleteCategory((Category) record);
			} else
				throw new DataBaseManagerException("delete: Type not found!");
		} catch (DataBaseManagerException e) {
			e.printStackTrace();
		}
		return result;
	}

	public synchronized int update(Object record) {
		int result = -1;
		try {
			if (record instanceof Current) {
				result = updateCurrent((Current) record);
			} else if (record instanceof Category) {
				result = updateCategory((Category) record);
			} else
				throw new DataBaseManagerException("update: Type not found!");
		} catch (DataBaseManagerException e) {
			e.printStackTrace();
		}
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
			case CATEGORY:
				records = getAllCategories();
				break;
			case CURRENT:
				records = getAllCurrents();
				break;
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
			case CATEGORY:
				record = getCategory(id);
				break;
			case CURRENT:
				record = getCurrent(id);
				break;
			default:
				throw new DataBaseManagerException("get: Type not found!");
			}

		} catch (DataBaseManagerException e) {
			e.printStackTrace();
		}
		return record;
	}

	private long insertCurrent(Current record) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Table.CURRENT_COLUNM_NAME, record.getName()); // Current Name
		values.put(Table.CURRENT_COLUNM_URL, record.getUrl()); // Current Url

		// Inserting Row
		long result = db.insert(Table.CURRENT.toString(), null, values);
		db.close(); // Closing database connection
		return result;
	}

	private int deleteCurrent(Current record) {
		SQLiteDatabase db = this.getWritableDatabase();
		int result = db.delete(Table.CURRENT.toString(), Table.CURRENT_COLUNM_ID + " = ?",
				new String[] { String.valueOf(record.getId()) });
		db.close();
		return result;
	}

	private int updateCurrent(Current record) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Table.CURRENT_COLUNM_NAME, record.getName());
		values.put(Table.CURRENT_COLUNM_URL, record.getUrl());

		// updating row
		return db.update(Table.CURRENT.toString(), values,
				Table.CURRENT_COLUNM_ID + " = ?",
				new String[] { String.valueOf(record.getId()) });
	}

	private Current getCurrent(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Table.CURRENT.toString(), new String[] {
				Table.CURRENT_COLUNM_ID, Table.CURRENT_COLUNM_NAME,
				Table.CURRENT_COLUNM_URL }, Table.CURRENT_COLUNM_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Current current = new Current(cursor.getInt(0));
		current.setName(cursor.getString(1));
		current.setUrl(cursor.getString(2));
		cursor.close();
		return current;
	}

	private List<Current> getAllCurrents() {
		List<Current> currenttList = new ArrayList<Current>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				String.format(SELECT_ALL_SQL, Table.CURRENT.toString()), null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Current current = new Current(cursor.getInt(0));
				current.setName(cursor.getString(1));
				current.setUrl(cursor.getString(2));
				// Adding contact to list
				currenttList.add(current);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// return contact list
		return currenttList;
	}

	private long insertCategory(Category record) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Table.CATEGORY_COLUNM_NAME, record.getName()); // Current
																	// Name
		// Inserting Row
		long result = db.insert(Table.CATEGORY.toString(), null, values);
		db.close(); // Closing database connection
		return result;
	}

	private int deleteCategory(Category record) {
		SQLiteDatabase db = this.getWritableDatabase();
		int result = db.delete(Table.CATEGORY.toString(), Table.CATEGORY_COLUNM_ID + " = ?",
				new String[] { String.valueOf(record.getId()) });
		db.close();
		return result;
	}

	private int updateCategory(Category record) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Table.CURRENT_COLUNM_NAME, record.getName());

		// updating row
		return db.update(Table.CATEGORY.toString(), values,
				Table.CATEGORY_COLUNM_ID + " = ?",
				new String[] { String.valueOf(record.getId()) });
	}

	private Category getCategory(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Table.CATEGORY.toString(), new String[] {
				Table.CATEGORY_COLUNM_ID, Table.CATEGORY_COLUNM_NAME },
				Table.CATEGORY_COLUNM_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Category category = new Category(cursor.getInt(0));
		category.setName(cursor.getString(1));

		cursor.close();
		return category;
	}

	private List<Category> getAllCategories() {
		List<Category> categorytList = new ArrayList<Category>();

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				String.format(SELECT_ALL_SQL, Table.CATEGORY.toString()), null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Category current = new Category(cursor.getInt(0));
				current.setName(cursor.getString(1));
				// Adding contact to list
				categorytList.add(current);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// return contact list
		return categorytList;
	}

}
