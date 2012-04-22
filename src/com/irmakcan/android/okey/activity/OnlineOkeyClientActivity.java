package com.irmakcan.android.okey.activity;

import java.util.HashMap;
import java.util.Map;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
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
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.irmakcan.android.okey.gson.ChatResponse;
import com.irmakcan.android.okey.gson.DrawTileResponse;
import com.irmakcan.android.okey.gson.ErrorResponse;
import com.irmakcan.android.okey.gson.GameStartResponse;
import com.irmakcan.android.okey.gson.NewUserResponse;
import com.irmakcan.android.okey.gson.ThrowTileResponse;
import com.irmakcan.android.okey.gson.UserLeaveResponse;
import com.irmakcan.android.okey.gson.WonResponse;
import com.irmakcan.android.okey.gui.BlankTileSprite;
import com.irmakcan.android.okey.gui.Board;
import com.irmakcan.android.okey.gui.Constants;
import com.irmakcan.android.okey.gui.CornerTileStackRectangle;
import com.irmakcan.android.okey.gui.TileSprite;
import com.irmakcan.android.okey.gui.UserInfoArea;
import com.irmakcan.android.okey.model.GameInformation;
import com.irmakcan.android.okey.model.Player;
import com.irmakcan.android.okey.model.Position;
import com.irmakcan.android.okey.model.TableCorner;
import com.irmakcan.android.okey.model.TableManager;
import com.irmakcan.android.okey.model.Tile;
import com.irmakcan.android.okey.model.User;
import com.irmakcan.android.okey.websocket.OkeyWebSocketEventHandler;
import com.irmakcan.android.okey.websocket.WebSocketProvider;

import de.roderick.weberknecht.WebSocket;

public class OnlineOkeyClientActivity extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final String LOG_TAG = "OnlineOkeyClientActivity";

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private static final int TILE_WIDTH = 56;
	private static final int TILE_HEIGHT = 84;
	
	private static final int USER_AREA_WIDTH = 140;
	private static final int USER_AREA_HEIGHT = 22;

	private static final int CORNER_X_MARGIN = 30;
	private static final int CORNER_Y_MARGIN = 20;
	private static final Point[] CORNER_POINTS = new Point[] { 
		new Point((CAMERA_WIDTH - (TILE_WIDTH + CORNER_X_MARGIN)), CAMERA_HEIGHT - (((int)Constants.FRAGMENT_HEIGHT*2) + CORNER_Y_MARGIN + TILE_HEIGHT)), 
		new Point((CAMERA_WIDTH - (TILE_WIDTH + CORNER_X_MARGIN)), CORNER_Y_MARGIN), 
		new Point(CORNER_X_MARGIN, CORNER_Y_MARGIN), 
		new Point(CORNER_X_MARGIN, CAMERA_HEIGHT - (((int)Constants.FRAGMENT_HEIGHT*2) + CORNER_Y_MARGIN + TILE_HEIGHT)) };
	private static final Point[] USER_AREA_POINTS = new Point[] { 
		new Point((CAMERA_WIDTH/2 - USER_AREA_WIDTH/2), CAMERA_HEIGHT - (((int)Constants.FRAGMENT_HEIGHT*2) + CORNER_Y_MARGIN + USER_AREA_HEIGHT)), 
		new Point((CAMERA_WIDTH - (USER_AREA_WIDTH + CORNER_X_MARGIN)), (CAMERA_HEIGHT-((int)Constants.FRAGMENT_HEIGHT)*2)/2 - USER_AREA_HEIGHT/2), 
		new Point((CAMERA_WIDTH/2 - USER_AREA_WIDTH/2), CORNER_Y_MARGIN), 
		new Point(CORNER_X_MARGIN, (CAMERA_HEIGHT-((int)Constants.FRAGMENT_HEIGHT)*2)/2 - USER_AREA_HEIGHT/2)};
	// ===========================================================
	// Fields
	// ===========================================================

	

	private GameInformation mGameInformation;

	private TableManager mTableManager;



	private ITextureRegion mTileTextureRegion;
	private ITextureRegion mBoardWoodTextureRegion;

	private Font mTileFont;
	private Font mUserAreaFont;

	private Board mBoard;
	private Map<TableCorner, CornerTileStackRectangle> mCornerStacks;
	private Map<Position, UserInfoArea> mUserAreas;
	private Rectangle mCenterArea;

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
		this.mTileFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, true, Color.WHITE);
		this.mTileFont.load();

		this.mUserAreaFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 20, true, Color.WHITE);
		this.mUserAreaFont.load();
		
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
			final CornerTileStackRectangle cornerStack = new CornerTileStackRectangle(point.x, point.y, TILE_WIDTH, TILE_HEIGHT, this.getVertexBufferObjectManager(), corner, mScene);
			mScene.attachChild(cornerStack);
			this.mCornerStacks.put(corner, cornerStack);
			position = corner.nextPosition();
		}
		// Create center throwing area
		this.mCenterArea = new Rectangle(2*CORNER_X_MARGIN + Constants.TILE_WIDTH, CORNER_Y_MARGIN,
				CAMERA_WIDTH - (2*(2*CORNER_X_MARGIN + Constants.TILE_WIDTH)), CAMERA_HEIGHT - (mBoard.getHeight() + 2*CORNER_Y_MARGIN), 
				this.getVertexBufferObjectManager());
		this.mCenterArea.setAlpha(0f);
		mScene.attachChild(this.mCenterArea);
		// Create User Info Areas
		this.mUserAreas = new HashMap<Position, UserInfoArea>();
		position = Player.getPlayer().getPosition();
		for(int i=0;i < 4;i++){
			final Point point = USER_AREA_POINTS[i];
			final UserInfoArea userArea = new UserInfoArea(point.x, point.y, USER_AREA_WIDTH, USER_AREA_HEIGHT, this.getVertexBufferObjectManager(), this.mUserAreaFont);
			mScene.attachChild(userArea);
			this.mUserAreas.put(position, userArea);
			position = TableCorner.nextCornerFromPosition(position).nextPosition();
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

		this.mTableManager = new TableManager(Player.getPlayer().getPosition(), mBoard, this.mCornerStacks, this.mCenterArea, this.mUserAreas);
		this.mTableManager.setUserAt(Player.getPlayer().getPosition(), Player.getPlayer());
		for(User user : this.mGameInformation.getUserList()){
			this.mTableManager.setUserAt(user.getPosition(), user);
		}
		WebSocket webSocket = WebSocketProvider.getWebSocket();
		webSocket.setEventHandler(new OkeyWebSocketEventHandler(this));

		try {
			JSONObject json = new JSONObject().put("action", "ready");
			webSocket.send(json.toString());
		} catch (Exception e) {
			// TODO Handle
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			JSONObject json = new JSONObject().put("action", "leave_room");
			WebSocketProvider.getWebSocket().send(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================

	private TileSprite createNewTileSprite(final Tile pTile) {
		return new TileSprite(0, 0, this.mTileTextureRegion, this.getVertexBufferObjectManager(), pTile , this.mTileFont, this.mTableManager);
	}

	// Actions
	public void errorMessage(final ErrorResponse pErrorResponse){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(OnlineOkeyClientActivity.this.getApplicationContext(), pErrorResponse.getMessage(), Toast.LENGTH_SHORT).show();
				OnlineOkeyClientActivity.this.mTableManager.cancelPendingOperation();
			}
		});
	}
	public void gameStartMessage(final GameStartResponse pGameStartResponse){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TileSprite indicatorSprite = createNewTileSprite(pGameStartResponse.getIndicator());
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

				for(Tile tile : pGameStartResponse.getUserHand()){
					TileSprite ts = createNewTileSprite(tile);
					mScene.registerTouchArea(ts);
					ts.enableTouch();
					mScene.attachChild(ts);
					mBoard.addChild(ts);
				}
				mTableManager.setTurn(pGameStartResponse.getTurn());
				mTableManager.setCenterCount(pGameStartResponse.getCenterCount());
			}
		});
	}
	public void drawTileMessage(final DrawTileResponse pDrawTileResponse){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mTableManager.getUserPosition() == pDrawTileResponse.getTurn()){ // Drawn by user (center or corner)
					if(mTableManager.getCenterCount() == pDrawTileResponse.getCenterCount()){
						mTableManager.pendingOperationSuccess(mTableManager.getPreviousCornerStack());
					}else{
						TileSprite tileSprite = createNewTileSprite(pDrawTileResponse.getTile());
						mScene.registerTouchArea(tileSprite);
						tileSprite.enableTouch();
						mScene.attachChild(tileSprite);
						mTableManager.pendingOperationSuccess(tileSprite);
					}
				} else {
					if(mTableManager.getCenterCount() == pDrawTileResponse.getCenterCount()){ // Drawn from corner
						CornerTileStackRectangle tileStack = mTableManager.getCornerStack(TableCorner.previousCornerFromPosition(pDrawTileResponse.getTurn()));
						final TileSprite tileSprite = tileStack.pop();
						tileSprite.dispose();
						//mScene.unregisterTouchArea(tileSprite); TODO test
						runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								tileSprite.detachSelf();
							}
						});
					} else { // Drawn from center
						// TODO
					}
				}
				mTableManager.setCenterCount(pDrawTileResponse.getCenterCount());
			}
		});
	}
	public void throwTileMessage(final ThrowTileResponse pThrowTileResponse){
		final TableCorner prevCorner = TableCorner.previousCornerFromPosition(pThrowTileResponse.getTurn());
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(TableCorner.nextCornerFromPosition(mTableManager.getUserPosition()) == prevCorner){ // Tile thrown by this user
					mTableManager.setTurn(pThrowTileResponse.getTurn());
					mTableManager.pendingOperationSuccess(mTableManager.getCornerStack(prevCorner));
				} else {
					TileSprite tileSprite = createNewTileSprite(pThrowTileResponse.getTile());
					if(pThrowTileResponse.getTurn() == mTableManager.getUserPosition()){
						mScene.registerTouchArea(tileSprite);
						tileSprite.enableTouch();
					}
					mScene.attachChild(tileSprite);
					mTableManager.getCornerStack(prevCorner).push(tileSprite);
					mTableManager.setTurn(pThrowTileResponse.getTurn());
				}
			}
		});
	}
	public void userWonMessage(final WonResponse pWonResponse){
		// Show tiles

		// Finish game
		OnlineOkeyClientActivity.this.finish();
	}
	public void chatMessage(final ChatResponse pChatResponse){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(OnlineOkeyClientActivity.this, /*mTableManager.getUser().username TODO */pChatResponse.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
	}

	public void newUserMessage(final NewUserResponse pNewUserResponse) {
		final User user = new User(pNewUserResponse.getUserName(), pNewUserResponse.getPosition());
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTableManager.setUserAt(user.getPosition(), user);
			}
		});
	}
	public void userLeaveMessage(final UserLeaveResponse pUserLeaveResponse){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTableManager.clearUserAt(pUserLeaveResponse.getPosition());
			}
		});
	}
	

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}