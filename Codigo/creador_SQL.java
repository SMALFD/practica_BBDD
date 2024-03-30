//Codigo creado por Santiago Monreal para uso libre para quien quiera.

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class creador_SQL {
    // Mientras que no es necesario que se declaren aquí fuera es mucho más cómo
    // para poder acceder desde cualquier sitio
    static String[] tablas = new String[50];
    // Ponemos una longitud de 50 por poner, si vas a meter mas tablas puedes
    // cambiarlo
    static int numTablas = 0;

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(
                "Powered by ASCFI\nEste codigo fue creado por SMALFD y testeado por Seb0w, esperamos que os guste\n\n");
        try {
            // Creamos el archiv, filewriter y scanner, estos serán compartidos a través de
            // todo el código
            File arch = new File(
                    "C:\\Users\\santi\\OneDrive\\Documentos\\Santiago uni\\Bases de datos\\Proyecto\\proyecto_SQL\\src\\resultado.txt");// Aqui
            // va el directorio donde tienes guardado el archivo
            FileWriter res = new FileWriter(arch);
            Scanner in = new Scanner(System.in);
            System.out.println("Introduce el nombre del esquema");
            String schema = in.nextLine();
            System.out.println("El esquema se llama " + schema);
            res.write("DROP SCHEMA IF EXISTS " + schema + ";\n\n" + "CREATE SCHEMA " + schema + ";\nUSE " + schema
                    + ";\n\n");// Primero se eliminan esquemas anteriores con ese nombre y luego se crea uno
                               // nuevo vacio
            boolean tablas = true;
            while (tablas) {
                System.out.println("¿Quieres anadir una tabla? y/n");
                String nuevaTabla = in.nextLine();
                if (nuevaTabla.equals("y")) {
                    createTabla(res, in);
                } else if (nuevaTabla.equals("n")) {
                    tablas = false;
                    System.out.println("Tu programa esta creado");
                } else {
                    System.out.println("RESULTADO INESPERADO");
                } // Al ser un bucle te pide infinitamente que anadas tablas hasta que quieras
                  // anadir mas
            }
            in.close();
            res.close();// Cerramos todo para evitar resource leaks
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    public static void createTabla(FileWriter res, Scanner in) {
        System.out.println("Introduce el nombre de la tabla");
        String tabla = in.nextLine();
        tablas[numTablas] = tabla;
        numTablas++;// Cada tabla que anades se suma al array global
        System.out.println("La tabla se llama " + tabla);
        /*
         * System.out.println("¿Cuantos datos tiene la tabla?");
         * String length = in.nextLine();
         * String[] todosDatos = new String[length];
         */
        // Dejo esto como una manera posible de hacer que el array tenga una longitud
        // variable
        String[] todosDatos = new String[50];
        int numerodeDatos = 0;
        try {
            res.write("CREATE TABLE " + tabla + "(\n");
            boolean datos = true;
            while (datos) {
                System.out.println("¿Quieres introducir datos? y/n");
                String nuevosDatos = in.nextLine();
                if (nuevosDatos.equals("y")) {
                    todosDatos[numerodeDatos] = anadeDatos(res, in);// anadeDatos devuelve en String el nombre del dato
                                                                    // anadido, esto luego se usa para las primary keys
                                                                    // y las foreign keys
                    numerodeDatos++;
                } else if (nuevosDatos.equals("n")) {
                    datos = false;// Sales del bucle
                    System.out.println("Datos anadidos a la tabla");
                } else {
                    System.out.println("RESULTADO INESPERADO");
                }
            }
            System.out.println("¿Cual es la primary key?");
            imprimeArray(todosDatos, numerodeDatos);// El array imprime solo los necesarios, si has creado el array con
                                                    // la longitud exacta puedes cambiar la funcion
            Integer primaryKey = in.nextInt();// Al usar nextInt no leera el /n porque es un char no un int, deberemos
                                              // quemar un .nextLine() luego
            res.write("PRIMARY KEY (" + todosDatos[primaryKey - 1] + "),\n");
            System.out.println("¿Tiene el código Foreign Keys? y/n");
            in.nextLine();// Quemamos un .nextLine() por lo explicado arriba
            String foreign = in.nextLine();
            if (foreign.equals("y")) {
                boolean hayforeign = true;
                while (hayforeign) {
                    System.out.println("¿Quieres anadir una Foreign Key? y/n");
                    String foreign2 = in.nextLine();
                    if (foreign2.equals("y")) {
                        anadeForeign(res, in, todosDatos, numerodeDatos);// Metemos el array para poder imprimir los
                                                                         // menus desplegables
                    } else if (foreign2.equals("n")) {
                        hayforeign = false;
                        System.out.println("Todos los datos anadidos");
                    } else {
                        System.out.println("RESULTADO INESPERADO");
                    }
                }
            }
            res.write(");\n\n");
            System.out.println("Tabla creada con exito");
            // in2.close();
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    public static String anadeDatos(FileWriter res, Scanner in) {
        System.out.println("Introduce el nombre del dato");
        String dato = in.nextLine();
        System.out.println("Introduce el tipo de dato\n1. Int\n2. String\n3. Float\n4. Boolean\n5. Date");
        String datoType = in.nextLine();
        System.out.println("¿Es este dato unico y tiene que estar rellenado? y/n (Por ejemplo, es primary key)");
        String uniquenotnull = in.nextLine();// Queremos hacer todo lo posible fuera del trycatch asi que lo
                                             // solucionamos fuera
        try {
            res.write(dato + " ");
            if (datoType.equals("1") || datoType.equals("Int") || datoType.equals("int")) {// Aceptamos varios inputs
                                                                                           // por comodidad del usuario
                res.write("INT");
            } else if (datoType.equals("2") || datoType.equals("String") || datoType.equals("string")) {
                res.write("VARCHAR(1500)");
            } else if (datoType.equals("3") || datoType.equals("Float") || datoType.equals("float")) {
                res.write("FLOAT");
            } else if (datoType.equals("4") || datoType.equals("Boolean") || datoType.equals("boolean")) {
                res.write("BOOL");
            } else if (datoType.equals("5") || datoType.equals("Date") || datoType.equals("date")) {
                res.write("DATE");
            } else {
                System.out.println("RESULTADO INESPERADO, RESETEA");// Si fallas en este punto en realidad solamente no
                                                                    // se escribe el tipo de dato y luego tienes que
                                                                    // meterlo a mano o resetear
            }
            if (uniquenotnull.equals("y")) {
                res.write(" UNIQUE NOT NULL");
            }
            res.write(",\n");

        } catch (java.io.IOException e) {
            System.out.println(e);
        }
        return dato;
    }

    public static void anadeForeign(FileWriter res, Scanner in, String[] todosDatos, int numerodeDatos) {
        System.out.println("Introduce el dato que es Foreign Key");
        imprimeArray(todosDatos, numerodeDatos);
        Integer key = in.nextInt() - 1;// El usuario mete el numero de la posicion +1 porque no empieza en cero
        in.nextLine();
        System.out.println("Introduce la tabla referenciada con su numero");
        imprimeArray(tablas, numTablas);// Por esto usamos la variable global, si no habria que hacerla viajar por todas
                                        // las funciones que es mas aburrido cuando en el fondo solo hay que hacer un
                                        // schema
        Integer tablareferencia = in.nextInt() - 1;
        in.nextLine();
        System.out.println("¿Que dato referencia? Poner a mano");
        String datoExacto = in.nextLine();
        System.out.println("Si se borra debería\n1. Restringir\n2. Actualizar");
        String onDelete = in.nextLine();// La respuesta a estos dos viene directamente en el pdf explicativo de la
                                        // practica
        System.out.println("Si se actualiza debería\n1. Restringir\n2. Actualizar");
        String onUpdate = in.nextLine();
        try {
            res.write("FOREIGN KEY (" + todosDatos[key] + ")\nREFERENCES " + tablas[tablareferencia] + " (" + datoExacto
                    + ")\n");
            if (onDelete.equals("1") || onDelete.equals("Restringir")) {
                res.write("ON DELETE RESTRICT");
            } else if (onDelete.equals("2") || onDelete.equals("Actualizar")) {
                res.write("ON DELETE CASCADE");
            } else {
                System.out.println("RESULTADO INESPERADO RESETEA");
            }
            if (onUpdate.equals("1") || onUpdate.equals("Restringir")) {
                res.write(" ON UPDATE RESTRICT\n");
            } else if (onUpdate.equals("2") || onUpdate.equals("Actualizar")) {
                res.write(" ON UPDATE CASCADE\n");
            } else {
                System.out.println("RESULTADO INESPERADO RESETEA");
            }
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    public static void imprimeArray(String[] arr, int numerodeDatos) {
        int n = 0;
        while (numerodeDatos > n) {// Esto no es necesario si estas creando el array con el tamano adecuado
                                   // directamente como esta explicado antes, yo no lo hago porque es un poco menos
                                   // eficiente en procesamiento aunque salve un poco de espacio
            int y = n + 1;
            System.out.println(y + ". " + arr[n]);
            n++;
        }
    }

}
