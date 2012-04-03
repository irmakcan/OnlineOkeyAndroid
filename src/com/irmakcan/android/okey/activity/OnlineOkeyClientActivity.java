package com.irmakcan.android.okey.activity;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.irmakcan.android.okey.gui.Board;
import com.irmakcan.android.okey.gui.TileSprite;
import com.irmakcan.android.okey.model.GameInformation;
import com.irmakcan.android.okey.model.Player;
import com.irmakcan.android.okey.model.TableManager;
import com.irmakcan.android.okey.model.Tile;
import com.irmakcan.android.okey.model.TileColor;
import com.irmakcan.android.okey.model.Position;

import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketMessage;

public class OnlineOkeyClientActivity extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String LOG_TAG = "OnlineOkeyClientActivity";

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private GameInformation mGameInformation;
	private Handler mHandler;

	private TableManager mTableManager;



	private ITextureRegion mTileTextureRegion;
	private ITextureRegion mBoardWoodTextureRegion;

	private Font mTileFont;




	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		Bundle b = this.getIntent().getExtras();
		if(b==null){
			this.finish();
		}else{
			this.mGameInformation = (GameInformation) b.getSerializable("game_information");
		}
		this.mHandler = new Handler();
		Log.v(LOG_TAG, "Table Name: " + this.mGameInformation.getTableName());
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BitmapTextureAtlas bitmapTextureAtlas;
		// Tile
		bitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 56, 84, TextureOptions.BILINEAR);
		this.mTileTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, this, "tile.png", 0, 0);
		bitmapTextureAtlas.load();

		// Board
		bitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 96, 144, TextureOptions.BILINEAR);
		this.mBoardWoodTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, this, "board_wood.png", 0, 0);
		bitmapTextureAtlas.load();

		// Load Fonts
		this.mTileFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, Color.WHITE);
		this.mTileFont.load();

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		// TEST TODO
		Board board = new Board(0, 0, mBoardWoodTextureRegion, this.getVertexBufferObjectManager());
		board.setPosition((CAMERA_WIDTH/2)-(board.getWidth()/2), CAMERA_HEIGHT-board.getHeight());
		scene.attachChild(board);

		final Sprite tile = new TileSprite(0, 0, this.mTileTextureRegion, this.getVertexBufferObjectManager(), new Tile(TileColor.BLUE, 12) , mTileFont);
		tile.setScale(1);
		scene.attachChild(tile);
		scene.registerTouchArea(tile);
		scene.setTouchAreaBindingOnActionDownEnabled(true);

		// TEST TODO
		Rectangle r = new Rectangle(15, 15, 40, 40, getVertexBufferObjectManager());
		r.setAlpha(0f);
		scene.attachChild(r);


		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback)	throws Exception {

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public synchronized void onGameCreated() {
		super.onGameCreated();

		this.mTableManager = new TableManager(Player.getPlayer());
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private WebSocketEventHandler mWebSocketEventHandler = new WebSocketEventHandler() {

		@Override
		public void onOpen() {
			Log.v(LOG_TAG, "OkeyGame WebSocket connected");
			throw new IllegalAccessError("Should not call onOpen in OkeyGame");
		}
		@Override
		public void onMessage(WebSocketMessage message) {
			Log.v(LOG_TAG, "OkeyGame Message received: " + message.getText());
			try {
				final JSONObject json = new JSONObject(message.getText());
				final String status =json.getString("status");
				if(status.equals("error")){
					final String errorMessage = json.getString("message");
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(OnlineOkeyClientActivity.this.getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
						}
					});
				} else if(status.equals("throw_tile")){
					// { "action":"throw_tile", "tile":"2:2" }
				} else if(status.equals("draw_tile")){
					// {"action":"draw_tile","tile":"8:0","turn":"east","center_count":47}
				} else if(status.equals("new_user")){
					// {"status":"new_user","position":"west","username":"irmak4"}
				} else if(status.equals("user_leave")){
					// {"status":"user_leave","position":"west"}
				} else if(status.equals("game_start")){
					// {"status":"game_start","turn":"south","center_count":48,"hand":["4:0","7:3",...,"6:2"],"indicator":"4:0"}
					Position turn = Position.fromString(json.getString("turn"));
					int centerCount = json.getInt("center_count");
					JSONArray jsonHand = json.getJSONArray("hand");
					List<Tile> userHand = new ArrayList<Tile>();
					for(int i=0;i < jsonHand.length();i++){
						userHand.add(Tile.fromString(jsonHand.getString(i)));
					}
					Tile indicator = Tile.fromString(json.getString("indicator"));
					//mTableManager.initializeGame();
				} else {
					// TODO
				}
			} catch (JSONException e) {
				// Messaging error TODO
				e.printStackTrace();
			}
		}
		@Override
		public void onClose() {
			Log.v(LOG_TAG, "OkeyGame WebSocket disconnected");
		}
	};
}