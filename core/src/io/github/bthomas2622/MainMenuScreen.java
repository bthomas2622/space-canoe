package io.github.bthomas2622;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by bthom on 12/7/2016.
 */

public class MainMenuScreen implements Screen {
    final SpaceCanoe game;
    OrthographicCamera camera;
    BitmapFont gameFont;

    public MainMenuScreen(final SpaceCanoe gam){
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SpaceMono-Bold.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 20;
//        gameFont = generator.generateFont(parameter);
//        game.font = gameFont;
//        generator.dispose(); //dispose generator to avoid memory leaks
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SpaceMono-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        gameFont = generator.generateFont(parameter);
        generator.dispose(); //dispose generator to avoid memory leaks
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        gameFont.draw(game.batch, "PRESS 'ENTER'", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4);
        //game.font.draw(game.batch, "PRESS 'ENTER'", 600, 300);
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
    }

}
