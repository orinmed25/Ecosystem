package gui;

import entities.AbstractEntity;
import entities.LivingEntity;

import javax.swing.*;
import java.awt.*;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Permanent side panel that displays detailed information about the currently selected entity.
 * The panel presents general entity data, energy status, and the entity's toString output.
 */
public class EntityInfoPanel extends JPanel {

    /**
     * Label displaying the selected entity type.
     */
    private final JLabel typeLabel;

    /**
     * Label displaying the selected entity position.
     */
    private final JLabel positionLabel;

    /**
     * Label displaying the selected entity alive/dead status.
     */
    private final JLabel statusLabel;

    /**
     * Label displaying the selected entity age.
     */
    private final JLabel ageLabel;

    /**
     * Label displaying the selected entity energy.
     */
    private final JLabel energyLabel;

    /**
     * Progress bar displaying the selected entity energy percentage.
     */
    private final JProgressBar energyBar;

    /**
     * Text area displaying the selected entity's toString output.
     */
    private final JTextArea toStringArea;

    /**
     * Creates the information panel and initializes all GUI components.
     */
    public EntityInfoPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(230, 0));
        setBackground(new Color(248, 248, 250));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel title = new JLabel("Entity Details");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(50, 50, 80));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        typeLabel     = field("Type: —");
        positionLabel = field("Position: —");
        statusLabel   = field("Status: —");
        ageLabel      = field("Age: —");
        energyLabel   = field("Energy: —");

        energyBar = new JProgressBar(0, 100);
        energyBar.setStringPainted(true);
        energyBar.setString("—");
        energyBar.setAlignmentX(LEFT_ALIGNMENT);
        energyBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        toStringArea = new JTextArea(4, 18);
        toStringArea.setEditable(false);
        toStringArea.setLineWrap(true);
        toStringArea.setWrapStyleWord(true);
        toStringArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        toStringArea.setBackground(new Color(235, 235, 240));
        toStringArea.setText("Click an entity on the\nmap to see details.");
        JScrollPane scroll = new JScrollPane(toStringArea);
        scroll.setAlignmentX(LEFT_ALIGNMENT);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel tsTitle = field("toString() output:");
        tsTitle.setFont(new Font("Arial", Font.BOLD, 11));

        content.add(typeLabel);
        content.add(gap(4));
        content.add(positionLabel);
        content.add(gap(4));
        content.add(statusLabel);
        content.add(gap(4));
        content.add(ageLabel);
        content.add(gap(8));
        content.add(separator());
        content.add(gap(8));
        content.add(energyLabel);
        content.add(gap(4));
        content.add(energyBar);
        content.add(gap(8));
        content.add(separator());
        content.add(gap(8));
        content.add(tsTitle);
        content.add(gap(4));
        content.add(scroll);

        add(content, BorderLayout.CENTER);
    }

    /**
     * Creates a formatted label for the information panel.
     * @param text the text to display in the label
     * @return a configured JLabel
     */
    private JLabel field(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    /**
     * Creates a vertical gap component with the given height.
     * @param h the gap height in pixels
     * @return a vertical spacing component
     */
    private Component gap(int h) {
        return Box.createVerticalStrut(h);
    }

    /**
     * Creates a separator component for visual division inside the panel.
     * @return a configured separator
     */
    private JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setAlignmentX(LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    /**
     * Updates the panel to display the details of the given entity.
     * If the entity is null, the panel displays default placeholder values.
     * @param entity the entity to display, or null if no entity is selected
     */
    public void showEntity(AbstractEntity entity) {
        if (entity == null) {
            typeLabel.setText("Type: —");
            positionLabel.setText("Position: —");
            statusLabel.setText("Status: —");
            ageLabel.setText("Age: —");
            energyLabel.setText("Energy: —");
            energyBar.setValue(0);
            energyBar.setString("—");
            toStringArea.setText("Click an entity on the\nmap to see details.");
            return;
        }

        typeLabel.setText("Type: " + entity.getClass().getSimpleName());
        positionLabel.setText("Position: " + entity.getPosition());
        statusLabel.setText("Status: " + (entity.isAlive() ? "Alive" : "Dead"));
        toStringArea.setText(entity.toString());

        if (entity instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) entity;
            ageLabel.setText("Age: " + le.getAge());
            energyLabel.setText(String.format("Energy: %.1f / %.1f", le.getEnergy(), le.getMaxEnergy()));
            int pct = (le.getMaxEnergy() > 0)
                    ? (int) ((le.getEnergy() / le.getMaxEnergy()) * 100)
                    : 0;
            energyBar.setValue(pct);
            energyBar.setString(pct + "%");
            if (pct > 60) {
                energyBar.setForeground(new Color(0, 170, 0));
            } else if (pct > 30) {
                energyBar.setForeground(Color.ORANGE);
            } else {
                energyBar.setForeground(Color.RED);
            }
        } else {
            ageLabel.setText("Age: —");
            energyLabel.setText("Energy: N/A");
            energyBar.setValue(0);
            energyBar.setString("N/A");
        }
    }
}