package knecht.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	
	BitmapFont font;
	Texture cards;
	Texture background;
	TextureRegion[] cardTextures;

	public void load() {
		font = new BitmapFont();
		cards = new Texture(Gdx.files.internal("cards2modded.png"));
		cards.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		int leftPadd = 0;
		int topPadd = 0;
		int cardWidth = 79;
		int cardHeight = 123;
		cardTextures = new TextureRegion[52];
		
		for(int i = 0; i < 52; i++) {
			cardTextures[i] = new TextureRegion(cards, leftPadd + i%13*cardWidth, topPadd + i/13*cardHeight, cardWidth, cardHeight);
		}
		background = new Texture(Gdx.files.internal("blackjack.jpg"));
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
