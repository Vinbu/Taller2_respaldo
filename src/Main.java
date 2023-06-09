import java.io.IOException;
public class Main {
    public static void main(String[] args) throws IOException, IOException {
        try {

        String mensajeEntrada= "";
        String nombre_imagen= "";
        String nombre_imagen_final;

            if (args[0].equalsIgnoreCase("encode")) {
                try {
                        // Crear instancia de Encode y ejecutar el código
                        mensajeEntrada = args[1];
                        nombre_imagen = args[2];
                        nombre_imagen_final = args[3];
                        Encode encode = new Encode();
                        encode.mainEncode(mensajeEntrada, nombre_imagen, nombre_imagen_final);
                    } catch (Exception e) {
                        System.out.println("Por favor ingrese el/los valor/es faltante/s");
                }
            } else if (args[0].equalsIgnoreCase("decode")) {
                try {
                    // Crear instancia de Decode y ejecutar el código
                    nombre_imagen_final = args[1];
                    Decode decode = new Decode();
                    decode.mainDecode(nombre_imagen_final);
                }
                catch(Exception e){
                    System.out.println("Ingrese el nombre de la imagen");
                }
            }else{
                System.out.println("Ingrese si es 'encode' o 'decode'");
            }

            }catch(Exception e){
            System.out.println("Ingrese si es 'encode' o 'decode'");
        }
    }
}