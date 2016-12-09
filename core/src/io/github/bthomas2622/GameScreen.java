package io.github.bthomas2622;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
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
    Sprite canoe;
    Array<Sprite> spaceDebris;
    ArrayList spaceDebrisList = new ArrayList();
    long lastDebrisTime;
    int debrisDodged;
    World world;
    Body canoeBody;
    Body debrisBody;
    float torque = 0.0f;

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

        // creating canoe sprite
        canoe = new Sprite(canoeImage);
        canoe.setPosition(Gdx.graphics.getWidth()/2 - canoe.getWidth() / 2, Gdx.graphics.getHeight() / 2 - canoe.getHeight() / 2);
        canoe.setOriginCenter();
        canoe.setRotation(0f);

        //phyiscs world and bodytypes
        world = new World(new Vector2(0, 0f), true);
        BodyDef canoeBodyDef = new BodyDef();
        BodyDef debrisBodyDef = new BodyDef();
        canoeBodyDef.type = BodyDef.BodyType.KinematicBody;
        debrisBodyDef.type = BodyDef.BodyType.DynamicBody;
        canoeBodyDef.position.set(canoe.getX(),canoe.getY());
        //create body in world using our definition
        canoeBody = world.createBody(canoeBodyDef);
        //define dimensions of the canoe physics shape
        PolygonShape canoeShape = new PolygonShape();
        canoeShape.setAsBox(canoe.getWidth()/2, canoe.getHeight()/2);
        //FixtureDef defines shape of body and properties like density
        FixtureDef canoeFixtureDef = new FixtureDef();
        canoeFixtureDef.shape = canoeShape;
        canoeFixtureDef.density = 0.1f;
        Fixture canoeFixture = canoeBody.createFixture(canoeFixtureDef);

        // create the space debris array and spawn the first piece of debris
        spaceDebris = new Array<Sprite>();
        spawnDebris();

        canoeShape.dispose();
    }

    //sets location for new space debris
    private void spawnDebris() {
        Sprite debris = new Sprite(spaceDebrisImage);
        if (getCanoeAngle() <= 45f || getCanoeAngle() >= 315f){
            debris.setPosition(Gdx.graphics.getWidth() + debris.getWidth() / 2, MathUtils.random()*Gdx.graphics.getHeight());
        } else if (getCanoeAngle() > 45f && getCanoeAngle() <= 135f){
            debris.setPosition(Gdx.graphics.getWidth()*MathUtils.random(), Gdx.graphics.getHeight() + debris.getHeight());
        } else if (getCanoeAngle() > 135f && getCanoeAngle() <= 225f){
            debris.setPosition(0 - debris.getWidth()/2, Gdx.graphics.getHeight()*MathUtils.random());
        } else {
            debris.setPosition(Gdx.graphics.getWidth()*MathUtils.random(), 0 - debris.getHeight() / 2);
        }

        spaceDebris.add(debris);

        BodyDef debrisBodyDef = new BodyDef();
        debrisBodyDef.type = BodyDef.BodyType.DynamicBody;
        debrisBodyDef.position.set(debris.getX(),debris.getY());
        //create body in world using our definition
        debrisBody = world.createBody(debrisBodyDef);
        //define dimensions of the canoe physics shape
        PolygonShape debrisShape = new PolygonShape();
        debrisShape.setAsBox(canoe.getWidth()/2, canoe.getHeight()/2);
        //FixtureDef defines shape of body and properties like density
        FixtureDef canoeFixtureDef = new FixtureDef();
        canoeFixtureDef.shape = debrisShape;
        canoeFixtureDef.density = 0.1f;
        Fixture canoeFixture = canoeBody.createFixture(canoeFixtureDef);

        // create the space debris array and spawn the first piece of debris
        spaceDebris = new Array<Sprite>();
        spawnDebris();

        debrisShape.dispose();

        lastDebrisTime = TimeUtils.nanoTime();
    }

    public float getCanoeAngle(){
        float currentDegrees = (canoeBody.getAngle());
        return currentDegrees;
    };

    public void setCanoeAngle(float degrees){
        //canoe.setRotation((float)Math.toRadians(degrees));
        canoe.setRotation(degrees);
        canoeBody.setTransform(canoeBody.getPosition(), degrees);
    };

    @Override
    public void render(float delta) {
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();
        //step physics forward at rate of 60hx
        world.step(1f/60f, 6, 2);
        //canoe.setPosition(canoeBody.getPosition().x, canoeBody.getPosition().y);

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            //canoe.setRotation((float)Math.toDegrees(30));
            if (getCanoeAngle() >= 330f)
                setCanoeAngle(0f);
            else
                setCanoeAngle((getCanoeAngle() + 30f));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (getCanoeAngle() <= 0f)
                setCanoeAngle(330f);
            else
                setCanoeAngle((getCanoeAngle() - 30f));
            //canoe.setRotation((float) getCanoeAngle(canoe) - 30f);
        }
        System.out.println(getCanoeAngle());
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);



        // begin a new batch and draw the canoe and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Debris Dodged: " + debrisDodged, 0, 480);
        game.batch.draw(canoe, canoe.getX(), canoe.getY(), canoe.getOriginX(), canoe.getOriginY(), canoe.getWidth(), canoe.getHeight(), canoe.getScaleX(), canoe.getScaleY(), canoe.getRotation());
        for (Sprite debris : spaceDebris) {
            game.batch.draw(debris, debris.getX(), debris.getY());
        }
        game.batch.end();

        // check if we need to create a new raindrop
//        if (TimeUtils.nanoTime() - lastDebrisTime > 1000000000)
//            spawnDebris();

        // move the debris, remove any that are beyond edge of
        // the screen or that hit the canoe. iterate counter and add sound effect
//        Iterator<Sprite> iter = spaceDebris.iterator();
//        while (iter.hasNext()) {
//            Sprite debris = iter.next();
//            debris.x -= 200*Gdx.graphics.getDeltaTime();
//            if (debris.x < 0){
//                iter.remove();
//                debrisDodged++;
//            }
//            if (debris.overlaps(canoe)) {
//                //launch game over screen
//                //collisionSound.play();
//                iter.remove();
//            }
//        }
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
        world.dispose();
        //collisionSound.dispose();
        //paddleSound.dispose();
        //backgroundMusic.dispose();
    }
}
