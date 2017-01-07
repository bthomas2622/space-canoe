package io.github.bthomas2622;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by bthom on 12/7/2016.
 */

public class MainMenuScreen implements Screen {
    final SpaceCanoe game;
    OrthographicCamera camera;
    BitmapFont gameFont;
    TextureAtlas textureAtlas;
    Animation titleAnimation;
    Float elapsedTime = 0f;
    Texture rightPaddleImage;
    Texture leftPaddleImage;
    Texture backgroundSpaceImage;
    Sprite rightPaddle;
    Sprite leftPaddle;
    Music introMusic;
    AssetManager assetManager;
    boolean loaded = false;
    float enterWidth;
    GlyphLayout glyphLayout;
    String item;
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;


    public MainMenuScreen(final SpaceCanoe gam){
        game = gam;
        camera = new OrthographicCamera();
        //camera.setToOrtho(false, 1280, 720);
        camera.setToOrtho(false, 1920, 1080);
        textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheets/TitleSprites.atlas")); //reference atlas file in assets folder
        titleAnimation = new Animation(0.066f, textureAtlas.findRegions("spacecanoe"), Animation.PlayMode.LOOP);
        rightPaddleImage = new Texture(Gdx.files.internal("canoePaddleRight.png"));
        leftPaddleImage = new Texture(Gdx.files.internal("canoePaddleLeft.png"));
        rightPaddle = new Sprite(rightPaddleImage);
        rightPaddle.setPosition(Gdx.graphics.getWidth()/2 - rightPaddle.getWidth()*2, 0);
        rightPaddle.setOriginCenter();
        rightPaddle.setRotation(0f);
        leftPaddle = new Sprite(leftPaddleImage);
        leftPaddle.setPosition(Gdx.graphics.getWidth()/2 + leftPaddle.getWidth(), 0);
        leftPaddle.setOriginCenter();
        leftPaddle.setRotation(0f);
        backgroundSpaceImage = new Texture(Gdx.files.internal("spaceBackground1920.png"));
        assetManager = new AssetManager();
        assetManager.load("intro.mp3", Music.class);
        assetManager.finishLoading();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("SpaceMono-Bold.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        gameFont = generator.generateFont(parameter);
        //generating a glyph layout to get the length of the string so i can center it
        glyphLayout = new GlyphLayout();
        item = "TOUCH ANYWHERE TO PLAY";
        glyphLayout.setText(gameFont,item);
        enterWidth = glyphLayout.width;
        generator.dispose(); //dispose generator to avoid memory leaks
    }

    public boolean startMusic() {
        if(assetManager.isLoaded("intro.mp3")) {
            introMusic = assetManager.get("intro.mp3", Music.class);
            introMusic.setLooping(true);
            introMusic.setVolume(0.5f);
            introMusic.play();
            return true;
        }else {
            //System.out.println("not loaded yet");
            return false;
        }
    }

    @Override
    public void render(float delta){
        //make sure music is loaded
        if (loaded == false){
            loaded = startMusic();
        }
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //enterWidth = glyphLayout.width;
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundSpaceImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        elapsedTime += Gdx.graphics.getDeltaTime();
        gameFont.draw(game.batch, item, Gdx.graphics.getWidth()/2 - enterWidth/2, Gdx.graphics.getHeight()/4.5f);
        game.batch.draw(titleAnimation.getKeyFrame(elapsedTime, true), Gdx.graphics.getWidth()/2 - 400, Gdx.graphics.getHeight()/2);
        game.batch.draw(rightPaddle, rightPaddle.getX(), rightPaddle.getY(), rightPaddle.getOriginX(), rightPaddle.getOriginY(), rightPaddle.getWidth(), rightPaddle.getHeight(), rightPaddle.getScaleX(), rightPaddle.getScaleY(), rightPaddle.getRotation());
        game.batch.draw(leftPaddle, leftPaddle.getX(), leftPaddle.getY(), leftPaddle.getOriginX(), leftPaddle.getOriginY(), leftPaddle.getWidth(), leftPaddle.getHeight(), leftPaddle.getScaleX(), leftPaddle.getScaleY(), leftPaddle.getRotation());
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height){
        camera.setToOrtho(false, width, height);
        rightPaddle.setPosition(Gdx.graphics.getWidth()/2 - rightPaddle.getWidth()*2, 0);
        leftPaddle.setPosition(Gdx.graphics.getWidth()/2 + leftPaddle.getWidth(), 0);
        camera.update();
    }

    @Override
    public void show(){
    }

    @Override
    public void hide(){
    }

    @Override
    public void pause(){
    }

    @Override
    public void resume(){
    }

    @Override
    public void dispose(){
        gameFont.dispose();
        rightPaddleImage.dispose();
        leftPaddleImage.dispose();
        backgroundSpaceImage.dispose();
        textureAtlas.dispose();
        introMusic.dispose();
    }

}
