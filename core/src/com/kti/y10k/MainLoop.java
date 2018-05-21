package com.kti.y10k;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import com.kti.y10k.control.Listener;
import com.kti.y10k.universe.Galaxy;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.NameFactory;
import com.kti.y10k.utilities.managers.AssetManager;
import com.kti.y10k.utilities.managers.GalaxyConstManager;
import com.kti.y10k.utilities.managers.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class MainLoop extends ApplicationAdapter {
	public static MainLoop instance;
	public static float WIN_WIDTH, WIN_HEIGHT;

	public boolean started = false;
	public boolean inMenu = false;
	public boolean inPrompt = false;
	public float dT = 10;

	private Camera camera;
	private ArrayList<Integer> fpsList;

	private ShapeRenderer renderer;
	public BitmapFont font;
	private SpriteBatch uiBatch;
	private SpriteBatch starBatch;

	private Sound sound;
	private long soundID;

	public Galaxy c;

	public Listener listener;
	private NameFactory nm;

	@Override
	public void create () {
		try {
			Logger.log(Logger.LogLevel.INFO, "Starting y10k...");
			Logger.log(Logger.LogLevel.INFO, "Initializing Camera and Batches...");

			instance = this;
			WIN_WIDTH = Gdx.graphics.getWidth();
			WIN_HEIGHT = Gdx.graphics.getHeight();

			camera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			List<Vector3> posStart = new ArrayList<>();
			posStart.add(new Vector3(-30, 651, 30));
			posStart.add(new Vector3(-467, 84, 464));
			posStart.add(new Vector3(0, 0, 1));
			posStart.add(new Vector3(1102, 0, -1592));
			posStart.add(new Vector3(-6094, 4356, 415));

			camera.position.set(posStart.get((int) (Math.random() * posStart.size())));
			camera.lookAt(0f, 0f, 0f);
			camera.near = 0.0f;
			camera.far = 3000.0f;

			renderer = new ShapeRenderer();
			renderer.setColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, 0.5f);
			renderer.rotate(1, 0, 0, 90);

			FreeTypeFontGenerator f = new FreeTypeFontGenerator(Gdx.files.absolute("assets/y10k-font.ttf"));
			FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
			ftfp.size = (int) (WIN_HEIGHT * 0.015f);
			font = f.generateFont(ftfp);
			f.dispose();

			starBatch = new SpriteBatch();
			uiBatch = new SpriteBatch();

			fpsList = new ArrayList<>(100);

			if (AssetManager.init() != 1) {
				System.exit(0);
			}

			if (GalaxyConstManager.init() != 1) {
				System.exit(0);
			}

			WindowManager.init();

			nm = new NameFactory("assets/predefs/constellations.txt", 2, 6);

			c = new Galaxy();

			listener = new Listener();

			try {
				sound = Gdx.audio.newSound(Gdx.files.internal("assets/audio/audio.mp3"));
				soundID = sound.play(0.15f);
				sound.setLooping(soundID, true);

			} catch (Exception e) {
				Logger.log(Logger.LogLevel.ERROR, "Couldn\'t load music...");
				Logger.log(e.getStackTrace());
			}

			System.gc();

			Logger.log(Logger.LogLevel.INFO, "Initialising Complete.");
		} catch (Exception e) {
			Logger.log(Logger.LogLevel.ERROR, "Could not set up game");
			Logger.log(e.getStackTrace());
			dispose();
			System.exit(0);
		}
	}

	@Override
	public void render () {
		dT = Gdx.graphics.getDeltaTime();
		listener.listen();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		camera.update();

		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
		if (!inMenu) c.renderAux(renderer);
		renderer.end();

		starBatch.begin();
		c.renderStars(starBatch, camera);
		starBatch.end();

		uiBatch.begin();

		int FPS = Math.round(1 / Gdx.graphics.getDeltaTime());
		if (fpsList.size() >= 99) fpsList.remove(0);
		fpsList.add(FPS);
		int averagedFPS = 0;
		for(int f : fpsList) {
			averagedFPS += f;
		}

		averagedFPS /= fpsList.size();
		font.draw(uiBatch, " FPS: " + averagedFPS, 0, Gdx.graphics.getHeight() - 10);

		font.draw(uiBatch, " Cam: " +  Math.round(camera.position.x * 10)  / 10.0 +
						", " + Math.round(camera.position.y * 10) / 10.0 +
						", " + Math.round(camera.position.z * 10) / 10.0,
				0, Gdx.graphics.getHeight() - 10 - font.getLineHeight());

		font.draw(uiBatch, " Star Count: " + c.size(), 0, Gdx.graphics.getHeight() - 10 - 2 * font.getLineHeight());

        WindowManager.render(uiBatch, font);

		uiBatch.end();
	}
	
	@Override
	public void dispose () {
		Logger.log(Logger.LogLevel.INFO, "Cleaning up...");

		renderer.dispose();
		uiBatch.dispose();

		c.release();

		sound.dispose();

		AssetManager.dispose();

		Gdx.app.exit();
	}

	public Camera getCamera() {
		return camera;
	}
}
