import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Notepad extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private boolean textChanged;

    public Notepad() {
        setTitle("Java Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textChanged = true;
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        fileChooser = new JFileChooser();
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(e -> newFile());
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> openFile());
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> saveFile());
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(e -> saveAsFile());
        fileMenu.add(saveAsMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> exit());
        fileMenu.add(exitMenuItem);

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        JMenuItem cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.addActionListener(e -> textArea.cut());
        editMenu.add(cutMenuItem);

        JMenuItem copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener(e -> textArea.copy());
        editMenu.add(copyMenuItem);

        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener(e -> textArea.paste());
        editMenu.add(pasteMenuItem);

        JMenuItem selectAllMenuItem = new JMenuItem("Select All");
        selectAllMenuItem.addActionListener(e -> textArea.selectAll());
        editMenu.add(selectAllMenuItem);

        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);

        JMenu formatMenu = new JMenu("Format");
        menuBar.add(formatMenu);

        JMenuItem wordWrapMenuItem = new JCheckBoxMenuItem("Word Wrap");
        wordWrapMenuItem.addActionListener(e -> textArea.setLineWrap(((JCheckBoxMenuItem)e.getSource()).isSelected()));
        formatMenu.add(wordWrapMenuItem);

        JMenuItem fontSizeMenuItem = new JMenuItem("Font Size");
        fontSizeMenuItem.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Enter font size:");
            try {
                int size = Integer.parseInt(input);
                textArea.setFont(new Font(textArea.getFont().getName(), textArea.getFont().getStyle(), size));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid integer.");
            }
        });
        formatMenu.add(fontSizeMenuItem);
    }

    private void newFile() {
        if (textChanged) {
            int option = JOptionPane.showConfirmDialog(this, "Do you want to save changes?");
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
            }
        }
        textArea.setText("");
        currentFile = null;
        textChanged = false;
    }

    private void openFile() {
        if (textChanged) {
            int option = JOptionPane.showConfirmDialog(this, "Do you want to save changes?");
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
            }
        }
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.read(reader, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        textChanged = false;
    }

    private void saveFile() {
        if (currentFile == null) {
            saveAsFile();
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                textArea.write(writer);
                textChanged = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAsFile() {
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                textArea.write(writer);
                textChanged = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exit() {
        if (textChanged) {
            int option = JOptionPane.showConfirmDialog(this, "Do you want to save changes?");
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Notepad notepad = new Notepad();
            notepad.setVisible(true);
        });
    }
}