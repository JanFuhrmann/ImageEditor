import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSlider;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author JanFuhrmann
 */
public class ImageEditor extends JFrame {
    private final JLabel lblFile = new JLabel("File: ");
    private final JTextField txtFile = new JTextField();
    private final JButton btnApply = new JButton("Apply filter");
    private final JButton btnSaveImage = new JButton("Save Picture");
    private final JButton btnChooseFile = new JButton("Choose");
    private final JButton btnBack = new JButton("\u25C4");
    private final JButton btnForth = new JButton("\u25BA");
    private final JButton btnPickColor = new JButton("Color");
    private final JCheckBox checkboxDraw = new JCheckBox("Draw");
    private final JCheckBox checkboxEraser = new JCheckBox("Eraser");
    private final JCheckBox checkboxSaveChanges = new JCheckBox("Save changes");
    private final JComboBox<String> comboBox = new JComboBox<>();
    private final JSlider slider = new JSlider();

    private final JPanel contentPane = new JPanel();
    private final JPanel pnlPreview = new JPanel();
    private final JPanel pnlImage = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawPicture(g);
        }
    };

    private BufferedImage editedImage = null, originalImage = null, backupImage = null;
    private final ArrayList<BufferedImage> lastImages = new ArrayList<>(), nextImages = new ArrayList<>();
    private int recentHeight = 0, recentWidth = 0, xStart = 0, yStart = 0, xEnd = 0, yEnd = 0, xStart2 = 0, yStart2 = 0;
    private boolean erase = false, draw = false, start = false, savePicture = false;
    private String selection;
    private final int[] rgbOfPoint = new int[3];


    /**
     * Launch the application
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ImageEditor frame = new ImageEditor();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame
     */
    public ImageEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close operation
        setBounds(10, 10, 1500, 750); // standard size (if not fullscreen)
        contentPane.setLayout(null); // mandatory for creating the layout
        setContentPane(contentPane);
        initComponents();
    }

    /**
     * This function initializes all elements (e.g. size, position, textSize)
     */
    private void initComponents() {
        // Fonts and text size
        Font font18 = new Font("Tahoma", Font.PLAIN, 18);
        lblFile.setFont(font18);
        btnChooseFile.setFont(font18);
        btnApply.setFont(font18);
        checkboxEraser.setFont(font18);
        checkboxDraw.setFont(font18);
        checkboxSaveChanges.setFont(new Font("Tahoma", Font.PLAIN, 14));
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 15));

        // Position and size
        lblFile.setBounds(10, 10, 40, 20);
        txtFile.setBounds(60, 10, 350, 25);
        btnChooseFile.setBounds(420, 10, 100, 25);
        btnSaveImage.setBounds(530, 10, 110, 25);
        btnBack.setBounds(650, 10, 50, 25);
        btnForth.setBounds(710, 10, 50, 25);
        btnApply.setBounds(850, 500, 200, 40);
        btnPickColor.setBounds(970, 65, 80, 25);
        checkboxSaveChanges.setBounds(770, 9, 125, 25);
        checkboxEraser.setBounds(850, 45, 100, 25);
        checkboxDraw.setBounds(850, 65, 100, 25);
        comboBox.setBounds(850, 100, 200, 25);
        pnlImage.setBounds(22, 45, 805, 805);
        pnlPreview.setBounds(850, 180, 200, 200);
        slider.setBounds(850, 125, 200, 25);

        // Listener
        btnChooseFile.addActionListener(e -> loadImage());
        btnSaveImage.addActionListener(e -> saveImage());
        btnApply.addActionListener(e -> applyFilterOnWhole());
        btnBack.addActionListener(arg -> {
            if (!nextImages.contains(editedImage)) {
                nextImages.add(editedImage);
            }
            if (lastImages.size() - 1 > 0) {
                editedImage = lastImages.get(lastImages.size() - 1);
                lastImages.remove(lastImages.size() - 1);
            } else {
                editedImage = originalImage;
            }
            pnlImage.repaint();
        });
        btnForth.addActionListener(e -> {
            if (nextImages.size() - 1 > 0) {
                lastImages.add(nextImages.get(nextImages.size() - 1));
                editedImage = lastImages.get(lastImages.size() - 1);
                pnlImage.repaint();
                nextImages.remove(nextImages.size() - 1);
            } else {
                editedImage = nextImages.get(nextImages.size() - 1);
                pnlImage.repaint();
            }
        });
        btnPickColor.addActionListener(e -> {
            pnlPreview.setVisible(true);
            pnlPreview.setBackground(JColorChooser.showDialog(null,
                    "Select Color...", null));
        });
        checkboxSaveChanges.addActionListener(e -> savePicture = !savePicture);
        checkboxEraser.addActionListener(e -> {
            erase = !erase;
            checkboxDraw.setSelected(false);
            draw = false;
            slider.setVisible(false);
            btnPickColor.setVisible(false);
        });
        checkboxDraw.addActionListener(arg0 -> {
            draw = !draw;
            btnPickColor.setVisible(draw);
            pnlPreview.setVisible(draw);
            slider.setVisible(draw);
            checkboxEraser.setSelected(false);
            erase = false;
        });
        comboBox.addActionListener(e -> getSelection());
        pnlImage.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                ImageEditor.this.mouseDragged(e);
            }
        });
        pnlImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                ImageEditor.this.mouseReleased();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lastImages.add(editedImage);
                nextImages.clear();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                ImageEditor.this.mouseClicked(e);
            }
        });
        slider.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!erase) {
                    applyFilter();
                }
            }
        });

        // Visibility
        btnApply.setVisible(false);
        btnPickColor.setVisible(false);
        pnlImage.setVisible(false);
        comboBox.setVisible(false);
        checkboxEraser.setVisible(false);
        checkboxDraw.setVisible(false);

        // Other
        checkboxSaveChanges.setToolTipText("Effects are saved");
        comboBox.addItem("Select Filter...");
        comboBox.addItem("Black & White");
        comboBox.addItem("Blurred");
        comboBox.addItem("Invert");
        comboBox.addItem("Grey");
        comboBox.addItem("Warm");
        comboBox.addItem("Point");
        comboBox.addItem("Horror");
        comboBox.addItem("Drawing");

        // Add all elements to current content Pane
        contentPane.add(lblFile);
        contentPane.add(txtFile);
        contentPane.add(btnChooseFile);
        contentPane.add(btnSaveImage);
        contentPane.add(btnApply);
        contentPane.add(checkboxSaveChanges);
        contentPane.add(checkboxEraser);
        contentPane.add(comboBox);
        contentPane.add(pnlImage);
        contentPane.add(pnlPreview);
        contentPane.add(slider);
        contentPane.add(btnBack);
        contentPane.add(btnForth);
        contentPane.add(checkboxDraw);
        contentPane.add(btnPickColor);
    }

    /**
     * Applies the chosen filter
     */
    public void applyFilter() {
        if (originalImage != null) {
            int red = 0, blue = 0, green = 0;
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            Color[][] rgb = new Color[width][height];
            editedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = editedImage.createGraphics();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pxlRed = 0, pxlGreen = 0, pxlBlue = 0, pxlColor;
                    if (savePicture) {
                        pxlColor = backupImage.getRGB(x + (int) (Math.random()), y + (int) (Math.random()));
                    } else {
                        pxlColor = originalImage.getRGB(x + (int) (Math.random()), y + (int) (Math.random()));
                    }
                    Color c = new Color(pxlColor);
                    pxlBlue += c.getBlue();
                    pxlGreen += c.getGreen();
                    pxlRed += c.getRed();
                    int sliderValue = slider.getValue();

                    if (selection.equals("Point") && pxlRed <= sliderValue + rgbOfPoint[0] && pxlRed >= rgbOfPoint[0] - sliderValue && pxlGreen <= sliderValue + rgbOfPoint[1] && pxlGreen >= rgbOfPoint[1] - sliderValue && pxlBlue <= sliderValue + rgbOfPoint[2] && pxlBlue >= rgbOfPoint[2] - sliderValue) {
                        rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                    } else if (selection.equals("Point")) {
                        int valueGrey = (int) (pxlRed * 0.299 + pxlGreen * 0.587 + pxlBlue * 0.114);
                        rgb[x][y] = new Color(valueGrey, valueGrey, valueGrey);
                    } else if (x >= xStart && y >= yStart && x <= xEnd && y <= yEnd) {
                        if (selection.equals("Black & White")) {
                            int valueBW = (pxlBlue + pxlGreen + pxlRed) / 3;
                            if (valueBW > sliderValue) {
                                rgb[x][y] = new Color(255, 255, 255);
                            } else {
                                rgb[x][y] = new Color(0, 0, 0);
                            }
                        } else if (selection.equals("Blurred") || selection.equals("Horror")) {
                            sliderValue /= 10 + 1;
                            int dr = 0;
                            int dg = 0;
                            int db = 0;
                            int co2 = 0;
                            for (int i = 0; i < sliderValue; i++) {
                                for (int j = 0; j < sliderValue; j++) {
                                    if (x + i < width && y + j < height) { // If not outside of range
                                        if (savePicture) {
                                            co2 = backupImage.getRGB(x + i,
                                                    y + j);
                                        } else {
                                            co2 = originalImage.getRGB(x + i,
                                                    y + j);
                                        }
                                    }
                                    Color c2 = new Color(co2);
                                    dr += c2.getRed();
                                    dg += c2.getGreen();
                                    db += c2.getBlue();
                                }
                            }
                            if (sliderValue == 0) sliderValue = 1;
                            dr /= (sliderValue * sliderValue);
                            dg /= (sliderValue * sliderValue);
                            db /= (sliderValue * sliderValue);
                            if (selection.equals("Blurred")) {
                                rgb[x][y] = new Color(dr, dg, db);
                            } else {
                                if (pxlRed - dr > 0) {
                                    pxlRed -= dr;
                                }
                                if (pxlGreen - dg > 0) {
                                    pxlGreen -= dg;
                                }
                                if (pxlBlue - db > 0) {
                                    pxlBlue -= db;
                                }
                                rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                            }
                        } else if (selection.equals("Invert")) {
                            rgb[x][y] = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
                        } else if (selection.equals("Grey")) {
                            int valueGrey = (int) (pxlRed * 0.299 + pxlGreen * 0.587 + pxlBlue * 0.114);
                            rgb[x][y] = new Color(valueGrey, valueGrey, valueGrey);
                        } else if (selection.equals("Warm")) {
                            if (pxlRed + (sliderValue / 2) < 256) {
                                pxlRed += (sliderValue / 2);
                            }
                            if (pxlGreen + (sliderValue / 2) < 256) {
                                pxlGreen += (sliderValue / 2);
                            }
                            rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                        } else if (selection.equals("Drawing")) {
                            if (Math.abs(red - pxlRed) > 5 && Math.abs(green - pxlGreen) > 5 && Math.abs(blue - pxlBlue) > 5) {
                                rgb[x][y] = Color.BLACK;
                            } else {
                                rgb[x][y] = Color.WHITE;
                            }
                            red = pxlRed;
                            blue = pxlBlue;
                            green = pxlGreen;
                        } else {
                            // original picture
                            rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                        }
                    } else {
                        // original picture
                        rgb[x][y] = new Color(pxlRed, pxlGreen, pxlBlue);
                    }
                    graphics.setColor(rgb[x][y]);
                    graphics.fillRect(x, y, 1, 1);
                }
            }
            pnlImage.repaint(); // paint image
        }
    }

    /**
     * Applies the chosen filter on whole image
     */
    protected void applyFilterOnWhole() {
        if (editedImage != null) {
            xStart = 0;
            yStart = 0;
            xEnd = editedImage.getWidth();
            yEnd = editedImage.getHeight();
            applyFilter();
        }
    }

    /**
     * Show elements if specific items are selected
     */
    protected void getSelection() {
        selection = (String) comboBox.getSelectedItem();
        pnlPreview.setVisible(selection.equals("Point"));
        slider.setVisible(selection.equals("Black & White") || selection.equals("Blurred") || selection.equals("Warm") || selection.equals("Point"));
    }

    /**
     * When mouse was dragged on panel
     *
     * @param mouseEvent The specific Event
     */
    protected void mouseDragged(MouseEvent mouseEvent) {
        int width = editedImage.getWidth();
        int height = editedImage.getHeight();
        float factorA = (float) recentWidth / width;
        float factorB = (float) recentHeight / height;
        int x = (int) (mouseEvent.getX() / factorA);
        int y = (int) (mouseEvent.getY() / factorB);
        if (draw) {
            Graphics2D g_bi = editedImage.createGraphics();
            g_bi.setColor(pnlPreview.getBackground());
            int sliderValue = slider.getValue()/4;
            g_bi.fillRect(x - (sliderValue / 2), y - (sliderValue / 2), sliderValue, sliderValue);
            pnlImage.repaint();
        } else if (erase) {
            int size = slider.getValue() / 5; // size of eraser
            Graphics2D g_bi = editedImage.createGraphics();
            Color[][] rgb = new Color[width][height];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (x - i + (size / 2) < width
                            && y - j + (size / 2) < height
                            && x - i + (size / 2) > -1
                            && y - j + (size / 2) > -1) {
                        int pxlColor = originalImage.getRGB(x - i + (size / 2), y
                                - j + (size / 2));
                        Color c = new Color(pxlColor);
                        int b = c.getBlue();
                        int g = c.getGreen();
                        int r = c.getRed();
                        rgb[x - i + (size / 2)][y - j + (size / 2)] = new Color(
                                r, g, b);
                        g_bi.setColor(rgb[x - i + (size / 2)][y - j
                                + (size / 2)]);
                        g_bi.fillRect(x - i + (size / 2), y - j
                                + (size / 2), 1, 1);
                    }
                }
            }
            pnlImage.repaint();
        } else if (!selection.equals("Point")) {
            // get start- and endpoint of selection
            if (!start) {
                xStart = x;
                yStart = y;
                xStart2 = x;
                yStart2 = y;
                start = true;
                if (savePicture) {
                    backupImage = editedImage;
                }
            } else {
                xEnd = x;
                yEnd = y;
                if (x < xStart2) {
                    xEnd = xStart2;
                    xStart = x;
                }
                if (y < yStart2) {
                    yEnd = yStart2;
                    yStart = y;
                }
                applyFilter();
            }
        }
    }

    /**
     * When mouse was clicked on panel
     *
     * @param mouseEvent The specific event
     */
    protected void mouseClicked(MouseEvent mouseEvent) {
        if (selection.equals("Point")) {
            float factorA = (float) recentWidth / editedImage.getWidth();
            float factorB = (float) recentHeight / editedImage.getHeight();
            int x = (int) (mouseEvent.getX() / factorA);
            int y = (int) (mouseEvent.getY() / factorB);
            int r = 0;
            int b = 0;
            int g = 0;
            int pxlColor;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (savePicture) {
                        pxlColor = backupImage.getRGB(x, y);
                    } else {
                        pxlColor = editedImage.getRGB(x, y);
                    }
                    Color c = new Color(pxlColor);
                    b += c.getBlue();
                    g += c.getGreen();
                    r += c.getRed();
                }
            }
            rgbOfPoint[0] = r / 100;
            rgbOfPoint[1] = g / 100;
            rgbOfPoint[2] = b / 100;
            // Point Preview
            pnlPreview.setVisible(true);
            pnlPreview.setBackground(new Color(rgbOfPoint[0], rgbOfPoint[1], rgbOfPoint[2]));
            applyFilterOnWhole();
        }
    }

    /**
     * When mouse was released on panel
     */
    protected void mouseReleased() {
        start = false;
        if (savePicture) {
            backupImage = editedImage;
        }
    }

    /**
     * Load image either from path or file chooser
     */
    protected void loadImage() {
        boolean errorWhileLoading = false;
        if (txtFile.getText().equals("")) {
            try {
                final JFileChooser fileChooser = new JFileChooser();
                // fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                fileChooser.showOpenDialog(null);
                txtFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
            } catch (Exception e) {
                System.out.println("No File selected!");
                JOptionPane.showMessageDialog(null, "No File selected!", "Warning: No File selected", JOptionPane.PLAIN_MESSAGE);
                errorWhileLoading = true;
            }
        }
        if (!errorWhileLoading) {
            try {
                editedImage = ImageIO.read(new File(txtFile.getText()));
                originalImage = editedImage; // save original
                backupImage = editedImage; // backup image
                lastImages.clear(); // clear old savings
                nextImages.clear(); // clear
                btnApply.setVisible(true);
                checkboxEraser.setVisible(true);
                checkboxDraw.setVisible(true);
                comboBox.setVisible(true);
                pnlImage.setVisible(true);
                pnlImage.repaint(); // repaint panel
            } catch (IOException e) {
                btnApply.setVisible(false);
                checkboxEraser.setVisible(false);
                checkboxDraw.setVisible(false);
                comboBox.setVisible(false);
                pnlImage.setVisible(false);
                System.out.println("Error while reading the file (" + e + ")");
                JOptionPane.showMessageDialog(null, "File could not be read!", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Save the created/edited image to local directory as a .png
     */
    protected void saveImage() {
        if (editedImage != null) {
            try {
                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(null);
                fc.showSaveDialog(this);
                File f = fc.getSelectedFile();
                ImageIO.write(editedImage, "png", f);
                System.out.println("Image successfully saved at " + f.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Image successfully saved!", "Successfully Saved", JOptionPane.PLAIN_MESSAGE);
            } catch (IOException e) {
                System.out.println("Error occurred while saving (" + e + ")");
                JOptionPane.showMessageDialog(null, "Image could not be saved!", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    /**
     * Draw the image every time the panel gets reloaded
     *
     * @param g The graphics to be drawn
     */
    public void drawPicture(Graphics g) {
        if (editedImage != null) {
            int width = pnlImage.getWidth();
            int height = editedImage.getHeight() * width / editedImage.getWidth();
            recentHeight = height;
            recentWidth = width;
            g.drawImage(editedImage, 0, 0, width, height, this);
        }
    }
}
