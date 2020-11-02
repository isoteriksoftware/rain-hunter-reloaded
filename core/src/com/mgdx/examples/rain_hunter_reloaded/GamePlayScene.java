package com.mgdx.examples.rain_hunter_reloaded;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.isoterik.mgdx.*;
import com.isoterik.mgdx.audio.AudioManager;
import com.isoterik.mgdx.input.IKeyListener;
import com.isoterik.mgdx.input.KeyCodes;
import com.isoterik.mgdx.input.KeyEventData;
import com.isoterik.mgdx.input.KeyTrigger;
import com.isoterik.mgdx.io.GameAssetsLoader;
import com.isoterik.mgdx.utils.WorldUnits;

public class GamePlayScene extends Scene {
    private long lastDropTime;

    private final WorldUnits worldUnits;
    private final GameAssetsLoader assetsLoader;
    private final GameObject bucket;

    private final AudioManager audioManager;
    private final Sound dropSound;
    private final Music rainMusic;

    private int score;
    private Label scoreLabel;

    public GamePlayScene() {
        setBackgroundColor(new Color(0.1f, 0.1f, 0.2f, 1.0f));

        worldUnits = new WorldUnits(800f, 480f, 64f);
        mainCamera.setup(new ExtendViewport(worldUnits.getWorldWidth(),
                worldUnits.getWorldHeight(), mainCamera.getCamera()), worldUnits);

        assetsLoader = MinGdx.instance().assets;

        bucket = newSpriteObject(assetsLoader.getTexture("sprites/bucket.png"));
        Transform bucketTransform = bucket.transform;
        bucketTransform.position.x = (worldUnits.getWorldWidth() - bucketTransform.size.x) / 2f;
        bucketTransform.position.y = worldUnits.toWorldUnit(20f);

        bucket.addComponent(new BucketController());
        addGameObject(bucket);

        spawnRaindrop();

        rainMusic = assetsLoader.getMusic("sfx/rain.mp3");
        dropSound = assetsLoader.getSound("sfx/drop.wav");

        audioManager = AudioManager.instance();
        audioManager.playMusic(rainMusic, 1f, true);

        setupCanvas(new StretchViewport(800, 480));
        setupUI();
    }

    private void spawnRaindrop() {
        final GameObject raindrop = newSpriteObject(assetsLoader.getTexture("sprites/droplet.png"));
        final Transform transform = raindrop.transform;

        transform.position.x = MathUtils.random(0,
                worldUnits.getWorldWidth() - transform.size.x);
        transform.position.y = worldUnits.getWorldHeight();

        addGameObject(raindrop);

        final Rectangle bucketBounds = new Rectangle(bucket.transform.position.x,
                bucket.transform.position.y, bucket.transform.size.x,
                bucket.transform.size.y);
        final Rectangle raindropBounds = new Rectangle(transform.position.x, transform.position.y,
                transform.size.x, transform.size.y);

        raindrop.addComponent(new Component(){
            @Override
            public void update(float deltaTime) {
                float speed = worldUnits.toWorldUnit(200);
                transform.position.y -= speed * deltaTime;

                if (transform.position.y + transform.size.y < 0)
                    removeGameObject(raindrop);

                if (TimeUtils.timeSinceMillis(lastDropTime) > 1000)
                    spawnRaindrop();
            }

            @Override
            public void lateUpdate(float deltaTime) {
                bucketBounds.x = bucket.transform.position.x;
                bucketBounds.y = bucket.transform.position.y;

                raindropBounds.x = raindrop.transform.position.x;
                raindropBounds.y = raindrop.transform.position.y;

                if (raindropBounds.overlaps(bucketBounds)) {
                    removeGameObject(raindrop);

                    audioManager.playSound(dropSound, 1f);

                    score++;
                    scoreLabel.setText("Score: " + score);
                }
            }
        });

        lastDropTime = TimeUtils.millis();
    }

    private void setupUI() {
        BitmapFont font = new BitmapFont();

        Label.LabelStyle scoreTextStyle = new Label.LabelStyle();
        scoreTextStyle.font = font;
        scoreTextStyle.fontColor = Color.YELLOW;

        scoreLabel = new Label("Score: 0", scoreTextStyle);
        scoreLabel.setFontScale(2f);

        scoreLabel.setX(20);
        scoreLabel.setY(480 - scoreLabel.getHeight() * 2f);

        canvas.addActor(scoreLabel);
    }

    private static class BucketController extends Component {
        private static final String MAPPING_MOVE_LEFT = "BUCKET_MOVE_LEFT";
        private static final String MAPPING_MOVE_RIGHT = "BUCKET_MOVE_RIGHT";

        @Override
        public void start() {
            input.addMapping(MAPPING_MOVE_LEFT, KeyTrigger.keyDownTrigger(KeyCodes.LEFT).setPolled(true),
                    KeyTrigger.keyDownTrigger(KeyCodes.A).setPolled(true));
            input.addMapping(MAPPING_MOVE_RIGHT, KeyTrigger.keyDownTrigger(KeyCodes.RIGHT).setPolled(true),
                    KeyTrigger.keyDownTrigger(KeyCodes.D).setPolled(true));

            final Transform transform = gameObject.transform;
            final WorldUnits worldUnits = scene.getMainCamera().getWorldUnits();
            final float moveSpeed = worldUnits.toWorldUnit(200);

            input.mapListener(MAPPING_MOVE_LEFT, new IKeyListener() {
                @Override
                public void onKey(String mappingName, KeyEventData keyEventData) {
                    transform.position.x -= moveSpeed * MinGdx.instance().getDeltaTime();
                }
            });

            input.mapListener(MAPPING_MOVE_RIGHT, new IKeyListener() {
                @Override
                public void onKey(String mappingName, KeyEventData keyEventData) {
                    transform.position.x += moveSpeed * MinGdx.instance().getDeltaTime();
                }
            });
        }

        @Override
        public void update(float deltaTime) {
            Transform transform = gameObject.transform;
            WorldUnits worldUnits = scene.getMainCamera().getWorldUnits();

            if (input.isTouched()) {
                float touchX = input.getTouchedX();
                touchX -= transform.size.x / 2f;

                transform.position.x = touchX;
            }

            if (transform.position.x < 0)
                transform.position.x = 0;
            if (transform.position.x > worldUnits.getWorldWidth() - transform.size.x)
                transform.position.x = worldUnits.getWorldWidth() - transform.size.x;
        }
    }
}