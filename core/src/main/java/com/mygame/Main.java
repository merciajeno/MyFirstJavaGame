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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Sprite bucketSprite;
    private FitViewport viewport;
    private Texture backgroundTexture;
    private Texture bucketTexture;
    private Texture dropTexture;
    private Sound dropSound;
    private Music music;
    private Vector2 touchPos;
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        
        viewport = new FitViewport(8, 5);
        
        //images
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");
        
        //sound
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        
        //sprite
        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);
        
        touchPos = new Vector2();
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
          draw();
          input();
    }
    
    private void input() {
    	float speed = 0.25f;
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
            bucketSprite.setCenterY(touchPos.y);
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
        
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
