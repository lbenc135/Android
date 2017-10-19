package com.licoforen.qbot;

import com.licoforen.framework.Pixmap;
import com.licoforen.framework.Music;
import com.licoforen.framework.Sound;

public class Assets {

	public static Pixmap menu, splash, background, character, character2,
			character3, heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
	public static Pixmap tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft,
			tilegrassRight, characterJump, characterDown;
	public static Pixmap button;
	public static Sound click;
	public static Music theme;

	public static void load(SampleGame sampleGame) {
		theme = sampleGame.getAudio().createMusic("menutheme.mp3");
		theme.setLooping(true);
		theme.setVolume(0.85f);
		theme.play();
	}

}