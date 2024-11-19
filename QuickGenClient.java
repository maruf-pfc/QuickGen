/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author maruf
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class QuickGenClient extends JFrame {
    private JComboBox<String> technologyComboBox;
    private JTextField projectNameField;
    private JTextField directoryField;
    private JButton chooseDirectoryButton;
    private JButton submitButton;
    private JTextArea resultArea;

    private static final String[] TECHNOLOGIES = {"React with JS", "React with TS", "Node.js MVC", "SCSS templates", "HTML/CSS projects"};
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public QuickGenClient() {
        setTitle("QuickGen - Project Generator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        layoutComponents();
        addListeners();

        setVisible(true);
    }

    private void initComponents() {
        technologyComboBox = new JComboBox<>(TECHNOLOGIES);
        projectNameField = new JTextField(20);
        directoryField = new JTextField(20);
        chooseDirectoryButton = new JButton("Choose Directory");
        submitButton = new JButton("Generate Project");
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Technology:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        inputPanel.add(technologyComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Project Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        inputPanel.add(projectNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Directory:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(directoryField, gbc);
        gbc.gridx = 2; gbc.gridwidth = 1;
        inputPanel.add(chooseDirectoryButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(submitButton, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        add(mainPanel);
    }

    private void addListeners() {
        chooseDirectoryButton.addActionListener(e -> chooseDirectory());
        submitButton.addActionListener(e -> submitProject());
    }

    private void chooseDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            directoryField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void submitProject() {
        String technology = (String) technologyComboBox.getSelectedItem();
        String projectName = projectNameField.getText();
        String directory = directoryField.getText();

        if (projectName.isEmpty() || directory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String response = sendToServer(technology, projectName, directory);
            resultArea.setText(response);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error communicating with server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String sendToServer(String technology, String projectName, String directory) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(technology);
            out.println(projectName);
            out.println(directory);

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuickGenClient::new);
    }
}