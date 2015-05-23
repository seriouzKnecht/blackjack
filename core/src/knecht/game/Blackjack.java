package knecht.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Blackjack extends ApplicationAdapter {
	
	int GAME_WIDTH = 1280;
	int GAME_HEIGHT = 720;
	
	SpriteBatch batch;
	Sprite img;
	private OrthographicCamera cam;
	private StretchViewport viewport;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Sprite(new Texture("badlogic.jpg"));
		img.setPosition(0, 360);
		initCam();
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
	public void render () {
		
		update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(img, img.getX(), img.getY());
		batch.end();
	}

	private void update(float deltaTime) {
		img.setX((img.getX() + 128 * deltaTime) % 1280);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		img.getTexture().dispose();
	}
}
