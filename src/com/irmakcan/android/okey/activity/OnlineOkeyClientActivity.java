package com.irmakcan.android.okey.activity;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
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

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;

import com.irmakcan.android.okey.model.GameInformation;
import com.irmakcan.android.okey.model.Tile;
import com.irmakcan.android.okey.model.TileColor;
import com.irmakcan.android.okey.model.TileSprite;

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
	
	
	private ITextureRegion mTileTextureRegion;
	private ITextureRegion mBoardWoodTextureRegion;

	private Font mFont;
	

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
		//Toast.makeText(this, "Touch & Drag the face!", Toast.LENGTH_LONG).show();

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
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, Color.WHITE);
		this.mFont.load();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		
		final Sprite tile = new TileSprite(0, 0, this.mTileTextureRegion, this.getVertexBufferObjectManager(), new Tile(TileColor.BLUE, 12) , mFont);
		tile.setScale(1);
		scene.attachChild(tile);
		scene.registerTouchArea(tile);
		scene.setTouchAreaBindingOnActionDownEnabled(true);

		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}
	
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback)	throws Exception {
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}