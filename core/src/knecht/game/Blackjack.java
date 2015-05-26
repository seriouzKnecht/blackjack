package knecht.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Blackjack extends ApplicationAdapter {

	public enum GameState {
		START, BET, ROUND, INSURANCE, PLAYERDRAW, BANKDRAW, COUNTING, WAIT
	}

	int GAME_WIDTH = 1280;
	int GAME_HEIGHT = 720;
	int PADDING = 10;
	int BUTTON_WIDTH = 256;
	int BUTTON_HEIGHT = 72;

	SpriteBatch batch;
	Sprite img;
	OrthographicCamera cam;
	StretchViewport viewport;
	Stage guiStage;
	Assets assets;
	Skin skin;

	TextButton ristBtn;
	TextButton cardBtn;
	TextButton betBtn;
	TextButton splitBtn;
	Label moneyLabel;
	int money;
	String moneyString;
	StringBuffer moneyStringBuffer;
	String loseMsg = "You lost!";
	String winMsg = "You won!";
	Label resultLabel;

	CardDeck cardDeck;
	DealerDeck dealerDeck;
	int dealerCard;

	DealedCardsActor bank;
	DealedCardsActor player;
	GameState currentState = GameState.START;
	private TextButton okBtn;
	protected boolean nextState;

	public static Blackjack instance;

	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		assets = new Assets();
		assets.load();
		batch = new SpriteBatch();
		initCam();

		guiStage = new Stage(viewport, batch);
		instance = this;

		initMenuBtns();
		initCards();

		Gdx.input.setInputProcessor(guiStage);
	}

	private void initCards() {
		cardDeck = new CardDeck();
		dealerDeck = new DealerDeck(this);
		dealerDeck.init();
		dealerDeck.setPosition(GAME_WIDTH - 250, GAME_HEIGHT - 300);
		dealerDeck.setWidth(100f);
		dealerDeck.setHeight(200f);
		guiStage.addActor(dealerDeck);

		bank = new DealedCardsActor(this, GAME_WIDTH / 2 - 30, GAME_HEIGHT - 200);
		guiStage.addActor(bank);
		player = new DealedCardsActor(this, GAME_WIDTH / 2 - 30, 200);
		guiStage.addActor(player);
	}

	private void initMenuBtns() {
		ristBtn = new TextButton("Rist", skin, "default");
		ristBtn.setWidth(BUTTON_WIDTH);
		ristBtn.setHeight(BUTTON_HEIGHT);
		ristBtn.setPosition(PADDING, PADDING);
		ristBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				player.rist = true;
				Gdx.app.log("Click", "Rist");
			}
		});
		guiStage.addActor(ristBtn);

		cardBtn = new TextButton("Card", skin, "default");
		cardBtn.setWidth(BUTTON_WIDTH);
		cardBtn.setHeight(BUTTON_HEIGHT);
		cardBtn.setPosition(2 * PADDING + BUTTON_WIDTH, PADDING);
		cardBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dealNextCard(player);
				Gdx.app.log("Click", "Card");

			}
		});
		guiStage.addActor(cardBtn);

		betBtn = new TextButton("Bet", skin, "default");
		betBtn.setWidth(BUTTON_WIDTH);
		betBtn.setHeight(BUTTON_HEIGHT);
		betBtn.setPosition(GAME_WIDTH - 2 * PADDING - 2 * BUTTON_WIDTH, PADDING);
		betBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				money -= 4;
				player.addBet(4);
				currentState = GameState.ROUND;
				Gdx.app.log("Click", "Bet");
			}
		});
		guiStage.addActor(betBtn);

		okBtn = new TextButton("OK", skin, "default");
		okBtn.setWidth(BUTTON_WIDTH);
		okBtn.setHeight(BUTTON_HEIGHT);
		okBtn.setPosition(GAME_WIDTH - 2 * PADDING - 2 * BUTTON_WIDTH, PADDING);
		okBtn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				nextState = true;
				Gdx.app.log("Click", "OK");
			}
		});
		okBtn.setVisible(false);
		guiStage.addActor(okBtn);

		splitBtn = new TextButton("Split", skin, "default");
		splitBtn.setWidth(BUTTON_WIDTH);
		splitBtn.setHeight(BUTTON_HEIGHT);
		splitBtn.setPosition(GAME_WIDTH - PADDING - BUTTON_WIDTH, PADDING);
		splitBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("Click", "Split");
			}
		});
		guiStage.addActor(splitBtn);

		money = 20;
		moneyString = "Money: ";
		moneyStringBuffer = new StringBuffer();
		moneyStringBuffer.append(moneyString);
		moneyStringBuffer.append(money);
		moneyLabel = new Label(moneyStringBuffer.toString(), skin) {
			@Override
			public void act(float delta) {
				super.act(delta);
				moneyStringBuffer.setLength(0);
				moneyStringBuffer.append(moneyString);
				moneyStringBuffer.append(money);
				setText(moneyStringBuffer.toString());
			}
		};
		moneyLabel.setPosition(3 * PADDING + 2 * BUTTON_WIDTH, PADDING);
		moneyLabel.setAlignment(Align.center);
		guiStage.addActor(moneyLabel);

		resultLabel = new Label("", skin, "message");
		resultLabel.setPosition(GAME_WIDTH / 2 - 200, GAME_HEIGHT / 2 + 30);
		resultLabel.setFontScale(3f);
		resultLabel.setVisible(false);
		guiStage.addActor(resultLabel);
	}

	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	private void initCam() {
		cam = new OrthographicCamera(GAME_WIDTH, GAME_HEIGHT);
		cam.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		viewport = new StretchViewport(GAME_WIDTH, GAME_HEIGHT, cam);
		batch.setProjectionMatrix(cam.combined);
	}

	@Override
	public void render() {

		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0.2f, 0.9f, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
//		batch.draw(assets.background, 0, 0, GAME_WIDTH, GAME_HEIGHT);
		batch.end();
		guiStage.draw();
	}

	private void update(float deltaTime) {
		guiStage.act(deltaTime);

		switch (currentState) {
		case BANKDRAW:
			betBtn.setVisible(false);
			cardBtn.setVisible(false);
			ristBtn.setVisible(false);
			splitBtn.setVisible(false);
			bankDraw();
			currentState = GameState.COUNTING;
			break;
		case BET:
			betBtn.setVisible(true);

			break;
		case COUNTING:
			betBtn.setVisible(false);
			cardBtn.setVisible(false);
			ristBtn.setVisible(false);
			splitBtn.setVisible(false);
			if (bank.currentValueHigh > 21) {
				playerWins();
			} else if (bank.currentValueHigh < player.currentValueHigh && player.currentValueHigh <= 21) {
				playerWins();
			} else {
				showMsg(loseMsg);
			}
			waitOk();
			currentState = GameState.WAIT;

			break;
		case INSURANCE:
			break;
		case PLAYERDRAW:
			betBtn.setVisible(false);
			cardBtn.setVisible(true);
			ristBtn.setVisible(true);
			splitBtn.setVisible(true);

			if (player.currentValueLow > 21) {
				currentState = GameState.COUNTING;
			}

			if (player.currentValueLow == 21) {
				player.rist = true;
			}

			if (player.rist) {
				currentState = GameState.BANKDRAW;
			}
			break;
		case ROUND:
			dealStartCards();

			currentState = GameState.PLAYERDRAW;
			break;
		case START:
			nextState = false;
			bank.clearCards(dealerDeck.usedCards);
			player.clearCards(dealerDeck.usedCards);
			player.clearBet();
			betBtn.setVisible(false);
			cardBtn.setVisible(false);
			ristBtn.setVisible(false);
			splitBtn.setVisible(false);
			if(dealerDeck.usedCards.size > 20) {
				dealerDeck.cards.addAll(dealerDeck.usedCards);
				dealerDeck.usedCards.clear();
				cardDeck.shuffle(dealerDeck.cards);
			}
			currentState = GameState.BET;
			break;
		case WAIT:
			if (nextState) {
				okBtn.setVisible(false);
				resultLabel.setVisible(false);
				currentState = GameState.START;
			}
		default:
			break;

		}

	}

	private void playerWins() {
		showMsg(winMsg);
		money += player.currentBet * 2;
	}

	private void bankDraw() {
		while (bank.currentValueHigh <= 16) {
			dealNextCard(bank);
		}
	}

	private void showMsg(String msg) {
		resultLabel.setText(msg);
		resultLabel.setVisible(true);
	}

	private void waitOk() {
		okBtn.setVisible(true);
	}

	private void dealStartCards() {
		dealNextCard(player);
		dealNextCard(player);

		dealNextCard(bank);
	}

	private void dealNextCard(DealedCardsActor actor) {
		actor.addCard(dealerDeck.cards.pop());
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		assets.dispose();
	}
}
