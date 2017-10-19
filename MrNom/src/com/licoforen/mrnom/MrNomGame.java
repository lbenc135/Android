package com.licoforen.mrnom;

import com.licoforen.framework.Screen;
import com.licoforen.framework.implementation.AndroidGame;

public class MrNomGame extends AndroidGame {
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}
}
