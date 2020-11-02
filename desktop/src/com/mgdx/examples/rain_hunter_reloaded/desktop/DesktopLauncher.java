package com.mgdx.examples.rain_hunter_reloaded.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mgdx.examples.rain_hunter_reloaded.RainHunterReloaded;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Rain Hunter Reloaded";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new RainHunterReloaded(), config);
	}
}
