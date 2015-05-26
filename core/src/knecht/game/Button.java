package knecht.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Button extends Actor {

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Blackjack.instance.assets.font.draw(batch, "test", getX(), getY());
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapes) {
		debug();
		super.drawDebug(shapes);
	}
}
