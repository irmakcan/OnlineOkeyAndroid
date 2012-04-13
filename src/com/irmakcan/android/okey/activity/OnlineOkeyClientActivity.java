package com.irmakcan.android.okey.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
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
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.irmakcan.android.okey.gui.BlankTileSprite;
import com.irmakcan.android.okey.gui.Board;
import com.irmakcan.android.okey.gui.Constants;
import com.irmakcan.android.okey.gui.CornerTileStackRectangle;
import com.irmakcan.android.okey.gui.TileSprite;
import com.irmakcan.android.okey.model.GameInformation;
import com.irmakcan.android.okey.model.Player;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.TableCorner;
import com.irmakcan.android.okey.model.TableManager;
import com.irmakcan.android.okey.model.Tile;
import com.irmakcan.android.okey.websocket.WebSocketProvider;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketMessage;

public class OnlineOkeyClientActivity extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String LOG_TAG = "OnlineOkeyClientActivity";

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private static final int TILE_WIDTH = 56;
	private static final int TILE_HEIGHT = 84;

	private static final int CORNER_X_MARGIN = 30;
	private static final int CORNER_Y_MARGIN = 20;
	private static final Point[] CORNER_POINTS = new Point[] { 
		new Point((CAMERA_WIDTH - (TILE_WIDTH + CORNER_X_MARGIN)), CAMERA_HEIGHT - (((int)Constants.FRAGMENT_HEIGHT*2) + CORNER_Y_MARGIN + TILE_HEIGHT)), 
		new Point((CAMERA_WIDTH - (TILE_WIDTH + CORNER_X_MARGIN)), CORNER_Y_MARGIN), 
		new Point(CORNER_X_MARGIN, CORNER_Y_MARGIN), 
		new Point(CORNER_X_MARGIN, CAMERA_HEIGHT - (((int)Constants.FRAGMENT_HEIGHT*2) + CORNER_Y_MARGIN + TILE_HEIGHT)) };
	// ===========================================================
	// Fields
	// ===========================================================

	private GameInformation mGameInformation;
	private Handler mHandler;

	private TableManager mTableManager;



	private ITextureRegion mTileTextureRegion;
	private ITextureRegion mBoardWoodTextureRegion;

	private Font mTileFont;

	private Board mBoard;
	private Map<TableCorner, CornerTileStackRectangle> mCornerStacks;
	
	private Scene mScene;



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
		bitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), TILE_WIDTH, TILE_HEIGHT, TextureOptions.BILINEAR);
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

		mScene = new Scene();
		mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		// Create board
		this.mBoard = new Board(0, 0, mBoardWoodTextureRegion, this.getVertexBufferObjectManager());
		mBoard.setPosition((CAMERA_WIDTH/2)-(mBoard.getWidth()/2), CAMERA_HEIGHT-mBoard.getHeight());
		mScene.attachChild(mBoard);

		// Create corners
		this.mCornerStacks = new HashMap<TableCorner, CornerTileStackRectangle>();
		Position position = Player.getPlayer().getPosition();

		for(int i=0;i < 4;i++){
			TableCorner corner = TableCorner.nextCornerFromPosition(position);
			Log.v(LOG_TAG, corner.toString());
			final Point point = CORNER_POINTS[i];
			final CornerTileStackRectangle cornerStack = new CornerTileStackRectangle(point.x, point.y, TILE_WIDTH, TILE_HEIGHT, this.getVertexBufferObjectManager(), corner);
			mScene.attachChild(cornerStack);
			this.mCornerStacks.put(corner, cornerStack);
			position = corner.nextPosition();
		}

		mScene.setTouchAreaBindingOnActionDownEnabled(true);
		mScene.setTouchAreaBindingOnActionMoveEnabled(true);

		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback)	throws Exception {

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public synchronized void onGameCreated() {
		super.onGameCreated();

		this.mTableManager = new TableManager(Player.getPlayer().getPosition(), mBoard, this.mCornerStacks);
		WebSocket webSocket = WebSocketProvider.getWebSocket();
		webSocket.setEventHandler(mWebSocketEventHandler);

		try {
			JSONObject json = new JSONObject().put("action", "ready");
			webSocket.send(json.toString());
		} catch (Exception e) {
			// TODO Handle
			e.printStackTrace();
		}
		
	}
	// ===========================================================
	// Methods
	// ===========================================================
	
	private TileSprite createNewTileSprite(final Tile pTile) {
		return new TileSprite(0, 0, this.mTileTextureRegion, this.getVertexBufferObjectManager(), pTile , this.mTileFont, this.mTableManager);
	}
	
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
				final String status = json.getString("status");
				if(status.equals("error")){
					
					final String errorMessage = json.getString("message");
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(OnlineOkeyClientActivity.this.getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
						}
					});
					OnlineOkeyClientActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							OnlineOkeyClientActivity.this.mTableManager.cancelPendingOperation();
						}
					});
				} else if(status.equals("throw_tile")){
					// { "action":"throw_tile", "tile":"2:2" }
				} else if(status.equals("draw_tile")){
					// {"action":"draw_tile","tile":"8:0","turn":"east","center_count":47}
					String rawTile = json.getString("tile");
					Tile tile = null;
					if(rawTile != null){
						tile = Tile.fromString(rawTile);
					}
					String rawTurn = json.getString("turn");
					Position position = Position.fromString(rawTurn);
					int centerCount = json.getInt("center_count");
					
					// Run on ui thread ??
					if(mTableManager.getPosition() == position){
						mTableManager.pendingOperationSuccess(tile);
					} else {
						if(mTableManager.getCenterCount() == centerCount){ // Drawn from corner
							CornerTileStackRectangle tileStack = mTableManager.getCornerStack(TableCorner.previousCornerFromPosition(position));
							TileSprite tileSprite = tileStack.pop();
							tileSprite.dispose();
							//mScene.unregisterTouchArea(tileSprite); TODO test
							tileSprite.detachSelf();
						} else { // Drawn from center
							// TODO
						}
					}
				} else if(status.equals("new_user")){
					// {"status":"new_user","position":"west","username":"irmak4"}
				} else if(status.equals("user_leave")){
					// {"status":"user_leave","position":"west"}
				} else if(status.equals("game_start")){
					// {"status":"game_start","turn":"south","center_count":48,"hand":["4:0","7:3",...,"6:2"],"indicator":"4:0"}
					final Position turn = Position.fromString(json.getString("turn"));
					final int centerCount = json.getInt("center_count");
					JSONArray jsonHand = json.getJSONArray("hand");
					final List<Tile> userHand = new ArrayList<Tile>();
					for(int i=0;i < jsonHand.length();i++){
						userHand.add(Tile.fromString(jsonHand.getString(i)));
					}
					final Tile indicator = Tile.fromString(json.getString("indicator"));
					//mTableManager.initializeGame();
					OnlineOkeyClientActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							TileSprite indicatorSprite = createNewTileSprite(indicator);
							indicatorSprite.setPosition(CAMERA_WIDTH/2 - (indicatorSprite.getWidth() + Constants.TILE_PADDING_X), 
									(CAMERA_HEIGHT - mBoard.getHeight())/2 - indicatorSprite.getHeight()/2);
							mScene.attachChild(indicatorSprite);
							// CenterTiles TODO
							BlankTileSprite blankTileSprite = new BlankTileSprite(
									CAMERA_WIDTH/2 + Constants.TILE_PADDING_X, 
									(CAMERA_HEIGHT - mBoard.getHeight())/2 - indicatorSprite.getHeight()/2, 
									mTileTextureRegion, getVertexBufferObjectManager(), mTableManager);
							mScene.registerTouchArea(blankTileSprite);
							blankTileSprite.enableTouch();
							mScene.attachChild(blankTileSprite);
							
							for(Tile tile : userHand){
								TileSprite ts = createNewTileSprite(tile);
								mScene.registerTouchArea(ts);
								ts.enableTouch();
								mScene.attachChild(ts);
								mBoard.addChild(ts);
							}
							mTableManager.setTurn(turn);
							mTableManager.setCenterCount(centerCount);
						}
					});
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