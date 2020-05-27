package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application implements EventHandler<ActionEvent> {
	// Speed settings
	private final int ONE_X = 500;
	private final int TWO_X = 250;
	private final int THREE_X = 100;

	// Draw modes
	private final int DRAW_MODE = 0;
	private final int ERASE_MODE = 1;

	// Color collections
	private final Color[] cellColors = { Color.rgb(20, 40, 80), Color.rgb(39, 73, 109), Color.rgb(12, 123, 147),
			Color.rgb(0, 168, 204) };
	private final Color BUTTON_COLOR = Color.rgb(34, 40, 49);
	private final Color GAME_CONTROL_COLOR = Color.rgb(0, 168, 204);
	private final Color DRAW_CONTROL_COLOR = Color.HOTPINK;
	private final Color SPEED_CONTROL_COLOR = Color.YELLOW;
	private final Color EXIT_BUTTON_COLOR = Color.rgb(255, 115, 115);
	private final Color DEAD_CELL_COLOR = Color.rgb(190, 190, 190);
	private final Color CANVAS_BORDER_COLOR = Color.rgb(224, 210, 189);

	// Font
	private final Font font = new Font("Courier new", 20);

	private static final double WINDOW_HEIGHT = 900;
	private static final double WINDOW_WIDTH = 900;
	public static int cellSize = 5;
	private int canvasSize = 654;
	private boolean running;
	private int numCells;
	private int mode;

	Canvas canvas;
	GraphicsContext conny;
	Image logo;
	ImageView logoimg;

	// Game controls
	Button next;
	Button start;
	Button clear;
	HBox gameControls;

	// Speed settings controls
	Button x;
	Button xx;
	Button xxx;
	HBox speedyControls;

	// Draw controls
	Button draw;
	Button erase;
	HBox drawControls;

	ToolBar tools;
	BorderPane pane;
	Scene main;
	Text gen;
	Button exit;
	VBox top;
	Game life;
	Timeline line;

	/**
	 * Start method for JavaFX. Initializes all of the controls and adds them to the
	 * correct places, sets up the game, creates event handlers.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Calculate numCells immediately
		numCells = canvasSize / cellSize;

		/*
		 * Creating canvas and getting graphics context
		 */
		canvas = new Canvas(canvasSize, canvasSize);
		conny = canvas.getGraphicsContext2D();

		/*
		 * Creating members of the top
		 */
		logo = new Image("game_logo_2.png");
		logoimg = new ImageView(logo);
		gen = new Text();
		gen.setFill(Color.rgb(222, 224, 255));
		gen.setFont(font);
		top = new VBox(0); // container for the top
		top.getChildren().addAll(logoimg, gen);
		top.setAlignment(Pos.CENTER);

		/*
		 * Creating the game controls
		 */
		next = new Button("Generate");
		next.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
		next.setTextFill(GAME_CONTROL_COLOR);
		start = new Button("Start");
		start.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
		start.setTextFill(GAME_CONTROL_COLOR);
		clear = new Button("Clear");
		clear.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
		clear.setTextFill(GAME_CONTROL_COLOR);
		gameControls = new HBox(1);
		gameControls.getChildren().addAll(start, next, clear);
		gameControls.setAlignment(Pos.CENTER);

		/*
		 * Creating the speed controls
		 */
		x = new Button("1X");
		x.setBackground(new Background(new BackgroundFill(SPEED_CONTROL_COLOR, new CornerRadii(2), Insets.EMPTY)));
		x.setTextFill(BUTTON_COLOR);
		xx = new Button("2X");
		xx.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
		xx.setTextFill(SPEED_CONTROL_COLOR);
		xxx = new Button("3X");
		xxx.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
		xxx.setTextFill(SPEED_CONTROL_COLOR);
		speedyControls = new HBox(1);
		speedyControls.getChildren().addAll(x, xx, xxx);
		speedyControls.setAlignment(Pos.CENTER);

		/*
		 * Creating the draw controls
		 */
		draw = new Button("Draw");
		draw.setBackground(new Background(new BackgroundFill(DRAW_CONTROL_COLOR, new CornerRadii(2), Insets.EMPTY)));
		draw.setTextFill(BUTTON_COLOR); // start out in draw mode -> draw button must be selected
		erase = new Button("Erase");
		erase.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
		erase.setTextFill(DRAW_CONTROL_COLOR);
		drawControls = new HBox(1);
		drawControls.getChildren().addAll(draw, erase);
		drawControls.setAlignment(Pos.CENTER);

		/*
		 * Creating the exit button
		 */
		exit = new Button("Exit");
		exit.setBackground(new Background(new BackgroundFill(EXIT_BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));

		/*
		 * Creating spacers for tool bar and adding all control groups to the toolbar
		 */
		HBox spacer1 = new HBox();
		HBox.setHgrow(spacer1, Priority.ALWAYS);
		HBox spacer2 = new HBox();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		tools = new ToolBar(spacer1, drawControls, gameControls, speedyControls, exit, spacer2);
		tools.setBackground(
				new Background(new BackgroundFill(Color.rgb(65, 65, 65), new CornerRadii(2), Insets.EMPTY)));
		tools.setBorder(new Border(new BorderStroke(BUTTON_COLOR, BorderStrokeStyle.SOLID, null, null)));

		/*
		 * Create game, timeline for running the graphics
		 */
		life = new Game(canvasSize - 4, canvasSize - 4);
		line = new Timeline();
		line.getKeyFrames().add(new KeyFrame(Duration.millis(ONE_X), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				life.nextGen();
				drawBoard();

			}
		}));
		line.setCycleCount(Animation.INDEFINITE);
		running = false; // remembers if running the game

		/*
		 * Creating the main container (pane). Adds background image to pane. Creates
		 * the main scene. Sets primary stage
		 */
		pane = new BorderPane(canvas, top, null, tools, null);
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
		Image image = new Image("background_3.png");

		Background back = new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.CENTER, bSize));
		pane.setBackground(back);
		main = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
		drawBoard();
		primaryStage.setHeight(WINDOW_HEIGHT);
		primaryStage.setWidth(WINDOW_WIDTH);
		primaryStage.setScene(main);
		primaryStage.show();

		/*
		 * Event handlers/registrations
		 */
		EventHandler<MouseEvent> mouser = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				int x = (int) (arg0.getX() / cellSize);
				int y = (int) (arg0.getY() / cellSize);
				if (x < 0 || x > numCells - 1 || y < 0 || y > numCells - 1) {
					return;
				}

				if (mode == DRAW_MODE) {
					if (!life.getBoard().get(x).get(y).isAlive()) {
						life.getBoard().get(x).get(y).setAlive(true);
					}
				} else if (mode == ERASE_MODE) {
					if (life.getBoard().get(x).get(y).isAlive()) {
						life.getBoard().get(x).get(y).setAlive(false);
					}
				}
				drawBoard();
			}
		};
		canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, mouser); // Mouse events use mouser
		canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouser);
		// Registers all buttons
		next.setOnAction(this);
		start.setOnAction(this);
		clear.setOnAction(this);
		x.setOnAction(this);
		xx.setOnAction(this);
		xxx.setOnAction(this);
		erase.setOnAction(this);
		draw.setOnAction(this);
		exit.setOnAction(this);
	}

	/**
	 * Draws the current board onto the Canvas. Chooses color of live cells base off
	 * of their age, draws grid lines and border around the Canvas.
	 */
	private void drawBoard() {
		gen.setText("Generation: " + life.getGen()); // Updates generation text
		conny.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clears the canvas

		// drawing the actual cells
		for (int i = 1; i < numCells; i++) {
			for (int j = 1; j < numCells; j++) {
				Cell curr = life.getBoard().get(i).get(j); // gets current cell
				conny.setFill(DEAD_CELL_COLOR);
				if (curr.isAlive()) {
					int color = curr.getAge() - 2; // cell colors increment by age of 2
					if (color < 2) { // Choose color of live cell
						conny.setFill(cellColors[0]);
					} else if (color < 4) {
						conny.setFill(cellColors[1]);
					} else if (color < 6) {
						conny.setFill(cellColors[2]);
					} else {
						conny.setFill(cellColors[3]);
					}
				}
				// Draw the current cell
				conny.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
			}
		}

		// After drawing cells, draw the grid
		conny.setStroke(Color.rgb(80, 80, 80));
		conny.setLineWidth(0.5); // reset the stroke thickness

		// drawing the grid columns
		for (int i = 1; i < numCells; i++) {
			conny.strokeLine(i * cellSize, 0, i * cellSize, numCells * cellSize);
		}

		// drawing the grid rows
		for (int i = 1; i < numCells; i++) {
			conny.strokeLine(0, i * cellSize, numCells * cellSize, i * cellSize);
		}

		/*
		 * Drawing border around canvas
		 */
		conny.setStroke(CANVAS_BORDER_COLOR);
		conny.setLineWidth(8);
		conny.strokeLine(1, 1, canvas.getWidth(), 1);
		conny.strokeLine(1, 1, 1, canvas.getHeight());
		conny.strokeLine(1, canvas.getHeight(), canvas.getWidth(), canvas.getHeight());
		conny.strokeLine(canvas.getWidth(), 1, canvas.getWidth(), canvas.getHeight());

	}

	/**
	 * Event handler for all of the buttons
	 */
	@Override
	public void handle(ActionEvent arg0) {
		if (arg0.getSource() == next) {
			life.nextGen();
			drawBoard();
		} else if (arg0.getSource() == start) {
			if (running) {
				// timer.cancel();
				start.setText("Start");
				running = false;
				line.stop();
			} else {
				start.setText("Stop");
				running = true;
				line.play();
			}
		} else if (arg0.getSource() == clear) {
			if (running) {
				line.stop();
				running = false;
				start.setText("Start");
			}
			life = new Game(numCells, numCells);
			drawBoard();
		} else if (arg0.getSource() == x) {
			x.setBackground(new Background(new BackgroundFill(SPEED_CONTROL_COLOR, new CornerRadii(2), Insets.EMPTY)));
			x.setTextFill(BUTTON_COLOR);
			// Change erase button back to normal
			xx.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
			xx.setTextFill(SPEED_CONTROL_COLOR);
			xxx.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
			xxx.setTextFill(SPEED_CONTROL_COLOR);
			if (running) {
				line.stop();
			}
			line = new Timeline();
			line.getKeyFrames().add(new KeyFrame(Duration.millis(ONE_X), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					life.nextGen();
					drawBoard();

				}
			}));
			line.setCycleCount(Animation.INDEFINITE);
			if (running) {
				line.play();
			}
		} else if (arg0.getSource() == xx) {
			xx.setBackground(new Background(new BackgroundFill(SPEED_CONTROL_COLOR, new CornerRadii(2), Insets.EMPTY)));
			xx.setTextFill(BUTTON_COLOR);
			// Change erase button back to normal
			x.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
			x.setTextFill(SPEED_CONTROL_COLOR);
			xxx.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
			xxx.setTextFill(SPEED_CONTROL_COLOR);
			if (running) {
				line.stop();
			}
			line = new Timeline();
			line.getKeyFrames().add(new KeyFrame(Duration.millis(TWO_X), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					life.nextGen();
					drawBoard();

				}
			}));
			line.setCycleCount(Animation.INDEFINITE);
			if (running) {
				line.play();
			}
		} else if (arg0.getSource() == xxx) {
			xxx.setBackground(
					new Background(new BackgroundFill(SPEED_CONTROL_COLOR, new CornerRadii(2), Insets.EMPTY)));
			xxx.setTextFill(BUTTON_COLOR);
			// Change erase button back to normal
			xx.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
			xx.setTextFill(SPEED_CONTROL_COLOR);
			x.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
			x.setTextFill(SPEED_CONTROL_COLOR);
			if (running) {
				line.stop();
			}
			line = new Timeline();
			line.getKeyFrames().add(new KeyFrame(Duration.millis(THREE_X), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					life.nextGen();
					drawBoard();

				}
			}));
			line.setCycleCount(Animation.INDEFINITE);
			if (running) {
				line.play();
			}
		} else if (arg0.getSource() == exit) {
			Platform.exit();
			System.exit(0);
		} else if (arg0.getSource() == draw) {
			this.mode = DRAW_MODE;
			draw.setBackground(
					new Background(new BackgroundFill(DRAW_CONTROL_COLOR, new CornerRadii(2), Insets.EMPTY)));
			draw.setTextFill(BUTTON_COLOR);
			// Change erase button back to normal
			erase.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
			erase.setTextFill(DRAW_CONTROL_COLOR);
		} else if (arg0.getSource() == erase) {
			erase.setBackground(
					new Background(new BackgroundFill(DRAW_CONTROL_COLOR, new CornerRadii(2), Insets.EMPTY)));
			erase.setTextFill(BUTTON_COLOR);
			// Change draw button colors back to normal
			draw.setBackground(new Background(new BackgroundFill(BUTTON_COLOR, new CornerRadii(2), Insets.EMPTY)));
			draw.setTextFill(DRAW_CONTROL_COLOR);
			this.mode = ERASE_MODE;
		}

	}

	/**
	 * Main method, launches the JavaFX application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		launch(args);
	}
}
