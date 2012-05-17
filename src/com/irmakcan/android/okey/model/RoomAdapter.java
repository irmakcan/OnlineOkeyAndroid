package com.irmakcan.android.okey.model;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.irmakcan.android.okey.R;

public class RoomAdapter extends BaseAdapter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final Context mContext;
	private List<Room> mRooms;
	// ===========================================================
	// Constructors
	// ===========================================================
	public RoomAdapter(Context pContext, List<Room> pRooms) {
		this.mContext = pContext;
		this.mRooms = pRooms;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView = convertView;
		RoomHolder roomHolder = null;
		
		if (gridView == null) {
			gridView = new View(mContext);

			// get layout from table.xml
			gridView = inflater.inflate(R.layout.table, null);
			
			roomHolder = new RoomHolder(gridView);
			gridView.setTag(roomHolder);
		} else {
//			gridView = (View) convertView;
			roomHolder = (RoomHolder)gridView.getTag();
		}
		
		roomHolder.populateFrom(this.mRooms.get(position));

		return gridView;
	}

	@Override
	public int getCount() {
		return this.mRooms.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mRooms.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	static class RoomHolder {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================
		private TextView mNameText;
		// ===========================================================
		// Constructors
		// ===========================================================
		public RoomHolder(View pGrid) {
			mNameText = (TextView)pGrid.findViewById(R.id.table_grid_tablename);
			
		}
		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
		public void populateFrom(final Room pRoom) {
			mNameText.setText(pRoom.getName());
			// TODO other stuff
		}
		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
