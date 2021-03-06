package com.dinoth.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dinoth.game.DinoTopHat;
import com.dinoth.game.DummyActionResolver;
import com.dinoth.logic.PcControls;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 540;
		config.title = "Dino Top Hat";
		new LwjglApplication(new DinoTopHat(new PcControls(), new DummyActionResolver(), false), config);
	}
}
