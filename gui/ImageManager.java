package gui;

import entities.AbstractEntity;
import entities.animals.Deer;
import entities.animals.Rabbit;
import entities.animals.Lion;
import entities.plants.Flower;
import entities.plants.OakTree;
import entities.resources.Rock;
import entities.resources.Water;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Student 1: Shir Yehudai 212712194
 * Student 2: Orin Medina 211564935
 * Manages loading, caching, and scaling of entity images used in the GUI.
 * This class loads image resources once and provides scaled or original images
 * for entities during rendering.
 */
public class ImageManager {

    /**
     * Singleton instance of the image manager.
     */
    private static ImageManager instance = null;

    /**
     * Relative path to the images directory.
     */
    private static final String IMAGE_PATH = "images/";

    /**
     * Stores the original loaded images by filename.
     */
    private final Map<String, BufferedImage> originals = new HashMap<>();

    /**
     * Stores cached scaled versions of images by filename and size key.
     */
    private final Map<String, BufferedImage> scaledCache = new HashMap<>();

    /**
     * Returns the singleton instance of the image manager.
     * @return the shared ImageManager instance
     */
    public static synchronized ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    /**
     * Creates a new image manager and loads all image resources.
     */
    private ImageManager() {
        loadAll();
    }

    /**
     * Loads all supported entity images from the images directory into memory.
     */
    private void loadAll() {
        String[] files = {
            "icons8-deer-100.png",
            "icons8-rabbit-100.png",
            "icons8-lion-100.png",
            "icons8-flower-100.png",
            "icons8-oak-tree-100.png",
            "icons8-rock-100.png",
            "icons8-water-100.png"
        };

        for (String filename : files) {
            try {
                BufferedImage img = ImageIO.read(new File(IMAGE_PATH + filename));
                if (img != null) {
                    originals.put(filename, img);
                    System.out.println("Loaded: " + filename);
                } else {
                    System.err.println("Null image for: " + filename);
                }
            } catch (IOException e) {
                System.err.println("Cannot load " + filename + ": " + e.getMessage());
            }
        }
        System.out.println("ImageManager ready – " + originals.size() + " icons loaded.");
    }

    /**
     * Returns the matching image filename for the given entity type.
     * @param entity the entity for which an image is requested
     * @return the matching filename, or null if no match exists
     */
    private String filenameFor(AbstractEntity entity) {
        if (entity instanceof Deer)    return "icons8-deer-100.png";
        if (entity instanceof Rabbit)  return "icons8-rabbit-100.png";
        if (entity instanceof Lion)    return "icons8-lion-100.png";
        if (entity instanceof Flower)  return "icons8-flower-100.png";
        if (entity instanceof OakTree) return "icons8-oak-tree-100.png";
        if (entity instanceof Rock)    return "icons8-rock-100.png";
        if (entity instanceof Water)   return "icons8-water-100.png";
        return null;
    }

    /**
     * Returns a scaled image for the given entity and size.
     * Scaled images are cached for reuse.
     * @param entity the entity whose image is requested
     * @param size the desired image width and height
     * @return a scaled image, or null if no valid image is available
     */
    public Image getScaledEntityImage(AbstractEntity entity, int size) {
        if (size <= 0) {
            return null;
        }
        String filename = filenameFor(entity);
        if (filename == null) {
            return null;
        }
        String key = filename + "@" + size;
        if (scaledCache.containsKey(key)) {
            return scaledCache.get(key);
        }

        BufferedImage src = originals.get(filename);
        if (src == null) {
            return null;
        }

        BufferedImage dst = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dst.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(src, 0, 0, size, size, null);
        g2d.dispose();

        scaledCache.put(key, dst);
        return dst;
    }

    /**
     * Returns the original image associated with the given entity.
     * @param entity the entity whose image is requested
     * @return the original image, or null if no image exists
     */
    public Image getEntityImage(AbstractEntity entity) {
        String filename = filenameFor(entity);
        return filename != null ? originals.get(filename) : null;
    }

    /**
     * Checks whether an image exists for the given entity.
     * @param entity the entity to check
     * @return true if a matching image exists, false otherwise
     */
    public boolean hasImage(AbstractEntity entity) {
        String filename = filenameFor(entity);
        return filename != null && originals.containsKey(filename);
    }
}