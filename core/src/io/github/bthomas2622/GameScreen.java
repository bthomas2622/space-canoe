package io.github.bthomas2622;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
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
    Texture spaceDebrisImageLarge;
    Texture spaceDebrisImageLargest;
    Texture backgroundSpaceImage;
    Texture purplePlanetImage;
    Texture orangePlanetImage;
    Sound paddleSound;
    Sound collisionSound;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    Music backgroundMusic;
    OrthographicCamera camera;
    Sprite canoe;
    Sprite purplePlanet;
    Sprite orangePlanet;
    Array<Sprite> spaceDebris;
    ArrayList spaceDebrisList = new ArrayList();
    long lastDebrisTime;
    int debrisDodged = 0;
    World world;
    Body canoeBody;
    Body debrisBody;
    Array<Body> bodies;
    Array<Fixture> debrisBodyFixture;
    float torque = 0.0f;
    float currentDegrees;
    Boolean gameOver = false;
    double getCanoeAngleDouble;
    int i;
    //font variables
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    BitmapFont gameFont;
    GlyphLayout glyphLayout;
    float countWidth;
    String debrisDodgedString;
    float[] canoePolygon;
    float debrisDiceRoller;

    /**
     * contstructor that takes in game object and creates game instance, loads in assets, creates debug renderer, world contact listener, etc.
     * @param gam SpaceCanoe game object that is rendered, batched, etc.
     */
    public GameScreen(final SpaceCanoe gam) {
        game = gam;

        // load the images for the canoe and the space debris
        canoeImage = new Texture(Gdx.files.internal("canoeSprite.png"));
        spaceDebrisImage = new Texture(Gdx.files.internal("spaceDebris.png"));
        spaceDebrisImageLarge = new Texture(Gdx.files.internal("spaceDebris100.png"));
        spaceDebrisImageLargest = new Texture(Gdx.files.internal("spaceDebris150.png"));
        backgroundSpaceImage = new Texture(Gdx.files.internal("spaceBackground1280.png"));
        purplePlanetImage = new Texture(Gdx.files.internal("purplePlanet.png"));
        orangePlanetImage = new Texture(Gdx.files.internal("orangePlanet.png"));

        // load the drop sound effect and the rain background "music"
//        paddleSound = Gdx.audio.newSound(Gdx.files.internal("TBD"));
//        collisionSound = Gdx.audio.newSound(Gdx.files.internal("TBD"));
//        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("TBD"));
//        backgroundMusic.setLooping(true);

        //debug renderer allows us to see physics simulation controlling the scen
        debugRenderer = new Box2DDebugRenderer();
        // create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        // creating canoe sprite
        canoe = new Sprite(canoeImage);
        canoe.setPosition(Gdx.graphics.getWidth()/2 - canoe.getWidth() / 2, Gdx.graphics.getHeight() / 2 - canoe.getHeight() / 2);
        //need to set origin center of sprite so that future rotations around center of sprite
        canoe.setOriginCenter();
        canoe.setRotation(0f);

        // creating planet sprites
        purplePlanet = new Sprite(purplePlanetImage);
        purplePlanet.setPosition(Gdx.graphics.getWidth()* (float) Math.random(), Gdx.graphics.getHeight()* (float) Math.random());
        purplePlanet.setOriginCenter();
        orangePlanet = new Sprite(orangePlanetImage);
        orangePlanet.setPosition(Gdx.graphics.getWidth()* (float) Math.random(), Gdx.graphics.getHeight()* (float) Math.random());
        orangePlanet.setOriginCenter();

        //phyiscs world and bodytypes
        world = new World(new Vector2(0f, 0f), true);
        BodyDef canoeBodyDef = new BodyDef();
        BodyDef debrisBodyDef = new BodyDef();
        canoeBodyDef.type = BodyDef.BodyType.DynamicBody;
        debrisBodyDef.type = BodyDef.BodyType.DynamicBody;
        canoeBodyDef.position.set(canoe.getX() + canoe.getWidth() / 2, canoe.getY() + canoe.getHeight() / 2);
        //create body in world using our definition
        canoeBody = world.createBody(canoeBodyDef);
        //define dimensions of the canoe physics shape
        PolygonShape canoeShape = new PolygonShape();
        //canoeShape.setAsBox(canoe.getWidth()/2, canoe.getHeight()/2);
        //float array of indices that make up shape of canoe for hit detection, it is a diamond, coordinates or with origin at center of canoe
        canoePolygon = new float[8];
        canoePolygon[0] = -112f;
        canoePolygon[1] = 0f;
        canoePolygon[2] = 0f;
        canoePolygon[3] = 26f;
        canoePolygon[4] = 112f;
        canoePolygon[5] = 0f;
        canoePolygon[6] = 0f;
        canoePolygon[7] = -26f;
        canoeShape.set(canoePolygon);
        //FixtureDef defines shape of body and properties like density
        FixtureDef canoeFixtureDef = new FixtureDef();
        canoeFixtureDef.shape = canoeShape;
        canoeFixtureDef.density = 1f;
        canoeFixtureDef.restitution = 1f;
        //Fixture canoeFixture = canoeBody.createFixture(canoeFixtureDef);
        canoeBody.setUserData("canoe");
        canoeBody.createFixture(canoeFixtureDef);

        //create contact listener for when debris collides with canoe
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (contact.isTouching()) {
                    if ((contact.getFixtureA().getBody().getUserData() == "debris" && contact.getFixtureB().getBody().getUserData() == "canoe") || (contact.getFixtureA().getBody().getUserData() == "canoe" && contact.getFixtureB().getBody().getUserData() == "debris")) {
                        System.out.println("COLLISION");
                        gameOver = true;
                    }
                }
            }
            @Override
            public void endContact(Contact contact) {
            }
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        //create font for dodge counter
        generator = new FreeTypeFontGenerator(Gdx.files.internal("SpaceMono-Bold.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.color = Color.RED;
        gameFont = generator.generateFont(parameter);
        //generating a glyph layout to get the length of the string so i can center it
        glyphLayout = new GlyphLayout();
        debrisDodgedString = String.valueOf(debrisDodged);
        glyphLayout.setText(gameFont,debrisDodgedString);
        countWidth = glyphLayout.width;

        // create the space debris array and spawn the first piece of debris
        spaceDebris = new Array<Sprite>();
        bodies = new Array<Body>();
        spawnDebris();

        canoeShape.dispose();
    }

    /**
     * method to spawn new space debris into game world, places based on canoe angle and gives initial velocity
     */
    private void spawnDebris() {
        debrisDiceRoller = MathUtils.random(10f);
        Sprite debris;
        if (debrisDiceRoller <= 6f){
            debris = new Sprite(spaceDebrisImage);
        }
        else if (debrisDiceRoller <= 9){
            debris = new Sprite(spaceDebrisImageLarge);
        }
        else {
            debris = new Sprite(spaceDebrisImageLargest);
        }
        //place the canoe just outside the screen wherever the canoe is facing
        if (getCanoeAngle() <= 45f || getCanoeAngle() >= 315f){
            debris.setPosition(Gdx.graphics.getWidth() + debris.getWidth() / 2, MathUtils.random()*Gdx.graphics.getHeight());
        } else if (getCanoeAngle() > 45f && getCanoeAngle() <= 135f){
            debris.setPosition(Gdx.graphics.getWidth()*MathUtils.random(), Gdx.graphics.getHeight() + debris.getHeight()/2);
        } else if (getCanoeAngle() > 135f && getCanoeAngle() <= 225f){
            debris.setPosition(0 - debris.getWidth()/2, Gdx.graphics.getHeight()*MathUtils.random());
        } else {
            debris.setPosition(Gdx.graphics.getWidth()*MathUtils.random(), 0 - debris.getHeight()/2);
        }

        //System.out.println(debris.getX());
        //System.out.println(debris.getY());

        //origin center allows debris to be rotated based off its origin
        debris.setOriginCenter();
        BodyDef debrisBodyDef = new BodyDef();
        debrisBodyDef.type = BodyDef.BodyType.DynamicBody;
        debrisBodyDef.position.set(debris.getX() + debris.getWidth()/2,debris.getY()+debris.getHeight()/2);
        //create body in world using our definition
        debrisBody = world.createBody(debrisBodyDef);
        //define dimensions of the canoe physics shape
        //PolygonShape debrisShape = new PolygonShape();
        CircleShape debrisShape = new CircleShape();
        debrisShape.setRadius(debris.getWidth()/2);
        //debrisShape.setAsBox(debris.getWidth()/2, debris.getHeight()/2);
        //FixtureDef defines shape of body and properties like density
        FixtureDef debrisFixtureDef = new FixtureDef();
        debrisFixtureDef.shape = debrisShape;
        debrisFixtureDef.density = 1.0f;
        debrisFixtureDef.restitution = 1.0f;
        debrisFixtureDef.friction = 0.0f;
        debrisBody.createFixture(debrisFixtureDef);
        //Fixture debrisFixture = canoeBody.createFixture(debrisFixtureDef);

        //initalize velocity to send towards at canoe
        if (getCanoeAngle() <= 45f || getCanoeAngle() >= 315f){
            debrisBody.setLinearVelocity(-100f, 0);
        } else if (getCanoeAngle() > 45f && getCanoeAngle() <= 135f){
            debrisBody.setLinearVelocity(0, -100f);
        } else if (getCanoeAngle() > 135f && getCanoeAngle() <= 225f){
            debrisBody.setLinearVelocity(100f, 0);
        } else {
            debrisBody.setLinearVelocity(0, 100f);
        }

        debrisBody.setUserData("debris");
        spaceDebris.add(debris);
        bodies.add(debrisBody);

        debrisShape.dispose();

        lastDebrisTime = TimeUtils.nanoTime();
    }

    /**
     * getter method to obtain the current orientation of the canoe
     * @return float of canoe angle in degrees
     */
    public float getCanoeAngle(){
        getCanoeAngleDouble = (double) canoeBody.getAngle();
        currentDegrees = (float) Math.toDegrees(getCanoeAngleDouble);
        return currentDegrees;
    };

    /**
     * setter method to rotate the game sprite and physics body towards new canoe angle
     * @param degrees
     */
    public void setCanoeAngle(float degrees){
        //canoe.setRotation((float)Math.toRadians(degrees));
        canoe.setOriginCenter();
        canoe.setRotation(degrees);
        double degreesDouble = (double) degrees;
        //System.out.println(degreesDouble);
        //System.out.println((float) Math.toRadians(degreesDouble));
        canoeBody.setTransform(canoeBody.getPosition(), (float) Math.toRadians(degreesDouble));
        //System.out.println(canoeBody.getAngle());
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

        //the impulseForce represents the instant force on the space debris objects as a result of a canoe paddle
        float impulseForce = 100000f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            //canoe.setRotation((float)Math.toDegrees(30));
            if (getCanoeAngle() >= 330f)
                setCanoeAngle(0f);
            else
                setCanoeAngle(getCanoeAngle() + 30f);
            i = 0;
            double doubleCanoeAngleInRadians = Math.toRadians((double) getCanoeAngle());
            for (Sprite debris : spaceDebris) {
                bodies.get(i).applyLinearImpulse(-impulseForce*(float)Math.cos(doubleCanoeAngleInRadians), -impulseForce*(float)Math.sin(doubleCanoeAngleInRadians), canoe.getOriginX(), canoe.getOriginY(), true);
                //debris.setPosition(bodies.get(i).getPosition().x + debris.getWidth(), bodies.get(i).getPosition().y);
                debris.setPosition(bodies.get(i).getPosition().x - debris.getWidth()/2,  bodies.get(i).getPosition().y - debris.getHeight()/2);
                debris.setRotation((float) Math.toDegrees((double) bodies.get(i).getAngle()));
                //System.out.println((float) Math.toDegrees((double) bodies.get(i).getAngle()));
                i++;
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            if (getCanoeAngle() <= 0f)
                setCanoeAngle(330f);
            else
                setCanoeAngle((getCanoeAngle() - 30f));
            i = 0;
            double doubleCanoeAngleInRadians = Math.toRadians((double) getCanoeAngle());
            for (Sprite debris : spaceDebris) {
                bodies.get(i).applyLinearImpulse(-impulseForce*(float)Math.cos(doubleCanoeAngleInRadians), -impulseForce*(float)Math.sin(doubleCanoeAngleInRadians), canoe.getOriginX(), canoe.getOriginY(), true);
                //debris.setPosition(bodies.get(i).getPosition().x + debris.getWidth(), bodies.get(i).getPosition().y);
                debris.setPosition(bodies.get(i).getPosition().x - debris.getWidth()/2,  bodies.get(i).getPosition().y - debris.getHeight()/2);
                debris.setRotation((float) Math.toDegrees(bodies.get(i).getAngle()));
                i++;
            }
        } else {
            i = 0;
            for (Sprite debris : spaceDebris) {
                bodies.get(i).applyForceToCenter((bodies.get(i).getLinearVelocity()).x * 25f, bodies.get(i).getLinearVelocity().y * 25f, true);
                //debris.setPosition(bodies.get(i).getPosition().x + debris.getWidth(), bodies.get(i).getPosition().y);
                debris.setPosition(bodies.get(i).getPosition().x - debris.getWidth()/2,  bodies.get(i).getPosition().y - debris.getHeight()/2);
                debris.setRotation((float) Math.toDegrees(bodies.get(i).getAngle()));
                //remove avoided space debris
                if (bodies.get(i).getPosition().x < - debris.getWidth() || bodies.get(i).getPosition().x > Gdx.graphics.getWidth() + debris.getWidth() || bodies.get(i).getPosition().y < -debris.getHeight() || bodies.get(i).getPosition().y > Gdx.graphics.getHeight() + debris.getHeight()){
                    spaceDebris.removeIndex(i); //destroys sprite associated with dodged debris
                    debrisBodyFixture = bodies.get(i).getFixtureList();
                    bodies.get(i).destroyFixture(debrisBodyFixture.first()); //destroys body fixture associated with dodged debris
                    bodies.removeIndex(i); //destroys body associated with dodged debris
                    debrisDodged++;
                    debrisDodgedString = String.valueOf(debrisDodged);
                }
                i++;
            }
        }
        //System.out.println(getCanoeAngle());
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        debugMatrix = game.batch.getProjectionMatrix().cpy();

        // begin a new batch and draw the canoe and all debris
        game.batch.begin();
        game.batch.draw(backgroundSpaceImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //game.font.draw(game.batch, "Debris Dodged: " + debrisDodged, 0, 480);
        game.batch.draw(purplePlanet, purplePlanet.getX(), purplePlanet.getY(), purplePlanet.getOriginX(), purplePlanet.getOriginY(), purplePlanet.getWidth(), purplePlanet.getHeight(), purplePlanet.getScaleX(), purplePlanet.getScaleY(), purplePlanet.getRotation());
        game.batch.draw(orangePlanet, orangePlanet.getX(), orangePlanet.getY(), orangePlanet.getOriginX(), orangePlanet.getOriginY(), orangePlanet.getWidth(), orangePlanet.getHeight(), orangePlanet.getScaleX(), orangePlanet.getScaleY(), orangePlanet.getRotation());
        gameFont.draw(game.batch, debrisDodgedString, Gdx.graphics.getWidth()/2 - countWidth/2, Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/6);
        game.batch.draw(canoe, canoe.getX(), canoe.getY(), canoe.getOriginX(), canoe.getOriginY(), canoe.getWidth(), canoe.getHeight(), canoe.getScaleX(), canoe.getScaleY(), canoe.getRotation());
        for (Sprite debris : spaceDebris) {
            game.batch.draw(debris, debris.getX(), debris.getY(), debris.getOriginX(), debris.getOriginY(), debris.getWidth(), debris.getHeight(), debris.getScaleX(), debris.getScaleY(), debris.getRotation());
        }
        game.batch.end();

        //render the debug matrix
        debugRenderer.render(world, debugMatrix);
        //check if we need to create a new space debris object based on time in nanoseconds
        if (TimeUtils.nanoTime() - lastDebrisTime > 1000000000)
            spawnDebris();

        //check to see if a collision with the canoe has been detected to generate the game over screen
        if (gameOver){
            game.setScreen(new GameOverScreen(game));
            dispose();
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
        spaceDebrisImageLarge.dispose();
        spaceDebrisImageLargest.dispose();
        backgroundSpaceImage.dispose();
        purplePlanetImage.dispose();
        orangePlanetImage.dispose();
        world.dispose();
//        collisionSound.dispose();
//        paddleSound.dispose();
//        backgroundMusic.dispose();
        debugRenderer.dispose();
        generator.dispose();
    }
}
