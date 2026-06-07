package gui;

import core.Environment;
import core.Position;
import core.SimulationEngine;
import entities.AbstractEntity;
import entities.LivingEntity;
import entities.animals.Deer;
import entities.animals.Lion;
import entities.animals.Rabbit;
import entities.plants.Flower;
import entities.plants.OakTree;
import entities.resources.Rock;
import entities.resources.Water;
import interfaces.EcosystemObserver;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 *
 * Main application frame that connects the ecosystem model to the graphical user interface.
 * This class manages the main window, simulation controls, statistics display, and observer updates.
 */
public class EcoFrame extends JFrame implements EcosystemObserver {

    /**
     * Default number of rows in the environment.
     */
    private static final int ENV_ROWS = 9;

    /**
     * Default number of columns in the environment.
     */
    private static final int ENV_COLS = 9;

    /**
     * Delay in milliseconds between automatic simulation ticks.
     */
    private static final int AUTO_TICK_MS = 600;

    /**
     * The ecosystem environment displayed in the frame.
     */
    private final Environment environment;

    /**
     * The simulation engine responsible for advancing the simulation.
     */
    private final SimulationEngine engine;

    /**
     * The panel used to display the ecosystem map.
     */
    private final EcoPanel ecoPanel;

    /**
     * The panel used to display information about the selected entity.
     */
    private final EntityInfoPanel infoPanel;

    /**
     * Label displaying the current tick count.
     */
    private JLabel tickLabel;

    /**
     * Label displaying the number of lions.
     */
    private JLabel lionLabel;

    /**
     * Label displaying the number of deer.
     */
    private JLabel deerLabel;

    /**
     * Label displaying the number of rabbits.
     */
    private JLabel rabbitLabel;

    /**
     * Label displaying the number of flowers.
     */
    private JLabel flowerLabel;

    /**
     * Label displaying the number of oak trees.
     */
    private JLabel oakLabel;

    /**
     * Label displaying the total energy in the simulation.
     */
    private JLabel energyLabel;

    /**
     * Timer used for automatic simulation progression.
     */
    private final Timer autoTimer;

    /**
     * Button used to start continuous simulation.
     */
    private JButton playButton;

    /**
     * Button used to pause continuous simulation.
     */
    private JButton pauseButton;

    /**
     * Constructs the main application frame, initializes the model,
     * registers the observer, creates the GUI components, and displays the window.
     */
    public EcoFrame() {
        setTitle("Ecosystem Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 500));

        environment = new Environment(ENV_ROWS, ENV_COLS);
        engine = new SimulationEngine(environment);
        engine.addObserver(this);

        populateInitialEntities();

        infoPanel = new EntityInfoPanel();
        ecoPanel = new EcoPanel(environment);
        ecoPanel.setSelectionListener(entity -> infoPanel.showEntity(entity));

        setLayout(new BorderLayout());
        setupMenuBar();
        add(ecoPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        autoTimer = new Timer(AUTO_TICK_MS, e -> engine.tick());

        updateStats();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Adds the initial entities to the environment when the application starts.
     */
    private void populateInitialEntities() {
        environment.addEntity(new Lion(new Position(0, 0)));
        environment.addEntity(new Lion(new Position(0, 8)));
        environment.addEntity(new Deer(new Position(2, 2)));
        environment.addEntity(new Deer(new Position(2, 6)));
        environment.addEntity(new Deer(new Position(5, 9)));
        environment.addEntity(new Rabbit(new Position(1, 4)));
        environment.addEntity(new Rabbit(new Position(4, 1)));
        environment.addEntity(new Rabbit(new Position(6, 7)));
        environment.addEntity(new Flower(new Position(7, 0)));
        environment.addEntity(new Flower(new Position(7, 4)));
        environment.addEntity(new Flower(new Position(3, 2)));
        environment.addEntity(new OakTree(new Position(1, 5)));
        environment.addEntity(new OakTree(new Position(6, 3)));
        environment.addEntity(new Water(new Position(5, 4)));
        environment.addEntity(new Rock(new Position(3, 5)));
        environment.addEntity(new Rock(new Position(1, 8)));
    }

    /**
     * Creates and assigns the menu bar for the frame.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu simMenu = new JMenu("Simulation");
        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(e -> doReset());
        simMenu.add(resetItem);

        menuBar.add(fileMenu);
        menuBar.add(simMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Builds the bottom panel containing the control panel and the statistics panel.
     * @return the bottom panel
     */
    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(45, 45, 55));
        bottom.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        bottom.add(buildControlPanel(), BorderLayout.WEST);
        bottom.add(buildStatsPanel(), BorderLayout.EAST);

        return bottom;
    }

    /**
     * Builds the simulation control panel with buttons for step, play, pause, add entity, and reset.
     * @return the control panel
     */
    private JPanel buildControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        JButton stepButton = styledButton("Step", new Color(70, 130, 180));
        playButton = styledButton("▶  Play", new Color(50, 160, 50));
        pauseButton = styledButton("⏸  Pause", new Color(200, 140, 0));
        JButton addButton = styledButton("＋ Add Entity", new Color(130, 60, 160));
        JButton resetButton = styledButton("⟲  Reset", new Color(180, 60, 60));

        pauseButton.setEnabled(false);

        stepButton.addActionListener(e -> engine.tick());
        playButton.addActionListener(e -> doPlay());
        pauseButton.addActionListener(e -> doPause());
        addButton.addActionListener(e -> doAddEntity());
        resetButton.addActionListener(e -> doReset());

        panel.add(stepButton);
        panel.add(playButton);
        panel.add(pauseButton);
        panel.add(addButton);
        panel.add(resetButton);

        return panel;
    }

    /**
     * Builds the statistics panel that displays simulation data.
     * @return the statistics panel
     */
    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 2));
        panel.setOpaque(false);

        tickLabel = statLabel("Tick: 0");
        lionLabel = statLabel("Lions: 0");
        deerLabel = statLabel("Deer: 0");
        rabbitLabel = statLabel("Rabbits: 0");
        flowerLabel = statLabel("Flowers: 0");
        oakLabel = statLabel("OakTrees: 0");
        energyLabel = statLabel("Total Energy: 0");

        panel.add(tickLabel);
        panel.add(divider());
        panel.add(lionLabel);
        panel.add(deerLabel);
        panel.add(rabbitLabel);
        panel.add(flowerLabel);
        panel.add(oakLabel);
        panel.add(divider());
        panel.add(energyLabel);

        return panel;
    }

    /**
     * Creates a styled button with the given text and background color.
     * @param text the button text
     * @param bg the background color
     * @return the styled button
     */
    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Creates a styled label for displaying statistics.
     * @param text the label text
     * @return the styled statistics label
     */
    private JLabel statLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        return lbl;
    }

    /**
     * Creates a divider label used between statistics labels.
     * @return the divider label
     */
    private JLabel divider() {
        JLabel d = new JLabel("|");
        d.setForeground(new Color(110, 110, 120));
        return d;
    }

    /**
     * Starts continuous automatic simulation updates.
     */
    private void doPlay() {
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
        autoTimer.start();
    }

    /**
     * Pauses continuous automatic simulation updates.
     */
    private void doPause() {
        autoTimer.stop();
        playButton.setEnabled(true);
        pauseButton.setEnabled(false);
    }

    /**
     * Resets the simulation to its initial state and repopulates the environment.
     */
    private void doReset() {
        doPause();
        engine.reset();
        populateInitialEntities();
        ecoPanel.clearSelection();
        infoPanel.showEntity(null);
        updateStats();
        ecoPanel.refresh();
    }

    /**
     * Opens the dialog for adding a new entity to the environment.
     */
    private void doAddEntity() {
        new EntityInfoDialog(this, environment, () -> {
            updateStats();
            ecoPanel.refresh();
        }).setVisible(true);
    }

    /**
     * Updates the GUI when the world changes.
     * @param env the updated environment
     * @param tickCount the current tick count
     */
    @Override
    public void onWorldChanged(Environment env, int tickCount) {
        SwingUtilities.invokeLater(() -> {
            updateStats();
            ecoPanel.refresh();
        });
    }

    /**
     * Recalculates and updates all statistics labels according to the current environment state.
     */
    private void updateStats() {
        List<AbstractEntity> entities = environment.getEntities();
        int lions = 0, deer = 0, rabbits = 0, flowers = 0, oaks = 0;
        double totalEnergy = 0;

        for (AbstractEntity e : entities) {
            if (e instanceof Lion) lions++;
            else if (e instanceof Deer) deer++;
            else if (e instanceof Rabbit) rabbits++;
            else if (e instanceof Flower) flowers++;
            else if (e instanceof OakTree) oaks++;

            if (e instanceof LivingEntity) {
                totalEnergy += ((LivingEntity) e).getEnergy();
            }
        }

        tickLabel.setText("Tick: " + engine.getTickCount());
        lionLabel.setText("Lions: " + lions);
        deerLabel.setText("Deer: " + deer);
        rabbitLabel.setText("Rabbits: " + rabbits);
        flowerLabel.setText("Flowers: " + flowers);
        oakLabel.setText("OakTrees: " + oaks);
        energyLabel.setText(String.format("Total Energy: %.0f", totalEnergy));
    }

    /**
     * Launches the ecosystem application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EcoFrame::new);
    }
}