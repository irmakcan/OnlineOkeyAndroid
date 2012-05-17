package com.irmakcan.android.okey.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
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
		return position;
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	class RoomHolder {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================
		private TextView mNameText;
		private List<TextTuple> mTextTuples;
		// ===========================================================
		// Constructors
		// ===========================================================
		public RoomHolder(View pGrid) {
			mNameText = (TextView)pGrid.findViewById(R.id.table_grid_tablename);
			mTextTuples = new ArrayList<TextTuple>();
			
			TextView tvName = (TextView)pGrid.findViewById(R.id.table_grid_username_player1);
			TextView tvPoint = (TextView)pGrid.findViewById(R.id.table_grid_point_player1);
			mTextTuples.add(new TextTuple(tvName, tvPoint));
			
			tvName = (TextView)pGrid.findViewById(R.id.table_grid_username_player2);
			tvPoint = (TextView)pGrid.findViewById(R.id.table_grid_point_player2);
			mTextTuples.add(new TextTuple(tvName, tvPoint));
			
			tvName = (TextView)pGrid.findViewById(R.id.table_grid_username_player3);
			tvPoint = (TextView)pGrid.findViewById(R.id.table_grid_point_player3);
			mTextTuples.add(new TextTuple(tvName, tvPoint));
			
			tvName = (TextView)pGrid.findViewById(R.id.table_grid_username_player4);
			tvPoint = (TextView)pGrid.findViewById(R.id.table_grid_point_player4);
			mTextTuples.add(new TextTuple(tvName, tvPoint));
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
			List<User> userList = pRoom.getUsers();
			for(int i=0;i < mTextTuples.size();i++){
				TextTuple tuple = mTextTuples.get(i);
				if(userList.size() > i){
					User user = userList.get(i);
					Log.v("RoomAdapter", "Room: " + pRoom.getName() + "user: " +user.getUserName() + " " + user.getPoints());
					tuple.getmNameText().setText(user.getUserName());
					tuple.getmPointText().setText(Integer.toString(user.getPoints()));
				}else{
					tuple.getmNameText().setText("");
					tuple.getmPointText().setText("");
					Log.v("RoomAdapter", "clearing text at index: " + i);
				}
			}
		}
		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
		private class TextTuple{
			private final TextView mNameText;
			private final TextView mPointText;
			public TextTuple(TextView pNameText, TextView pPointText) {
				mNameText = pNameText;
				mPointText = pPointText;
			}
			public TextView getmNameText() {
				return mNameText;
			}
			public TextView getmPointText() {
				return mPointText;
			}
		}
	}
}
