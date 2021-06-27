package com.sharif.ce.pac.man.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.sharif.ce.pac.man.Pacman;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setResizable(false);
		config.setTitle("Pac man");
		config.setWindowIcon("icons/gameIcon.png");
		config.setWindowedMode(900,500);
		new Lwjgl3Application(new Pacman(), config);
	}
}
