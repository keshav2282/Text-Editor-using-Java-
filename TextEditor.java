import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener {

    private JTextArea textArea;
    private JButton openButton;
    private JButton saveButton;
    private JButton renameButton;
    private JButton clearButton;
    private JFileChooser fileChooser;
    private FileNameExtensionFilter fileFilter;
    private JTextField renameField;
    private File currentFile;

    public TextEditor() {
        super("Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // create the text area
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // create the buttons
        openButton = new JButton("Open");
        openButton.addActionListener(this);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        renameButton = new JButton("Rename");
        renameButton.addActionListener(this);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(renameButton);
        buttonPanel.add(clearButton);
        Border buttonBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        buttonPanel.setBorder(buttonBorder);
        add(buttonPanel, BorderLayout.SOUTH);

        // create the rename field
        renameField = new JTextField(20);
        JPanel renamePanel = new JPanel();
        renamePanel.add(new JLabel("Rename to:"));
        renamePanel.add(renameField);
        Border renameBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        renamePanel.setBorder(renameBorder);
        renamePanel.setVisible(false);
        add(renamePanel, BorderLayout.NORTH);

        // create the file chooser
        fileChooser = new JFileChooser();
        fileFilter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(fileFilter);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    FileReader reader = new FileReader(fileChooser.getSelectedFile());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    textArea.setText("");
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        textArea.append(line + "\n");
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                    reader.close();
                    currentFile = fileChooser.getSelectedFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == saveButton) {
            if (currentFile == null) {
                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        FileWriter writer                    = new FileWriter(fileChooser.getSelectedFile());
                    writer.write(textArea.getText());
                    writer.close();
                    currentFile = fileChooser.getSelectedFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            try {
                FileWriter writer = new FileWriter(currentFile);
                writer.write(textArea.getText());
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    } else if (e.getSource() == renameButton) {
        if (currentFile == null) {
            JOptionPane.showMessageDialog(this, "Please save the file before renaming.");
        } else {
            renameField.setText(currentFile.getName());
            int option = JOptionPane.showConfirmDialog(this, renameField, "Rename", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String newFilename = renameField.getText();
                File newFile = new File(currentFile.getParentFile(), newFilename);
                if (currentFile.renameTo(newFile)) {
                    JOptionPane.showMessageDialog(this, "File renamed successfully.");
                    currentFile = newFile;
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to rename file.");
                }
            }
        }
    } else if (e.getSource() == clearButton) {
        textArea.setText("");
        currentFile = null;
    }
}

public static void main(String[] args) {
    TextEditor editor = new TextEditor();
    editor.setVisible(true);
}
}
