package gui;

import core.Environment;
import core.Position;
import entities.AbstractEntity;
import entities.LivingEntity;
import entities.animals.Animal;
import entities.animals.Deer;
import entities.animals.Lion;
import entities.animals.Rabbit;
import entities.plants.Plant;
import entities.resources.Rock;
import entities.resources.Water;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * The map panel (View). Renders the environment grid with entity icons, handles hover tooltips and click selection.
 */
public class EcoPanel extends JPanel {

    /**
     * Callback interface for entity selection events.
     */
    public interface SelectionListener {
        void onEntitySelected(AbstractEntity entity);
    }

    private final Environment environment;
    private final ImageManager imageManager;
    private AbstractEntity hoveredEntity;
    private AbstractEntity selectedEntity;
    private SelectionListener selectionListener;

    private static final int GRID_OFFSET = 5;
    private static final int PREFERRED_CELL = 45;
    private int cellSize = PREFERRED_CELL;

    public EcoPanel(Environment environment) {
        this.environment = environment;
        this.imageManager = ImageManager.getInstance();

        setBackground(new Color(30, 70, 30));
        setDoubleBuffered(true);

        ToolTipManager.sharedInstance().registerComponent(this);
        ToolTipManager.sharedInstance().setInitialDelay(300);
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateCellSize();
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleHover(e.getX(), e.getY());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredEntity != null) {
                    hoveredEntity = null;
                    repaint();
                }
            }
        });
    }

    public void setSelectionListener(SelectionListener listener) {
        this.selectionListener = listener;
    }

    @Override
    public Dimension getPreferredSize() {
        int w = GRID_OFFSET * 2 + environment.getCols() * PREFERRED_CELL;
        int h = GRID_OFFSET * 2 + environment.getRows() * PREFERRED_CELL;
        return new Dimension(w, h);
    }

    /** 
     *Triggers a repaint to reflect the latest environment state.
     */
    public void refresh() {
        repaint();
    }

    /** 
     * Clears the current selection and repaints. 
     */
    public void clearSelection() {
        selectedEntity = null;
        repaint();
    }

    public AbstractEntity getSelectedEntity() {
        return selectedEntity;
    }

    private void updateCellSize() {
        int w = getWidth() - GRID_OFFSET * 2;
        int h = getHeight() - GRID_OFFSET * 2;
        if (w <= 0 || h <= 0) {
            return;
        }
        int cw = w / environment.getCols();
        int ch = h / environment.getRows();
        cellSize = Math.max(20, Math.min(cw, ch));
    }

    /** 
     * Converts a pixel coordinate to a grid Position, or null if outside the grid. 
     */
    private Position pixelToGrid(int x, int y) {
        int col = (x - GRID_OFFSET) / cellSize;
        int row = (y - GRID_OFFSET) / cellSize;
        if (row < 0 || row >= environment.getRows() || col < 0 || col >= environment.getCols()) {
            return null;
        }
        return new Position(row, col);
    }

    private void handleHover(int x, int y) {
        Position pos = pixelToGrid(x, y);
        AbstractEntity entity = (pos != null) ? environment.getEntityAt(pos) : null;
        if (entity != hoveredEntity) {
            hoveredEntity = entity;
            repaint();
        }
    }

    /**
     * Returns the tooltip text for the cell under the mouse.
     * Swing's ToolTipManager calls this automatically on hover.
     */
    @Override
    public String getToolTipText(MouseEvent e) {
        Position pos = pixelToGrid(e.getX(), e.getY());
        if (pos == null) {
            return null;
        }
        AbstractEntity entity = environment.getEntityAt(pos);
        return entity != null ? entity.toString() : null;
    }

    private void handleClick(int x, int y) {
        Position pos = pixelToGrid(x, y);
        if (pos == null) {
            return;
        }
        AbstractEntity entity = environment.getEntityAt(pos);
        selectedEntity = entity;
        if (selectionListener != null) {
            selectionListener.onEntitySelected(entity);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getWidth() > 0) {
            updateCellSize();
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        drawGrid(g2d);
        drawEntities(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
        int rows = environment.getRows();
        int cols = environment.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = GRID_OFFSET + c * cellSize;
                int y = GRID_OFFSET + r * cellSize;
                g2d.setColor((r + c) % 2 == 0 ? new Color(40, 100, 40) : new Color(50, 120, 50));
                g2d.fillRect(x, y, cellSize, cellSize);
            }
        }

        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.setStroke(new BasicStroke(1));
        for (int c = 0; c <= cols; c++) {
            int x = GRID_OFFSET + c * cellSize;
            g2d.drawLine(x, GRID_OFFSET, x, GRID_OFFSET + rows * cellSize);
        }
        for (int r = 0; r <= rows; r++) {
            int y = GRID_OFFSET + r * cellSize;
            g2d.drawLine(GRID_OFFSET, y, GRID_OFFSET + cols * cellSize, y);
        }
    }

    private void drawEntities(Graphics2D g2d) {
        List<AbstractEntity> entities = environment.getEntities();
        for (AbstractEntity entity : entities) {
            Position pos = entity.getPosition();
            if (pos.getRow() < 0 || pos.getRow() >= environment.getRows()
                    || pos.getCol() < 0 || pos.getCol() >= environment.getCols()) {
                continue;
            }

            int x = GRID_OFFSET + pos.getCol() * cellSize;
            int y = GRID_OFFSET + pos.getRow() * cellSize;
            int pad = Math.max(2, cellSize / 10);
            int iconSize = cellSize - pad * 2;

            Image img = imageManager.getScaledEntityImage(entity, iconSize);
            if (img != null) {
                g2d.drawImage(img, x + pad, y + pad, iconSize, iconSize, null);
            } else {
                g2d.setColor(getFallbackColor(entity));
                g2d.fillOval(x + pad, y + pad, iconSize, iconSize);
                g2d.setColor(Color.BLACK);
                int fontSize = Math.max(10, cellSize / 3);
                g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
                FontMetrics fm = g2d.getFontMetrics();
                String sym = String.valueOf(entity.getSymbol());
                int sx = x + (cellSize - fm.stringWidth(sym)) / 2;
                int sy = y + (cellSize + fm.getAscent()) / 2 - 2;
                g2d.drawString(sym, sx, sy);
            }

           
            if (entity == selectedEntity) {
                g2d.setColor(new Color(30, 144, 255));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
            } else if (entity == hoveredEntity) {
                g2d.setColor(Color.YELLOW);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
            }
        }
    }

    private Color getFallbackColor(AbstractEntity entity) {
        if (entity instanceof Lion)   return new Color(255, 140, 0);
        if (entity instanceof Deer)   return new Color(100, 200, 80);
        if (entity instanceof Rabbit) return new Color(210, 170, 110);
        if (entity instanceof Plant)  return new Color(0, 180, 0);
        if (entity instanceof Water)  return new Color(60, 120, 255);
        if (entity instanceof Rock)   return Color.DARK_GRAY;
        return Color.WHITE;
    }
}
