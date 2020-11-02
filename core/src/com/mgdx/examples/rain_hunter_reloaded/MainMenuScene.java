package com.mgdx.examples.rain_hunter_reloaded;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.isoterik.mgdx.MinGdx;
import com.isoterik.mgdx.Scene;
import com.isoterik.mgdx.input.ITouchListener;
import com.isoterik.mgdx.input.TouchEventData;
import com.isoterik.mgdx.input.TouchTrigger;
import com.isoterik.mgdx.m2d.scenes.transition.SceneTransitions;
import com.isoterik.mgdx.m2d.scenes.transition.TransitionDirection;

public class MainMenuScene extends Scene {
    public MainMenuScene() {
        setBackgroundColor(new Color(.1f, .1f, .1f, 1.0f));

        setupCanvas(new StretchViewport(800, 480));
        setupUI();

        inputManager.addListener(TouchTrigger.touchUpTrigger(), new ITouchListener() {
            @Override
            public void onTouch(String mappingName, TouchEventData touchEventData) {
                MinGdx.instance().setScene(new GamePlayScene(),
                        SceneTransitions.slice(1f, TransitionDirection.UP_DOWN, 15,
                                Interpolation.pow5Out));
            }
        });
    }

    private void centerOnScene(Actor actor) {
        actor.setX((800 - actor.getWidth()) / 2f);
        actor.setY((480 - actor.getHeight()) / 2f);
    }

    private void setupUI() {
        BitmapFont font = new BitmapFont();

        Label.LabelStyle titleTextStyle = new Label.LabelStyle();
        titleTextStyle.font = font;
        titleTextStyle.fontColor = Color.RED;

        Label.LabelStyle instructionTextStyle = new Label.LabelStyle();
        instructionTextStyle.font = font;
        instructionTextStyle.fontColor = Color.YELLOW;

        Label titleText = new Label("Rain Hunter Reloaded", titleTextStyle);
        titleText.setFontScale(3f);
        titleText.setAlignment(Align.center);

        Label instructionText = new Label("Touch/Click To Start!", instructionTextStyle);
        instructionText.setFontScale(2f);
        instructionText.setAlignment(Align.center);

        Container<Label> container = new Container<>(instructionText);
        container.setTransform(true);
        container.setOrigin(container.getWidth()/2f, container.getHeight()/2f);

        centerOnScene(titleText);
        titleText.setY(480 - titleText.getHeight() * 4);

        centerOnScene(container);

        container.addAction(Actions.forever(Actions.sequence(
                Actions.scaleTo(.7f, .7f, 1.5f),
                Actions.scaleTo(1f, 1f, 1.5f)
        )));

        canvas.addActor(titleText);
        canvas.addActor(container);
    }
}