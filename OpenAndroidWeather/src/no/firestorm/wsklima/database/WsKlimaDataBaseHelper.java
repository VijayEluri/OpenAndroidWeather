/*
	Copyright 2011 Torstein Ingebrigtsen Bø

    This file is part of OpenAndroidWeather.

    OpenAndroidWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenAndroidWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenAndroidWeather.  If not, see <http://www.gnu.org/licenses/>.
 */

// Inspired by http://www.helloandroid.com/tutorials/how-have-default-database

package no.firestorm.wsklima.database;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.firestorm.ui.stationpicker.Station;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

public class WsKlimaDataBaseHelper extends SQLiteOpenHelper {
	@SuppressWarnings("unused")
	private static final String LOG_ID = "no.firestorm.db";
	private static final String DATABASE_NAME = "stations.db";
	private static Context mContext;
	private static final int DATABASE_VERSION = 4;
	public static final String STATIONS_TABLE_NAME = "stations";
	public static final String STATIONS_ID = "_id";
	public static final String STATIONS_NAME = "name";
	public static final String STATIONS_LON = "lon";
	public static final String STATIONS_LAT = "lat";
	private static final String STATION_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ STATIONS_TABLE_NAME + " (" + STATIONS_ID
			+ " INTEGER PRIMARY KEY," + STATIONS_NAME + " TEXT," + STATIONS_LAT
			+ " REAL," + STATIONS_LON + " REAL)";

	public WsKlimaDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	public List<Station> getStationsSortedAlphabetic(Location currentLocation) {
		final WsKlimaDataBaseHelper dbHelper = new WsKlimaDataBaseHelper(
				mContext);
		final SQLiteDatabase mDb = dbHelper.getReadableDatabase();
		final String[] select = { WsKlimaDataBaseHelper.STATIONS_ID,
				WsKlimaDataBaseHelper.STATIONS_NAME,
				WsKlimaDataBaseHelper.STATIONS_LAT,
				WsKlimaDataBaseHelper.STATIONS_LON };
		final String orderBy = WsKlimaDataBaseHelper.STATIONS_NAME;
		final Cursor c = mDb.query(WsKlimaDataBaseHelper.STATIONS_TABLE_NAME,
				select, null, null, null, null, orderBy);

		final List<Station> stationList = new ArrayList<Station>(c.getCount());

		final int nameCol = c
				.getColumnIndexOrThrow(WsKlimaDataBaseHelper.STATIONS_NAME);
		final int idCol = c
				.getColumnIndexOrThrow(WsKlimaDataBaseHelper.STATIONS_ID);
		final int latCol = c
				.getColumnIndexOrThrow(WsKlimaDataBaseHelper.STATIONS_LAT);
		final int lonCol = c
				.getColumnIndexOrThrow(WsKlimaDataBaseHelper.STATIONS_LON);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			final Station station = new Station(c.getString(nameCol),
					c.getInt(idCol), c.getDouble(latCol), c.getDouble(lonCol),
					currentLocation);
			stationList.add(station);
			c.moveToNext();
		}

		c.close();
		mDb.close();
		return stationList;
	}

	public List<Station> getStationsSortedByLocation(Location currentLocation) {
		final WsKlimaDataBaseHelper dbHelper = new WsKlimaDataBaseHelper(
				mContext);
		final SQLiteDatabase mDb = dbHelper.getReadableDatabase();
		final String[] select = { WsKlimaDataBaseHelper.STATIONS_ID,
				WsKlimaDataBaseHelper.STATIONS_NAME,
				WsKlimaDataBaseHelper.STATIONS_LAT,
				WsKlimaDataBaseHelper.STATIONS_LON };
		final String orderBy = WsKlimaDataBaseHelper.STATIONS_LAT;
		final Cursor c = mDb.query(WsKlimaDataBaseHelper.STATIONS_TABLE_NAME,
				select, null, null, null, null, orderBy);

		final List<Station> stationList = new ArrayList<Station>(c.getCount());

		final int nameCol = c
				.getColumnIndexOrThrow(WsKlimaDataBaseHelper.STATIONS_NAME);
		final int idCol = c
				.getColumnIndexOrThrow(WsKlimaDataBaseHelper.STATIONS_ID);
		final int latCol = c
				.getColumnIndexOrThrow(WsKlimaDataBaseHelper.STATIONS_LAT);
		final int lonCol = c
				.getColumnIndexOrThrow(WsKlimaDataBaseHelper.STATIONS_LON);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			final Station station = new Station(c.getString(nameCol),
					c.getInt(idCol), c.getDouble(latCol), c.getDouble(lonCol),
					currentLocation);
			stationList.add(station);
			c.moveToNext();
		}

		c.close();
		mDb.close();
		Collections.sort(stationList);
		return stationList;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(STATION_CREATE);

		try {
			// Open the file that is the first
			// command line parameter
			final InputStream fstream = mContext.getAssets().open(
					"stations.txt");
			// Get the object of DataInputStream
			final DataInputStream in = new DataInputStream(fstream);
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					in));
			String strLine;
			while ((strLine = br.readLine()) != null)
				db.execSQL(strLine);
			in.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < DATABASE_VERSION) {
			db.execSQL("DROP TABLE IF EXISTS " + STATIONS_TABLE_NAME);
			onCreate(db);
		}
	}

}
