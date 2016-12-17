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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by bthom on 12/7/2016.
 */

public class MainMenuScreen implements Screen {
    final SpaceCanoe game;
    OrthographicCamera camera;
    BitmapFont gameFont;
    Texture spaceCanoeImage;
    Sprite headerImage;

    public MainMenuScreen(final SpaceCanoe gam){
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        spaceCanoeImage = new Texture(Gdx.files.internal("SpaceCanoe800w.png"));
        headerImage = new Sprite(spaceCanoeImage);
        headerImage.setPosition(Gdx.graphics.getWidth()/2 - headerImage.getWidth() / 2, Gdx.graphics.getHeight() - headerImage.getHeight() - 50);
        headerImage.setOriginCenter();
        headerImage.setRotation(0f);
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
        String item = "PRESS 'ENTER'";
        glyphLayout.setText(gameFont,item);
        float enterWidth = glyphLayout.width;
        generator.dispose(); //dispose generator to avoid memory leaks
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        gameFont.draw(game.batch, "PRESS 'ENTER'", Gdx.graphics.getWidth()/2 - enterWidth/2, Gdx.graphics.getHeight()/4);
        //game.font.draw(game.batch, "PRESS 'ENTER'", 600, 300);
        game.batch.draw(headerImage, headerImage.getX(), headerImage.getY(), headerImage.getOriginX(), headerImage.getOriginY(), headerImage.getWidth(), headerImage.getHeight(), headerImage.getScaleX(), headerImage.getScaleY(), headerImage.getRotation());
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
