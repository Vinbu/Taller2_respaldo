import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 */
public class Encode {
    public void mainEncode(String mensajeEntrada, String nombre_imagen, String nombre_imagen_final) throws IOException{
        try {
            // Cargar la imagen desde un archivo
            BufferedImage imagen = ImageIO.read(new File(nombre_imagen));

            String inicio_fin = "$$$";
            String mensaje = inicio_fin + mensajeEntrada + inicio_fin;


            int filas = (int) Math.ceil((double) mensaje.length() * 8 / 3); // Obtener el número de filas necesario
            int columnas = 3;
            char[][] matriz = new char[filas][columnas];

// Guardar los bits individuales en la matriz
            int fila = 0;
            int columna = 0;
            for (int i = 0; i < mensaje.length(); i++) {
                char letra = mensaje.charAt(i);
                String binario = String.format("%8s", Integer.toBinaryString(letra)).replace(' ', '0');
                for (int j = 0; j < binario.length(); j++) {
                    matriz[fila][columna] = binario.charAt(j);
                    columna++;
                    if (columna >= columnas) {
                        columna = 0;
                        fila++;
                    }
                }
            }

            //Obtiene las dimensiones de la imagen
            int alto = imagen.getHeight();
            int ancho = imagen.getWidth();

            int x = 0;

            int i = 0;
            for (int y = 0; y < alto; y++) {
                if (i != filas && y == alto - 1) {
                    System.out.println("Lo sentimos tu mensaje es muy largo para caber en la "
                            + "imagen, escoge otra imagen, o acorta el mensaje. De todas formas"
                            + " tu mensaje se guardó parcialmente");
                    break;
                }
                if (i == filas) {
                    break;
                }
                for (x = 0; x < ancho; x++) {
                    if (i == filas) {
                        break;
                    }
                    //Obtiene los valores del Pixel
                    int Pixel = imagen.getRGB(x, y);
                    Color c = new Color(Pixel);
                    //Crea la mascara para comparar los bits
                    int mascara = 0xFE;

                    int nuevoPixel;

                    //Reccorre la fila actual de la matriz para guardar los bits en cada valor RGB
                    for (int j = 0; j < matriz[i].length; j++) {


                        int colorA = c.getAlpha();
                        int colorR = c.getRed();
                        int colorG = c.getGreen();
                        int colorB = c.getBlue();


                        int nuevoColorR = 0;
                        int nuevoColorG = 0;
                        int nuevoColorB = 0;

                        int cantidadElementos = 0;
                        for (int n = 0; n < matriz[i].length; n++) {
                            if (matriz[i][n] != '\u0000') {
                                cantidadElementos++;
                            }
                        }
                        //Se crean 3 situaciones= Dependiendo del largo del mensaje existe la posibilidad de que la última fila de la matriz de bits
                        //este incompleta, es decir, tenga solo 2 o 1 bit guardado, entonces para evitar errores en el codigo,
                        //se prepara la situacion de que última fila solo tenga 1 elemento o 2, el resto de veces siempre pasará por la opción 3,
                        //en la que la fila 3 está llena
                        if (cantidadElementos == 1) {
                            char bitChar = matriz[i][j];
                            int bit = Character.getNumericValue(bitChar);
                            nuevoColorR = (colorR & mascara) | bit;
                            nuevoPixel = (colorA << 24) | (nuevoColorR << 16) | (colorG << 8) | colorB;
                            imagen.setRGB(x, y, nuevoPixel);
                            i++;
                            break;
                        } else if (cantidadElementos == 2) {

                            j = 1;

                            char bitRChar = matriz[i][j - 1];
                            int bit = Character.getNumericValue(bitRChar);
                            nuevoColorR = (colorR & mascara) | bit;


                            char bitGChar = matriz[i][j];
                            bit = Character.getNumericValue(bitGChar);
                            nuevoColorG = (colorG & mascara) | bit;
                            nuevoPixel = (colorA << 24) | (nuevoColorR << 16) | (nuevoColorG << 8) | colorB;
                            imagen.setRGB(x, y, nuevoPixel);
                            i++;
                            break;

                        } else if (cantidadElementos == 3) {
                            j = 2;
                            if (j == 2 && (i == (filas - 1))) {

                                int bitChar = matriz[i][j - 2];
                                int bit = Character.getNumericValue(bitChar);
                                nuevoColorR = (colorR & mascara) | bit;


                                bitChar = matriz[i][j - 1];
                                bit = Character.getNumericValue(bitChar);
                                nuevoColorG = (colorG & mascara) | bit;

                                bitChar = matriz[i][j];
                                bit = Character.getNumericValue(bitChar);
                                nuevoColorB = (colorB & mascara) | bit;
                                nuevoPixel = (colorA << 24) | (nuevoColorR << 16) | (nuevoColorG << 8) | nuevoColorB;
                                imagen.setRGB(x, y, nuevoPixel);
                                i++;
                                break;
                            }
                            if (j == 2) {
                                char bitChar = matriz[i][j - 2];
                                int bit = Character.getNumericValue(bitChar);
                                nuevoColorR = (colorR & mascara) | bit;


                                bitChar = matriz[i][j - 1];
                                bit = Character.getNumericValue(bitChar);
                                nuevoColorG = (colorG & mascara) | bit;

                                bitChar = matriz[i][j];
                                bit = Character.getNumericValue(bitChar);
                                nuevoColorB = (colorB & mascara) | bit;
                                nuevoPixel = (colorA << 24) | (nuevoColorR << 16) | (nuevoColorG << 8) | nuevoColorB;
                                imagen.setRGB(x, y, nuevoPixel);
                                i++;
                            }
                        }
                    }
                }
            }
            ImageIO.write(imagen, "png", new File(nombre_imagen_final));
            System.out.println("La nueva imagen se ha guardado");
        }catch (javax.imageio.IIOException e){
            System.out.println("La imagen no se encuentra en el directorio, por favor considera el" +
                    " número de argumentos y que tu mensaje debe estar entre comillas");
        }
    }
}