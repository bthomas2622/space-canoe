package io.github.bthomas2622;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by bthom on 12/10/2016.
 */

public class GameOverScreen implements Screen {
    final SpaceCanoe game;
    OrthographicCamera camera;
    Texture backgroundSpaceImage;
    BitmapFont endFont;
    BitmapFont scoreFont;
    BitmapFont highScoreFont;
    BitmapFont enterFont;
    String gameOver;
    String score;
    String highScoreString;
    String enterString = "PRESS 'ENTER' TO PLAY AGAIN";
    int debrisDodged;
    int highScore;
    int timesRowed;
    float gameOverWidth;
    float scoreWidth;
    Music gameOverMusic;
    Sound collisionSound;
    Preferences prefs = Gdx.app.getPreferences("My Preferences");

    public GameOverScreen(final SpaceCanoe gam, int dodged, int rows) {
        game = gam;
        camera = new OrthographicCamera();
        //camera.setToOrtho(false, 1280, 720);
        camera.setToOrtho(false, 1920, 1080);
        backgroundSpaceImage = new Texture(Gdx.files.internal("spaceBackground1920.png"));
        debrisDodged = dodged;
        try{
            if (prefs.getInteger("io.github.bthomas2622.highscore") > debrisDodged){
                highScore = prefs.getInteger("io.github.bthomas2622.highscore");
            }
            else {
                highScore = debrisDodged;
                prefs.putInteger("io.github.bthomas2622.highscore", highScore);
            }
        }
        catch (Exception err){
            highScore = debrisDodged;
            prefs.putInteger("io.github.bthomas2622.highscore", highScore);
        }
        timesRowed = rows;
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameOver.mp3"));
        gameOverMusic.setLooping(true);
        gameOverMusic.setVolume(0.65f);
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        collisionSound.play();
        gameOverMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SpaceMono-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter gameOverParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        gameOverParameter.size = 80;
        endFont = generator.generateFont(gameOverParameter);
        //generating a glyph layout to get the length of the string so i can center it
        GlyphLayout endGlyphLayout = new GlyphLayout();
        gameOver = "GAME OVER";
        endGlyphLayout.setText(endFont,gameOver);
        gameOverWidth = endGlyphLayout.width;
        //Score font
        FreeTypeFontGenerator.FreeTypeFontParameter scoreParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        scoreParameter.size = 40;
        scoreParameter.color = Color.RED;
        scoreFont = generator.generateFont(scoreParameter);
        GlyphLayout scoreGlyphLayout = new GlyphLayout();
        score = "Score: " + String.valueOf(debrisDodged) + "\n" + "Paddles: " + String.valueOf(timesRowed);
        scoreGlyphLayout.setText(scoreFont, score);
        scoreWidth = scoreGlyphLayout.width;
        //High Score font
        FreeTypeFontGenerator.FreeTypeFontParameter highScoreParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        highScoreParameter.size = 40;
        highScoreParameter.color = Color.GREEN;
        highScoreFont = generator.generateFont(highScoreParameter);
        highScoreString = "HIGH SCORE: " + String.valueOf(highScore);
        //press enter to play again text
        FreeTypeFontGenerator.FreeTypeFontParameter enterParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        enterParameter.size = 30;
        enterFont = generator.generateFont(enterParameter);
        //generating a glyph layout to get the length of the string so i can center it
        GlyphLayout enterLayout = new GlyphLayout();
        enterLayout.setText(enterFont,enterString);
        float enterWidth = enterLayout.width;
        generator.dispose(); //dispose generator to avoid memory leaks
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundSpaceImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        endFont.draw(game.batch, gameOver, Gdx.graphics.getWidth()/2 - gameOverWidth/2, Gdx.graphics.getHeight()/1.25f);
        scoreFont.draw(game.batch, score, Gdx.graphics.getWidth()/2 - scoreWidth/2, Gdx.graphics.getHeight()/1.5f);
        highScoreFont.draw(game.batch, highScoreString, Gdx.graphics.getWidth()/7, Gdx.graphics.getHeight()/7);
        enterFont.draw(game.batch, enterString, Gdx.graphics.getWidth()/2 - enterWidth/2, Gdx.graphics.getHeight()/3);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height){
        camera.setToOrtho(false, width, height);
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
        backgroundSpaceImage.dispose();
        endFont.dispose();
        scoreFont.dispose();
        gameOverMusic.dispose();
        collisionSound.dispose();
    }

}