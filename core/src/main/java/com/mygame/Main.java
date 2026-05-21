package com.mygame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private BitmapFont font;
    private Sprite bucketSprite;
    private FitViewport viewport;
    private Texture backgroundTexture;
    private Texture bucketTexture;
    private Texture dropTexture;
    private Sound dropSound;
    private Music music;
    private Vector2 touchPos;
    private Array<Sprite> dropSprites;
    private float dropTimer;
    private long lastRecorded=0;
    private Rectangle bucketRectangle;
    private Rectangle dropRectangle;
    private int points = 0;
    
    @Override
    public void create() {
    	
    	
    	
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        
        //font
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.setUseIntegerPositions(false);
        font.getData().setScale(0.02f);
        
        
        viewport = new FitViewport(8, 5);
        
        //images
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");
        
        //sound
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        
      //music
    	music.setLooping(true);
    	music.play();
        
        //sprite
        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);
        
        touchPos = new Vector2();
        
        //droplets
        dropSprites = new Array<>();
        createDroplet();
        
        //rectangle
        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();
    }
    
    
    private void createDroplet()
    {
    	float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        
        //create dropSprite 
        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0f,worldWidth-dropWidth));
        dropSprite.setY(worldHeight);
        
        dropSprites.add(dropSprite);
        
    }
    
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//        batch.begin();
//        //batch.draw(image, 140, 210);
//        //font.draw(batch,"Happy Coding!", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
//        shape.begin(ShapeRenderer.ShapeType.Line);
//        shape.circle(200, 100, 78);
//        shape.end();
//        batch.end();
          
          input();
          logic();
          draw();
    }
    
    private void logic() {
		// TODO Auto-generated method stub
    	float worldWidth = viewport.getWorldWidth();
    	float worldHeight = viewport.getWorldHeight();
    	 float delta = Gdx.graphics.getDeltaTime(); // retrieve the current delta
    	float bucketWidth = bucketSprite.getWidth();
    	float bucketHeight = bucketSprite.getHeight();
    	
    	bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth-bucketWidth));
    	bucketRectangle.set(bucketSprite.getX(),bucketSprite.getY(),bucketWidth,bucketHeight);
    	//for drop to move
//    	for (Sprite dropSprite : dropSprites) { // can cause memory leaks as we are not removing object
//            dropSprite.translateY(-2f * delta); // move the drop downward every frame
//        }
    	// to prevent index related error
    	 for (int i = dropSprites.size - 1; i >= 0; i--) {
    	        Sprite dropSprite = dropSprites.get(i); // Get the sprite from the list
    	        float dropWidth = dropSprite.getWidth();
    	        float dropHeight = dropSprite.getHeight();

    	        dropSprite.translateY(-2f * delta);
                dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);
    	        // if the top of the drop goes below the bottom of the view, remove it
    	        if (dropSprite.getY() < -dropHeight) dropSprites.removeIndex(i);
    	        else if(dropRectangle.overlaps(bucketRectangle))
    	        	{
    	        	dropSprites.removeIndex(i);
    	        	points+=1;
    	        	dropSound.play();
    	        	}
    	    }
    	dropTimer+=delta;
    	if(dropTimer>1f)
    	{
    		createDroplet();
    		dropTimer = 0;
    	}
//    	long currentTime = TimeUtils.millis();
//    	if(currentTime-lastRecorded>1000)
//    	{
//    		lastRecorded=currentTime;
//    		createDroplet();
//    	}
    	
		
	}
	private void input() {
    	float speed = 10f;
    	float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // todo: Do something when the user presses the right arrow
        	bucketSprite.translateX(speed*delta);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
        	bucketSprite.translateX(-speed*delta);
        }
        
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get where the touch happened on screen
            viewport.unproject(touchPos); // Convert the units to the world units of the viewport
            bucketSprite.setCenterX(touchPos.x); // Change the horizontally centered position of the bucket
           // bucketSprite.setCenterY(touchPos.y); we don't need this now
        }
    }

    
    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        batch.draw(backgroundTexture,0,0,worldWidth,worldHeight);
        bucketSprite.draw(batch);
        
        //batch.draw(bucketTexture, 0, 0, 1,1); can be done like this also
        
        for(Sprite dropSprite:dropSprites) 
        {
        	dropSprite.draw(batch);
        }
       
        font.draw(batch, "Points:" + points, 0.5f, 4.8f);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        font.dispose();
    }
}
