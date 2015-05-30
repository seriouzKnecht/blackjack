package knecht.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public class DealerDeck extends Group {

	Blackjack blackjack;
	Array<Integer> cards;
	Array<Integer> usedCards;
	Label label;

	public DealerDeck(Blackjack blackjack) {
		this.blackjack = blackjack;
	}

	public void init(int numDecks) {
		cards = blackjack.cardDeck.getFullSet();
		for (int i = 0; i < numDecks - 1; ++i) {
			cards.addAll(blackjack.cardDeck.getFullSet());
		}
		blackjack.cardDeck.shuffle(cards);
		label = new Label("" + cards.size, blackjack.skin);
		label.setPosition(0, 0);
		addActor(label);
		usedCards = new Array<Integer>();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (cards.size > 0) {
			// blackjack.cardDeck.renderCard((SpriteBatch) batch,
			// cards.peek().intValue(), getX(), getY()+30, getWidth(),
			// getHeight()-30);
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		label.setText("" + cards.size);
	}

}
