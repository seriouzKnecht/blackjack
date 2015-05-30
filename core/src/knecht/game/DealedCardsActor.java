package knecht.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class DealedCardsActor extends Group {

	public int CARD_WIDTH = 80;
	public int CARD_HEIGHT = 120;

	Array<Integer> cards;
	Label label;
	Blackjack game;
	int currentValueLow;
	int currentValueHigh;
	int currentBet;
	StringBuffer valueString;
	boolean rist;

	public DealedCardsActor(Blackjack game, int x, int y) {
		this.game = game;
		setPosition(x, y);
		cards = new Array<Integer>(8);
		label = new Label("Kackwurst", game.skin);
		label.setPosition(0, -70);
		label.setFontScale(1.3f);
		addActor(label);
		valueString = new StringBuffer();
		currentBet = 0;
		rist = false;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		int offsetX = 0;
		int offsetY = 0;
		for (int c : cards) {
			game.cardDeck.renderCard((SpriteBatch) batch, c, getX() + offsetX, getY() + offsetY, CARD_WIDTH, CARD_HEIGHT);
			offsetX += 15;
			offsetY += 15;
		}
	}

	public void addCard(int card) {
		cards.add(card);
		updateValue();
	}

	public void clearCards(Array<Integer> staple) {
		staple.addAll(cards);
		cards.clear();
		rist = false;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		updateValue();
		label.setText(valueString.toString());
	}

	private void updateValue() {
		int valueLow = 0;
		int valueHigh = 0;

		for (int c : cards) {
			int val = game.cardDeck.getValue(c);
			if (val == 11) {
				valueLow += 1;
				valueHigh += 11;
			} else {
				valueLow += val;
				valueHigh += val;
			}
		}

		currentValueLow = valueLow;
		currentValueHigh = valueHigh;
		valueString.setLength(0);
		if (currentValueHigh == currentValueLow || currentValueHigh > 21) {
			currentValueHigh = currentValueLow;
			valueString.append(currentValueLow);
		} else if (currentValueHigh == 21) {
			currentValueLow = currentValueHigh;
			valueString.append(currentValueLow);
		} else {
			valueString.append(currentValueLow);
			valueString.append("/");
			valueString.append(currentValueHigh);
		}
		valueString.append("\n");
		valueString.append(currentBet);
	}

	public void addBet(int i) {
		currentBet += i;
	}

	public void clearBet() {
		currentBet = 0;
	}

	public boolean hasBlackJack() {
		return cards.size == 2 && currentValueHigh == 21;
	}
}
