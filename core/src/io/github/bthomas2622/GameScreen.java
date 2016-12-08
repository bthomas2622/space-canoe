package io.github.bthomas2622;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

/**
 * Created by bthom on 12/7/2016.
 */

public class GameScreen implements Screen {
    final SpaceCanoe game;
    Texture canoeImage;
    Texture spaceDebrisImage;
    Sound paddleSound;
    Sound collisionSound;
    Music backgroundMusic;
    OrthographicCamera camera;
    Rectangle canoe;
    Array<Rectangle> spaceDebris;
    long lastDebrisTime;
    int debrisDodged;

    public GameScreen(final SpaceCanoe gam) {
        this.game = gam;

        // load the images for the canoe and the space debris
        canoeImage = new Texture(Gdx.files.internal("holderRectangle.PNG"));
        spaceDebrisImage = new Texture(Gdx.files.internal("holderSquare.png"));

        // load the drop sound effect and the rain background "music"
//        paddleSound = Gdx.audio.newSound(Gdx.files.internal("TBD"));
//        collisionSound = Gdx.audio.newSound(Gdx.files.internal("TBD"));
//        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("TBD"));
//        backgroundMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        // create a Rectangle to logically represent the canoe
        canoe = new Rectangle();
        canoe.x = 1280 / 2 - 300 / 2; // center the canoe horizontally
        canoe.y = 720 / 2 - 100 / 2; // bottom left corner of the canoe is centered
        // the bottom screen edge
        canoe.width = 300;
        canoe.height = 100;

        // create the space debris array and spawn the first piece of debris
        spaceDebris = new Array<Rectangle>();
        spawnDebris();

    }

    //sets location for new space debris
    private void spawnDebris() {
        Rectangle debris = new Rectangle();
        debris.x = 1300;
        debris.y = MathUtils.random(0, 720);
        debris.width = 50;
        debris.height = 50;
        spaceDebris.add(debris);
        lastDebrisTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the canoe and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Debris Dodged: " + debrisDodged, 0, 480);
        game.batch.draw(canoeImage, canoe.x, canoe.y, canoe.width, canoe.height);
        for (Rectangle debris : spaceDebris) {
            game.batch.draw(spaceDebrisImage, debris.x, debris.y);
        }
        game.batch.end();

        for (Rectangle debris : spaceDebris) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                debris.y -= 200 * Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                debris.y += 200 * Gdx.graphics.getDeltaTime();
        }

//        if (Gdx.input.isKeyPressed(Input.Keys.UP))
//            canoe.x -= 200 * Gdx.graphics.getDeltaTime();
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
//            bucket.x += 200 * Gdx.graphics.getDeltaTime();

//        // make sure the bucket stays within the screen bounds
//        if (bucket.x < 0)
//            bucket.x = 0;
//        if (bucket.x > 800 - 64)
//            bucket.x = 800 - 64;

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDebrisTime > 1000000000)
            spawnDebris();

        // move the debris, remove any that are beyond edge of
        // the screen or that hit the canoe. iterate counter and add sound effect
        Iterator<Rectangle> iter = spaceDebris.iterator();
        while (iter.hasNext()) {
            Rectangle debris = iter.next();
            debris.x -= 200*Gdx.graphics.getDeltaTime();
            if (debris.x < 0){
                iter.remove();
                debrisDodged++;
            }
            if (debris.overlaps(canoe)) {
                //launch game over screen
                //collisionSound.play();
                iter.remove();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        //backgroundMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        canoeImage.dispose();
        spaceDebrisImage.dispose();
        //collisionSound.dispose();
        //paddleSound.dispose();
        //backgroundMusic.dispose();
    }
}
