package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.game.utils.Bikeshed;
import com.mygdx.game.utils.BikeshedRoot;
import com.mygdx.game.utils.Event;
import com.mygdx.game.utils.Geolocation;
import com.mygdx.game.utils.MapRasterTiles;
import com.mygdx.game.utils.Mbajk;
import com.mygdx.game.utils.PixelPosition;
import com.mygdx.game.utils.Stand;
import com.mygdx.game.utils.StandRoot;
import com.mygdx.game.utils.Temperature;
import com.mygdx.game.utils.ZoomXY;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;

import java.io.IOException;

public class CyclingInMaribor extends ApplicationAdapter implements GestureDetector.GestureListener {
	private Vector3 touchPosition;
	private SpriteBatch batch;
	private SpriteBatch hudBatch;
	private BitmapFont font;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private OrthographicCamera camera;

	private Texture[] mapTiles;
	private ZoomXY beginTile;   // top left tile

	private final int NUM_TILES = 4;
	private final int ZOOM = 15;
	private final Geolocation CENTER_GEOLOCATION = new Geolocation(46.557314, 15.637771);
	private final int WIDTH = MapRasterTiles.TILE_SIZE * NUM_TILES;
	private final int HEIGHT = MapRasterTiles.TILE_SIZE * NUM_TILES;

	ObjectMapper om = new ObjectMapper();

	private Texture mbajkIcon;
	private Texture bikeshedIcon;
	private Texture eventIcon;
	private Texture standIcon;
	private Texture temperatureIcon;
	private Texture legend;

	Event[] events;
	Mbajk[] mbajks;
	StandRoot standRoot;
	BikeshedRoot bikeshedRoot;
	Temperature[] temperatures;

	boolean legendOn = false;
	boolean mbajkOn = true;
	boolean eventOn = true;
	boolean bikeshedOn = true;
	boolean temperatureOn = true;
	boolean standOn = true;

	@Override
	public void create() {

		mbajkIcon = new Texture(Gdx.files.internal("bike.png"));
		bikeshedIcon = new Texture(Gdx.files.internal("bikeshed.png"));
		eventIcon = new Texture(Gdx.files.internal("event.png"));
		standIcon = new Texture(Gdx.files.internal("stand.png"));
		legend = new Texture(Gdx.files.internal("legend.png"));
		temperatureIcon = new Texture(Gdx.files.internal("temperature.png"));

		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
		camera.viewportWidth = WIDTH / 2f;
		camera.viewportHeight = HEIGHT / 2f;
		camera.zoom = 2f;
		camera.update();

		touchPosition = new Vector3();
		Gdx.input.setInputProcessor(new GestureDetector(this));

		font = new BitmapFont();
		font.getData().setScale(1.5f);

		HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

		Net.HttpRequest eventRequest = requestBuilder.newRequest().method("GET").url("https://digitalni-dvojcek-feri.herokuapp.com/event").build();
		Gdx.net.sendHttpRequest (eventRequest, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Json json = new Json();
				String text = httpResponse.getResultAsString();
				json.setOutputType(JsonWriter.OutputType.minimal);
				//System.out.println("response: " + json.prettyPrint(text));
				events = json.fromJson(Event[].class, text);
                /*
                for (Event event : events) {
                    System.out.println("y: " + event.geometry.coordinates.get(0));
                    System.out.println("x: " + event.geometry.coordinates.get(1));
                }
                 */
			}
			@Override
			public void failed(Throwable t) {
				System.out.println("Request failed : " + t.getMessage());
			}
			@Override
			public void cancelled() {
				System.out.println("Request cancelled");
			}
		});

		Net.HttpRequest mbajkRequest = requestBuilder.newRequest().method("GET").url("https://digitalni-dvojcek-feri.herokuapp.com/mbajk").build();
		Gdx.net.sendHttpRequest (mbajkRequest, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Json json = new Json();
				String text = httpResponse.getResultAsString();
				json.setOutputType(JsonWriter.OutputType.minimal);
				//System.out.println("response: " + json.prettyPrint(text));
				mbajks = json.fromJson(Mbajk[].class, text);
                /*
                for (Mbajk mbajk: mbajks) {
                    System.out.println(mbajk.name);
                }

                 */
			}
			@Override
			public void failed(Throwable t) {
				System.out.println("Request failed : " + t.getMessage());
			}
			@Override
			public void cancelled() {
				System.out.println("Request cancelled");
			}
		});

		Net.HttpRequest bikeshedRequest = requestBuilder.newRequest().method("GET").url("https://digitalni-dvojcek-feri.herokuapp.com/bikeshed").build();
		Gdx.net.sendHttpRequest (bikeshedRequest, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Json json = new Json();
				String text = httpResponse.getResultAsString();
				json.setOutputType(JsonWriter.OutputType.minimal);
				//System.out.println("response: " + json.prettyPrint(text));
				try {
					bikeshedRoot = om.readValue(text, BikeshedRoot.class);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
                /*
                for (Bikeshed bikeshed : bikeshedRoot.bikeSheds) {
                    System.out.println(bikeshed.providerLink);
                }

                 */
			}
			@Override
			public void failed(Throwable t) {
				System.out.println("Request failed : " + t.getMessage());
			}
			@Override
			public void cancelled() {
				System.out.println("Request cancelled");
			}
		});

		final Net.HttpRequest standRequest = requestBuilder.newRequest().method("GET").url("https://digitalni-dvojcek-feri.herokuapp.com/stand").build();
		Gdx.net.sendHttpRequest (standRequest, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Json json = new Json();
				String text = httpResponse.getResultAsString();
				json.setOutputType(JsonWriter.OutputType.minimal);
				//System.out.println("response: " + json.prettyPrint(text));
				try {
					standRoot = om.readValue(text, StandRoot.class);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
                /*
                for (Stand stand : standRoot.stands) {
                    System.out.println(stand.geometry.coordinates.get(1));
                }

                 */
			}
			@Override
			public void failed(Throwable t) {
				System.out.println("Request failed : " + t.getMessage());
			}
			@Override
			public void cancelled() {
				System.out.println("Request cancelled");
			}
		});

		Net.HttpRequest temperatureRequest = requestBuilder.newRequest().method("GET").url("https://digitalni-dvojcek-feri.herokuapp.com/sensortempdata").build();
		Gdx.net.sendHttpRequest (temperatureRequest, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Json json = new Json();
				String text = httpResponse.getResultAsString();
				json.setOutputType(JsonWriter.OutputType.minimal);
				//System.out.println("response: " + json.prettyPrint(text));
				temperatures = json.fromJson(Temperature[].class, text);
                /*
                for (Temperature temp: temperatures) {
                    System.out.println(temp.temperature);
                }

                 */
			}
			@Override
			public void failed(Throwable t) {
				System.out.println("Request failed : " + t.getMessage());
			}
			@Override
			public void cancelled() {
				System.out.println("Request cancelled");
			}
		});

		try {
			//in most cases, geolocation won't be in the center of the tile because tile borders are predetermined (geolocation can be at the corner of a tile)
			ZoomXY centerTile = MapRasterTiles.getTileNumber(CENTER_GEOLOCATION.lat, CENTER_GEOLOCATION.lng, ZOOM);
			mapTiles = MapRasterTiles.getRasterTileZone(centerTile, NUM_TILES);
			//you need the beginning tile (tile on the top left corner) to convert geolocation to a location in pixels.
			beginTile = new ZoomXY(ZOOM, centerTile.x - ((NUM_TILES - 1) / 2), centerTile.y - ((NUM_TILES - 1) / 2));
		} catch (IOException e) {
			e.printStackTrace();
		}

		tiledMap = new TiledMap();
		MapLayers layers = tiledMap.getLayers();

		TiledMapTileLayer layer = new TiledMapTileLayer(NUM_TILES, NUM_TILES, MapRasterTiles.TILE_SIZE, MapRasterTiles.TILE_SIZE);
		int index = 0;
		for (int j = NUM_TILES - 1; j >= 0; j--) {
			for (int i = 0; i < NUM_TILES; i++) {
				TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
				cell.setTile(new StaticTiledMapTile(new TextureRegion(mapTiles[index], MapRasterTiles.TILE_SIZE, MapRasterTiles.TILE_SIZE)));
				layer.setCell(i, j, cell);
				index++;
			}
		}
		layers.add(layer);

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handleInput();

		camera.update();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (eventOn) {
			drawEvents();
		}
		if (mbajkOn) {
			drawMbajks();
		}
		if (bikeshedOn) {
			drawBikesheds();
		}
		if (standOn) {
			drawStands();
		}
		if (temperatureOn) {
			drawTemperatures();
		}
		batch.end();

		if (legendOn) {
			hudBatch.begin();
			hudBatch.draw(legend, Gdx.graphics.getWidth() -150, Gdx.graphics.getHeight() -900);
			if (standOn) {
				font.setColor(Color.RED);
				font.draw(hudBatch, "Number below stands shows available park spots", Gdx.graphics.getWidth()-900, Gdx.graphics.getHeight()-860);
			}
			hudBatch.end();
		}
	}

	private void drawEvents() {
		for (Event event : events) {
			PixelPosition marker = MapRasterTiles.getPixelPosition(event.geometry.coordinates.get(1), event.geometry.coordinates.get(0), MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
			batch.draw(eventIcon, marker.x, marker.y);
			if (legendOn) {
				font.setColor(Color.BLACK);
				font.draw(batch, event.description, marker.x - 120, marker.y - 10);
			}
		}
	}

	private void drawMbajks() {
		for (Mbajk mbajk : mbajks) {
			PixelPosition marker = MapRasterTiles.getPixelPosition(mbajk.geometry.coordinates.get(1), mbajk.geometry.coordinates.get(0), MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
			batch.draw(mbajkIcon, marker.x, marker.y);
			if (legendOn) {
				font.setColor(Color.BLACK);
				font.draw(batch, mbajk.name, marker.x - 120, marker.y - 10);
			}
		}
	}

	private void drawBikesheds() {
		for (Bikeshed bikeshed : bikeshedRoot.bikeSheds) {
			PixelPosition marker = MapRasterTiles.getPixelPosition(bikeshed.geometry.coordinates.get(1), bikeshed.geometry.coordinates.get(0), MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
			batch.draw(bikeshedIcon, marker.x, marker.y);
			if (legendOn) {
				font.setColor(Color.BLACK);
				font.draw(batch, bikeshed.address, marker.x - 120, marker.y - 10);
			}
		}
	}

	private void drawStands() {
		for (Stand stand : standRoot.stands) {
			PixelPosition marker = MapRasterTiles.getPixelPosition(stand.geometry.coordinates.get(1), stand.geometry.coordinates.get(0), MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
			batch.draw(standIcon, marker.x, marker.y);
			if (legendOn) {
				font.setColor(Color.BLACK);
				font.draw(batch, String.valueOf(stand.parkSpots), marker.x - 10, marker.y - 10);
			}
		}
	}

	private void drawTemperatures() {
		for (Temperature temp : temperatures) {
			PixelPosition marker = MapRasterTiles.getPixelPosition(temp.geometry.coordinates.get(1), temp.geometry.coordinates.get(0), MapRasterTiles.TILE_SIZE, ZOOM, beginTile.x, beginTile.y, HEIGHT);
			batch.draw(temperatureIcon, marker.x, marker.y);
			if (legendOn) {
				font.setColor(Color.BLACK);
				font.draw(batch, temp.temperature + "C", marker.x - 10, marker.y - 10);
			}
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		hudBatch.dispose();
		font.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		touchPosition.set(x, y, 0);
		camera.unproject(touchPosition);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		camera.translate(-deltaX, deltaY);
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		if (initialDistance >= distance)
			camera.zoom += 0.02;
		else
			camera.zoom -= 0.02;
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {

	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 3, 0);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
			legendOn = !legendOn;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			mbajkOn = !mbajkOn;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			bikeshedOn = !bikeshedOn;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			eventOn = !eventOn;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
			standOn = !standOn;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
			temperatureOn = !temperatureOn;
		}

		camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, 2f);

		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, WIDTH - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, HEIGHT - effectiveViewportHeight / 2f);
	}
}
