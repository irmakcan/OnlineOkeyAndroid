package com.irmakcan.android.okey.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.irmakcan.android.okey.model.GameInformation;
import com.irmakcan.android.okey.model.Player;
import com.irmakcan.android.okey.model.User;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.websocket.MockWebSocketProvider;

public class MockStarterOkeyClientActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MockWebSocketProvider mp = MockWebSocketProvider.getInstance();
		Intent i = new Intent(this, OnlineOkeyClientActivity.class);
		List<User> userList = new ArrayList<User>();
		i.putExtra("game_information", new GameInformation("RoomName", userList)); // TODO
		Player.getPlayer().setPosition(Position.SOUTH);
		startActivity(i);
		
		this.finish();
	}
}
