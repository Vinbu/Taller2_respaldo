import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 */
public class Decode {
    public void mainDecode(String nombre_imagen_final) throws IOException{
        try {
            String imagePath = nombre_imagen_final;
            int[] bitsArray = decode(imagePath);
            String message = convertirbitsaletra(bitsArray);
            System.out.println("Mensaje extraído: " + message);
        }
        catch(Exception e){
            System.out.println("La imagen no se encuentra en el directorio");
        }
    }
    public static int[] decode(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            int width = image.getWidth();
            int height = image.getHeight();
            int[] bitsArray = new int[width*height*3];
            int index = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    Color c = new Color(pixel);

                    int red = c.getRed() & 0x01;
                    int green = c.getGreen() & 0x01;
                    int blue = c.getBlue() & 0x01;

                    bitsArray[index] = red;
                    index++;

                    bitsArray[index] = green;
                    index++;

                    bitsArray[index] = blue;
                    index++;
                }
            }

            return bitsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new int[0];
    }
    public static String convertirbitsaletra(int[] bitsArray) {
        StringBuilder messageBuilder = new StringBuilder();
        int contador = 0;
        for (int i = 0; i < bitsArray.length; i += 8) {
            int byteValue = 0;

            for (int j = 0; j < 8; j++) {
                if (i + j < bitsArray.length) {
                    byteValue = (byteValue << 1) | bitsArray[i + j];
                }
            }
            if (byteValue == 36){
                contador++;
            }
            if((contador == 3) && (byteValue != 36)){
                messageBuilder.append((char) byteValue);
            }
            if (contador == 6){
                break;
            }

        }

        return messageBuilder.toString();
    }
}
