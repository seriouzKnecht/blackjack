package knecht.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class CardDeck {

	ArrayMap<Integer, String> CardMap;

	public CardDeck() {
		CardMap = new ArrayMap<Integer, String>();
		// Diamonds
		CardMap.put(1, "AD");
		CardMap.put(2, "2D");
		CardMap.put(3, "3D");
		CardMap.put(4, "4D");
		CardMap.put(5, "5D");
		CardMap.put(6, "6D");
		CardMap.put(7, "7D");
		CardMap.put(8, "8D");
		CardMap.put(9, "9D");
		CardMap.put(10, "10D");
		CardMap.put(11, "JD");
		CardMap.put(12, "QD");
		CardMap.put(13, "KD");
		// Hearts
		CardMap.put(21, "AH");
		CardMap.put(22, "2H");
		CardMap.put(23, "3H");
		CardMap.put(24, "4H");
		CardMap.put(25, "5H");
		CardMap.put(26, "6H");
		CardMap.put(27, "7H");
		CardMap.put(28, "8H");
		CardMap.put(29, "9H");
		CardMap.put(30, "10H");
		CardMap.put(31, "JH");
		CardMap.put(32, "QH");
		CardMap.put(33, "KH");
		// Spades
		CardMap.put(41, "AS");
		CardMap.put(42, "2S");
		CardMap.put(43, "3S");
		CardMap.put(44, "4S");
		CardMap.put(45, "5S");
		CardMap.put(46, "6S");
		CardMap.put(47, "7S");
		CardMap.put(48, "8S");
		CardMap.put(49, "9S");
		CardMap.put(50, "10S");
		CardMap.put(51, "JS");
		CardMap.put(52, "QS");
		CardMap.put(53, "KS");
		// Clubs
		CardMap.put(61, "AC");
		CardMap.put(62, "2C");
		CardMap.put(63, "3C");
		CardMap.put(64, "4C");
		CardMap.put(65, "5C");
		CardMap.put(66, "6C");
		CardMap.put(67, "7C");
		CardMap.put(68, "8C");
		CardMap.put(69, "9C");
		CardMap.put(70, "10C");
		CardMap.put(71, "JC");
		CardMap.put(72, "QC");
		CardMap.put(73, "KC");
	}

	public int getValue(int id) {
		int value = id % 20;
		if (value >= 10) {
			return 10;
		} else if (value == 1) {
			return 11; // Ass
		} else {
			return value;
		}
	}

	public Array<Integer> getFullSet() {
		Array<Integer> array = new Array<Integer>(false, 52);
		for (Integer i : CardMap.keys()) {
			array.add(i.intValue());
		}
		return array;
	}

	public void shuffle(Array<Integer> array) {
		// TODO own shuffle
		array.shuffle();
	}

	public void renderCard(SpriteBatch batch, int id, float x, float y, float w, float h) {
		int row = id / 20;
		int column = id % 20;
		column = column % 14;
		int index = column + row * 13 - 1;
		if (index >= 0 && index < 52) {
			TextureRegion texture = Blackjack.instance.assets.cardTextures[index];
			batch.draw(texture, x, y, w, h);
//			Blackjack.instance.assets.font.draw(batch, CardMap.get(id), x, y - 30);
		}
	}
}
