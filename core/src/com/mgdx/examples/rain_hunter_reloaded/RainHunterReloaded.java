package com.mgdx.examples.rain_hunter_reloaded;

import com.badlogic.gdx.graphics.Texture;
import com.isoterik.mgdx.MinGdxGame;
import com.isoterik.mgdx.Scene;
import com.isoterik.mgdx.io.GameAssetsLoader;
import com.isoterik.mgdx.m2d.scenes.transition.SceneTransitions;

public class RainHunterReloaded extends MinGdxGame {
	@Override
	protected Scene initGame() {
		GameAssetsLoader assetsLoader = minGdx.assets;

		assetsLoader.enqueueFolderContents("sprites", Texture.class);
		assetsLoader.enqueueSfxFolder("sfx");

		assetsLoader.loadAssetsNow();

		splashTransition = SceneTransitions.fade(1f);
		return new MainMenuScene();
	}
}