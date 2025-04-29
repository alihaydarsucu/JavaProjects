package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import Farm.*;

public class FarmSimulatorUI {

    private JFrame frame;
    private JTable animalTable;
    private DefaultTableModel tableModel;
    private final Farm farm;

    public FarmSimulatorUI() {
        farm = new Farm();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Farm Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        
        frame.getContentPane().setBackground(new Color(0xFF7F00)); // Bright orange

       
        String[] columnNames = {"Age", "Weight", "Owner", "Specie", "State", "Meat Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
        animalTable = new JTable(tableModel);
        refreshTable();

        
        animalTable.setBackground(new Color(0xFFFF66));
        animalTable.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        animalTable.setRowHeight(30);

  
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 20, 20)); 
        buttonPanel.setBackground(new Color(0xFFE4B5));  

    
        JButton addAnimalButton = createButton("Add Animal");
        JButton feedAnimalButton = createButton("Feed Animal");
        JButton killAnimalButton = createButton("Kill Animal");
        JButton processMeatButton = createButton("Process Meat");

        buttonPanel.add(addAnimalButton);
        buttonPanel.add(feedAnimalButton);
        buttonPanel.add(killAnimalButton);
        buttonPanel.add(processMeatButton);

     
        frame.add(new JScrollPane(animalTable), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addAnimalButton.addActionListener(e -> addAnimal());
        feedAnimalButton.addActionListener(e -> feedAnimal());
        killAnimalButton.addActionListener(e -> killAnimal());
        processMeatButton.addActionListener(e -> processMeat());

        
        JLabel header = new JLabel("Farm Simulator", JLabel.CENTER);
        header.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        header.setForeground(new Color(0x006400)); 
        header.setBackground(new Color(0x98FB98)); 
        header.setOpaque(true);
        frame.add(header, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        button.setBackground(new Color(0xFF4500));  // Red-Orange button
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        button.setPreferredSize(new Dimension(200, 60));
        return button;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Animal animal : farm.getAnimals()) {
            tableModel.addRow(new Object[]{
                animal.getAge(),
                animal.getWeight(),
                animal.getOwner(),
                animal.getSpecie().getSpecie(),
                animal.isAlive() ? "Alive" : "Dead",
                animal.getMeatType() == null ? "None" : animal.getMeatType().toString()
            });
        }
    }

    private void addAnimal() {
        String age = JOptionPane.showInputDialog("Enter age:");
        String weight = JOptionPane.showInputDialog("Enter weight:");
        String owner = JOptionPane.showInputDialog("Enter owner name:");
        String[] species = {"Sheep", "Cow", "Calf"};
        String specie = (String) JOptionPane.showInputDialog(null, "Select species:", "Animal Type", JOptionPane.QUESTION_MESSAGE, null, species, species[0]);
        if (age != null && weight != null && owner != null && specie != null) {
            farm.addAnimal(Integer.parseInt(age), Integer.parseInt(weight), owner, Specie.valueOf(specie.toUpperCase()));
            refreshTable();
        }
    }


    private void feedAnimal() {
        int selectedRow = animalTable.getSelectedRow();
        if (selectedRow != -1) {
            Animal selectedAnimal = farm.getAnimals().get(selectedRow);
            int foodAmount = 10; 
            farm.feedAnimal(selectedAnimal, foodAmount);
            refreshTable();  
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an animal to feed.");
        }
    }


    private void killAnimal() {
        int selectedRow = animalTable.getSelectedRow();
        if (selectedRow != -1) {
            Animal selectedAnimal = farm.getAnimals().get(selectedRow);
            Butcher butcher = farm.getButcher();
            selectedAnimal.kill(butcher);
            refreshTable();  
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an animal to kill.");
        }
    }


    private void processMeat() {
        int selectedRow = animalTable.getSelectedRow();
        if (selectedRow != -1) {
            Animal selectedAnimal = farm.getAnimals().get(selectedRow);
            farm.processAnimal(selectedAnimal);
            refreshTable();  
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an animal to process.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FarmSimulatorUI::new);
    }
}
