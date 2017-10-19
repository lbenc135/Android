package com.licoforen.qbot;

import com.licoforen.framework.Game;
import com.licoforen.framework.Graphics;
import com.licoforen.framework.Graphics.PixmapFormat;
import com.licoforen.framework.Screen;

public class LoadingScreen extends Screen {
	public LoadingScreen(Game game) {

		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.menu = g.newPixmap("menu.png", PixmapFormat.RGB565);
		Assets.background = g.newPixmap("background.png", PixmapFormat.RGB565);
		Assets.character = g.newPixmap("character.png", PixmapFormat.ARGB4444);
		Assets.character2 = g.newPixmap("character2.png", PixmapFormat.ARGB4444);
		Assets.character3 = g.newPixmap("character3.png", PixmapFormat.ARGB4444);
		Assets.characterJump = g.newPixmap("jumped.png", PixmapFormat.ARGB4444);
		Assets.characterDown = g.newPixmap("down.png", PixmapFormat.ARGB4444);

		Assets.heliboy = g.newPixmap("heliboy.png", PixmapFormat.ARGB4444);
		Assets.heliboy2 = g.newPixmap("heliboy2.png", PixmapFormat.ARGB4444);
		Assets.heliboy3 = g.newPixmap("heliboy3.png", PixmapFormat.ARGB4444);
		Assets.heliboy4 = g.newPixmap("heliboy4.png", PixmapFormat.ARGB4444);
		Assets.heliboy5 = g.newPixmap("heliboy5.png", PixmapFormat.ARGB4444);

		Assets.tiledirt = g.newPixmap("tiledirt.png", PixmapFormat.RGB565);
		Assets.tilegrassTop = g
				.newPixmap("tilegrasstop.png", PixmapFormat.RGB565);
		Assets.tilegrassBot = g
				.newPixmap("tilegrassbot.png", PixmapFormat.RGB565);
		Assets.tilegrassLeft = g.newPixmap("tilegrassleft.png",
				PixmapFormat.RGB565);
		Assets.tilegrassRight = g.newPixmap("tilegrassright.png",
				PixmapFormat.RGB565);

		Assets.button = g.newPixmap("button.jpg", PixmapFormat.RGB565);

		// This is how you would load a sound if you had one.
		// Assets.click = game.getAudio().createSound("explode.ogg");

		game.setScreen(new MainMenuScreen(game));

	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.splash, 0, 0);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {

	}
}