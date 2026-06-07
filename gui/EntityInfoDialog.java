package gui;

import core.Environment;
import core.Position;
import entities.AbstractEntity;
import entities.animals.Deer;
import entities.animals.Lion;
import entities.animals.Rabbit;
import entities.plants.Plant;
import entities.plants.Flower;
import entities.plants.OakTree;
import entities.resources.Rock;
import entities.resources.Water;

import javax.swing.*;
import java.awt.*;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * "Add Entity" dialog that allows the user to create and add a new entity
 * to the ecosystem during runtime.
 */
public class EntityInfoDialog extends JDialog {

    /**
     * The environment to which new entities are added.
     */
    private final Environment environment;

    /**
     * Callback executed after a new entity is successfully added.
     */
    private final Runnable onEntityAdded;

    /**
     * Combo box for selecting the entity type.
     */
    private JComboBox<String> typeCombo;

    /**
     * Spinner for selecting the row position.
     */
    private JSpinner rowSpinner;

    /**
     * Spinner for selecting the column position.
     */
    private JSpinner colSpinner;

    /**
     * Label used to display validation or error messages.
     */
    private JLabel messageLabel;

    /**
     * Creates a modal dialog for adding a new entity to the environment.
     * @param owner the parent frame of the dialog
     * @param environment the environment to update
     * @param onEntityAdded callback to run after a successful addition
     */
    public EntityInfoDialog(Frame owner, Environment environment, Runnable onEntityAdded) {
        super(owner, "Add Entity", true);
        this.environment = environment;
        this.onEntityAdded = onEntityAdded;
        buildUI();
    }

    /**
     * Builds and initializes the dialog user interface.
     */
    private void buildUI() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(340, 250);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Entity Type:"), gbc);
        typeCombo = new JComboBox<>(new String[]{"Deer", "Rabbit", "Lion", "Flower", "OakTree", "Rock", "Water"});
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(typeCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Row (0 – " + (environment.getRows() - 1) + "):"), gbc);
        rowSpinner = new JSpinner(new SpinnerNumberModel(0, 0, environment.getRows() - 1, 1));
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(rowSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Column (0 – " + (environment.getCols() - 1) + "):"), gbc);
        colSpinner = new JSpinner(new SpinnerNumberModel(0, 0, environment.getCols() - 1, 1));
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(colSpinner, gbc);

        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(messageLabel, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addBtn = new JButton("Add");
        JButton cancelBtn = new JButton("Cancel");
        addBtn.addActionListener(e -> doAdd());
        cancelBtn.addActionListener(e -> dispose());
        buttons.add(addBtn);
        buttons.add(cancelBtn);

        gbc.gridy = 4;
        panel.add(buttons, gbc);

        add(panel);
    }

    /**
     * Validates the selected input, creates the selected entity,
     * and adds it to the environment if possible.
     */
    private void doAdd() {
        int row = (Integer) rowSpinner.getValue();
        int col = (Integer) colSpinner.getValue();
        Position pos = new Position(row, col);

        if (!environment.isPositionFree(pos)) {
            messageLabel.setText("Position (" + row + ", " + col + ") is already occupied.");
            return;
        }

        AbstractEntity entity = makeEntity((String) typeCombo.getSelectedItem(), pos);
        if (entity == null || !environment.addEntity(entity)) {
            messageLabel.setText("Failed to add entity. Try a different position.");
            return;
        }

        if (onEntityAdded != null) {
            onEntityAdded.run();
        }
        dispose();
    }

    /**
     * Creates a new entity instance according to the selected type and position.
     * @param type the selected entity type
     * @param pos the position for the new entity
     * @return the created entity, or null if the type is invalid
     */
    private AbstractEntity makeEntity(String type, Position pos) {
        switch (type) {
            case "Deer":    return new Deer(pos);
            case "Rabbit":  return new Rabbit(pos);
            case "Lion":    return new Lion(pos);
            case "Flower":  return new Flower(pos);
            case "OakTree": return new OakTree(pos);
            case "Rock":    return new Rock(pos);
            case "Water":   return new Water(pos);
            case "Plant":   return new Plant(pos);
            default:        return null;
        }
    }
}