import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ImageFromImages extends JFrame {
    private BufferedImage editedImage = null;
    private BufferedImage originalImage = null;
    private final JButton btnSaveDirectory = new JButton("Save Directory");
    private final JLabel lblDirectory = new JLabel("Directory:");
    private String imageDirData = "";
    private String pathOfDirectory;
    private final JPanel pnlOriginalImage = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawOriginalImage(g);
        }
    };

    private final JPanel pnlNewImage = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawNewImage(g);
        }
    };

    /**
     * Launch the application
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ImageFromImages frame = new ImageFromImages();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame
     */
    public ImageFromImages() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Declaration and Initialization
        JButton btnChooseImage = new JButton("Choose Image");
        JButton btnChooseDirectory = new JButton("Choose Directory");
        JButton btnLoadTxt = new JButton("Load TXT");
        JButton btnCreateImage = new JButton("Create");
        JTextField tfNumberOfImages = new JTextField("10");

        // Position and Size
        btnChooseImage.setBounds(10, 10, 125, 25);
        btnChooseDirectory.setBounds(140, 10, 150, 25);
        btnLoadTxt.setBounds(300, 10, 100, 25);
        lblDirectory.setBounds(410, 10, 200, 25);
        btnSaveDirectory.setBounds(650, 10, 125, 25);
        tfNumberOfImages.setBounds(10, 360, 50, 25);
        btnCreateImage.setBounds(70, 360, 120, 25);
        pnlOriginalImage.setBounds(10, 50, 300, 300);
        pnlNewImage.setBounds(324, 50, 300, 300);

        // Visibility
        btnSaveDirectory.setVisible(false);

        // Listener
        btnChooseImage.addActionListener(arg0 -> {
            try {
                editedImage = ImageIO.read(openFileChooser("file"));
                originalImage = editedImage; // save original
                pnlOriginalImage.repaint();
            } catch (Exception e) {
                System.out.println("Error while choosing image: " + e.getMessage());
            }
        });
        btnChooseDirectory.addActionListener(arg0 -> {
            try {
                File file = openFileChooser("dir");
                pathOfDirectory = file.getAbsolutePath();
                loadDirectory(file);
                btnSaveDirectory.setVisible(true);
            } catch (Exception e) {
                System.out.println("Error while choosing directory: " + e.getMessage());
            }
        });
        btnSaveDirectory.addActionListener(arg0 -> {
            if (!imageDirData.equals("")) {
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(openFileChooser("file") + ".txt"));
                    bw.write(imageDirData);
                    bw.close();
                } catch (Exception e) {
                    System.out.println("No File selected!");
                    JOptionPane.showMessageDialog(null, "No File selected!", "Warning: No File selected", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        btnLoadTxt.addActionListener(arg0 -> {
            try {
                File file = openFileChooser("txt");
                BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
                imageDirData = br.readLine();
                pathOfDirectory = imageDirData.split("°")[0];
                System.out.println("Directory loaded successfully!");
                lblDirectory.setText("Directory: " + file.getName());
            } catch (Exception e) {
                System.out.println("Error while loading Color File: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Error while loading Color File!", "Warning: Error while loading Color File", JOptionPane.PLAIN_MESSAGE);
            }
        });
        btnCreateImage.addActionListener(arg0 -> {
            if (editedImage == null) {
                JOptionPane.showMessageDialog(null, "Choose an image first!", "Warning: No image", JOptionPane.PLAIN_MESSAGE);
            } else if (pathOfDirectory == null) {
                JOptionPane.showMessageDialog(null, "Choose a directory first!", "Warning: No directory", JOptionPane.PLAIN_MESSAGE);
            } else {
                divideImageAndReadRGB(Integer.parseInt(tfNumberOfImages.getText()));
            }
        });

        // Add to Panel
        contentPane.add(btnChooseImage);
        contentPane.add(btnChooseDirectory);
        contentPane.add(btnSaveDirectory);
        contentPane.add(btnLoadTxt);
        contentPane.add(btnCreateImage);
        contentPane.add(lblDirectory);
        contentPane.add(tfNumberOfImages);
        contentPane.add(pnlOriginalImage);
        contentPane.add(pnlNewImage);
    }

    /**
     * Opens the File chooser and returns the selected path
     */
    private File openFileChooser(String type) {
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(null);
        if (type.equals("dir")) {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else if (type.equals("txt")) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (.txt)", "txt", "text");
            fc.setFileFilter(filter);
        }
        fc.showOpenDialog(null);
        File f = fc.getSelectedFile();
        System.out.println(f.getAbsolutePath());
        return f;
    }

    /**
     * Load a specific directory that contains certain images.
     * Then save their average color value
     *
     * @param file The file to be loaded
     */
    public void loadDirectory(File file) {
        System.out.println("Iterate over every image in directory and save the average color value");
        String[] files = file.list();
        int numberOfFiles = files.length;
        System.out.println("Found " + numberOfFiles + " files in " + file);
        imageDirData = file + "°" + numberOfFiles + "#";
        for (String image : files) {
            if (image.contains(".JPG") || image.contains(".jpg")|| image.contains(".PNG")|| image.contains(".png")) {
                System.out.println("Found image: " + image);
                readRGBFromImage(image, file.getAbsolutePath());
            } else {
                System.out.println("No Picture: " + image);
            }
        }
        System.out.println(imageDirData);
        btnSaveDirectory.setVisible(true);
        lblDirectory.setText("Directory: " + file.getName());
    }

    /**
     * This function reads the average rgb color values from a given picture via the path
     *
     * @param imageName The image name
     * @param dirPath   The main path of the directory
     */
    public void readRGBFromImage(String imageName, String dirPath) {
        try {
            BufferedImage image = ImageIO.read(new File(dirPath + "\\" + imageName));
            int width = image.getWidth();
            int height = image.getHeight();
            int red = 0, green = 0, blue = 0;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pxlColor = image.getRGB(x, y);
                    Color color = new Color(pxlColor);
                    red += color.getRed();
                    green += color.getGreen();
                    blue += color.getBlue();
                }
            }
            int points = width * height;
            red /= points;
            green /= points;
            blue /= points;
            //System.out.println(red + "," + green + "," + blue);
            imageDirData += imageName + "=" + red + "," + green + "," + blue + ";";
        } catch (IOException e) {
            System.out.println("Error while reading Color: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error while reading Color!", "Warning: Error while reading Color", JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * Divides the image in parts and reads the red, blue and green values
     *
     * @param numberOfImages How many small images should appear in one row
     */
    public void divideImageAndReadRGB(int numberOfImages) {
        try {
            int widthOfOneImage = editedImage.getWidth() / numberOfImages;
            int heightOfOneImage = editedImage.getHeight() / numberOfImages;
            BufferedImage[] squaresOfOriginal = new BufferedImage[numberOfImages * numberOfImages];
            BufferedImage[] smallImages = new BufferedImage[numberOfImages * numberOfImages];
            int counter = 0, counter2 = 0;
            for (int x = 0; x < numberOfImages; x++) {
                for (int y = 0; y < numberOfImages; y++) {
                    // Divide the image into smaller images
                    squaresOfOriginal[counter] = new BufferedImage(widthOfOneImage, heightOfOneImage, originalImage.getType());
                    Graphics2D gr = squaresOfOriginal[counter].createGraphics();
                    gr.drawImage(originalImage, 0, 0, widthOfOneImage, heightOfOneImage,
                            widthOfOneImage * y, heightOfOneImage * x, widthOfOneImage * y
                                    + widthOfOneImage, heightOfOneImage * x
                                    + heightOfOneImage, null);
                    gr.dispose();
                    counter++;
                }
            }
            for (BufferedImage img : squaresOfOriginal) {
                int width = img.getWidth();
                int height = img.getHeight();
                int red = 0, green = 0, blue = 0;
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int pxlColor = img.getRGB(x, y);
                        Color color = new Color(pxlColor);
                        red += color.getRed();
                        green += color.getGreen();
                        blue += color.getBlue();
                    }
                }
                int points = width * height;
                red /= points;
                green /= points;
                blue /= points;
                String test = pathOfDirectory + "\\" + findMatchingImage(red, green, blue);
                smallImages[counter2] = ImageIO.read(new File(test));
                counter2++;
            }
            createNewImage(smallImages, numberOfImages, widthOfOneImage, heightOfOneImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Find for each square of the original image a matching image out of the directory
     *
     * @param r The red value of square
     * @param g The green value of square
     * @param b The blue value of square
     * @return The Path of the best matching image
     */
    public String findMatchingImage(int r, int g, int b) {
        String data = imageDirData;
        data = data.split("#")[1];
        String[] elements = data.split(";");
        String pathOfPicture = "";
        int bestDiff = 1000;
        for (String element : elements) {
            String text = element;
            String directory = text.split("=")[0];
            text = text.split("=")[1];
            int red = Integer.parseInt(text.split(",")[0]);
            int green = Integer.parseInt(text.split(",")[1]);
            int blue = Integer.parseInt(text.split(",")[2]);
            int difference = Math.abs(r - red) + Math.abs(g - green) + Math.abs(b - blue);
            if (difference < bestDiff) {
                pathOfPicture = directory;
                bestDiff = difference;
            }
        }
        //System.out.println("Use image " + pathOfPicture + ", because " + bestDiff + " is minimum deviation/difference.");
        return pathOfPicture;
    }

    /**
     * Create the new image
     *
     * @param smallImages The small images from directory
     * @param numberOfImages The number of images per row
     * @param widthOfOneImage The width of one image
     * @param heightOfOneImage The height of one image
     */
    public void createNewImage(BufferedImage[] smallImages, int numberOfImages, int widthOfOneImage, int heightOfOneImage) {
        int rowX = -1;
        int rowY = 0;
        Graphics2D g_bi = editedImage.createGraphics();
        for (int i = 0; i < smallImages.length; i++) {
            if (i % numberOfImages == 0 && i != 0) {
                rowX = 0;
                rowY++;
            } else {
                rowX++;
            }
            g_bi.drawImage(smallImages[i], rowX * widthOfOneImage, rowY * heightOfOneImage, widthOfOneImage, heightOfOneImage, Color.BLUE, null);
        }
        g_bi.dispose();
        pnlNewImage.repaint();
    }

    /**
     * Draw the image every time the panel gets reloaded
     *
     * @param g The graphics to be drawn
     */
    public void drawOriginalImage(Graphics g) {
        if (originalImage != null) {
            int width = pnlOriginalImage.getWidth();
            int height = originalImage.getHeight() * width / originalImage.getWidth();
            g.drawImage(originalImage, 0, 0, width, height, this);
        }
    }

    /**
     * Draw the image every time the panel gets reloaded
     *
     * @param g The graphics to be drawn
     */
    public void drawNewImage(Graphics g) {
        if (editedImage != null) {
            int width = pnlOriginalImage.getWidth();
            int height = editedImage.getHeight() * width / editedImage.getWidth();
            g.drawImage(editedImage, 0, 0, width, height, this);
        }
    }
}
