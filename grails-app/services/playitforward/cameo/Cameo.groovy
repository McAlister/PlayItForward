package playitforward.cameo

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.image.BufferedImage

class Cameo {

    private BufferedImage image
    private int xCenter
    private double xAxisSq
    private int yCenter
    private double yAxisSq
    private static double widthFromCenter = 0.35
    private static double heightFromCenter = 0.43

    Cameo(File filename) {
        image = ImageIO.read(filename)
        xCenter = image.getWidth()/2
        yCenter = image.getHeight()/2
        double xAxis = image.getWidth() * widthFromCenter
        double yAxis = image.getHeight() * heightFromCenter
        xAxisSq = xAxis * xAxis
        yAxisSq = yAxis * yAxis
    }

    private double ellipseVal(int x, int y) {
        return (x-xCenter)*(x-xCenter)/xAxisSq + (y-yCenter)*(y-yCenter)/yAxisSq
    }

    private boolean isInEllipse(int x, int y) {
        return ellipseVal(x, y) <= 1.0
    }

    private void maskEllipse() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (!isInEllipse(x, y)) {
                    image.setRGB(x, y, 0)
                }
            }
        }
    }

    private static int frameColor = new Color(0, 0x20, 0).getRGB()
    private static double frameLimit = 1.1

    private void addFrame() {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                double v = ellipseVal(x, y)
                if (v > 1 && v < frameLimit) {
                    image.setRGB(x, y, frameColor)
                }
            }
        }
    }

    private static double shadowLimit = 1.3
    private static int shadowIntensity = 128

    private void addDropShadow() {
        double start = frameLimit
        double stop = shadowLimit
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                double v = ellipseVal(x, y)
                if (v > start && v < stop) {
                    double level = 1 - (start-v) / (start-stop)
                    int mask = (int) (shadowIntensity * level)
                    if (mask < 0) {
                        mask = 0
                    }
                    image.setRGB(x, y, mask << 24)
                }
            }
        }
    }

    private def writePng(File output) {
        ImageIO.write(image, "png", output)
    }

    def makeCameo(File output) {
        maskEllipse()
        addFrame()
        addDropShadow()
        writePng(output)
    }
}
