package io.github.bthomas2622;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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


    public MainMenuScreen(final SpaceCanoe gam){
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheets/TitleSprites.atlas")); //reference atlas file in assets folder
        //textureRegion = textureAtlas.findRegion("01SpaceCanoe800w");
        //spaceCanoeImage = new Texture(Gdx.files.internal("SpaceCanoe800w.png"));
//        headerImage = new Sprite(textureRegion);
//        headerImage.setPosition(Gdx.graphics.getWidth()/2 - headerImage.getWidth() / 2, Gdx.graphics.getHeight() - headerImage.getHeight() - 50);
//        headerImage.setOriginCenter();
//        headerImage.setRotation(0f);
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
        backgroundSpaceImage = new Texture(Gdx.files.internal("spaceBackground1280.png"));
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SpaceMono-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        gameFont = generator.generateFont(parameter);
        //generating a glyph layout to get the length of the string so i can center it
        GlyphLayout glyphLayout = new GlyphLayout();
        String item = "PRESS 'ENTER' TO PLAY";
        glyphLayout.setText(gameFont,item);
        float enterWidth = glyphLayout.width;
        generator.dispose(); //dispose generator to avoid memory leaks
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundSpaceImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        elapsedTime += Gdx.graphics.getDeltaTime();
        gameFont.draw(game.batch, item, Gdx.graphics.getWidth()/2 - enterWidth/2, Gdx.graphics.getHeight()/4);
        game.batch.draw(titleAnimation.getKeyFrame(elapsedTime, true), Gdx.graphics.getHeight()/2.75f, Gdx.graphics.getHeight()/3);
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
        textureAtlas.dispose();
    }

}
