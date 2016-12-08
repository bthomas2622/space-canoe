package io.github.bthomas2622.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.bthomas2622.SpaceCanoe;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Space Canoe";
        config.width = 1280;
        config.height = 720;
		new LwjglApplication(new SpaceCanoe(), config);
	}
}
