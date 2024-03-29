//Codigo creado por Santiago Monreal para uso libre para quien quiera. Aun tiene unos bugs que estoy resolviendo.

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;

public class creador_SQL {

    static String[] tablas = new String[50];
    static int numTablas = 0;

    public static void main(String[] args) throws FileNotFoundException {
        try {
            File arch = new File(
                    "C:\\Users\\santi\\OneDrive\\Documentos\\Santiago uni\\Bases de datos\\Proyecto\\proyecto_SQL\\src\\resultado.txt");
            FileWriter res = new FileWriter(arch);
            Scanner in = new Scanner(System.in);
            System.out.println("Introduce el nombre del esquema");
            String schema = in.nextLine();
            System.out.println("El esquema se llama " + schema);
            res.write("DROP SCHEMA IF EXISTS " + schema + ";\n\n" + "CREATE SCHEMA " + schema + ";\nUSE " + schema
                    + ";\n\n");
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
                }
            }
            in.close();
            res.close();
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    public static void createTabla(FileWriter res, Scanner in) {
        System.out.println("Introduce el nombre de la tabla");
        String tabla = in.nextLine();
        tablas[numTablas] = tabla;
        numTablas++;
        System.out.println("La tabla se llama " + tabla);
        String[] todosDatos = new String[50];
        int numerodeDatos = 0;
        try {
            res.write("CREATE TABLE " + tabla + "(\n");
            boolean datos = true;
            while (datos) {
                System.out.println("¿Quieres introducir datos? y/n");
                String nuevosDatos = in.nextLine();
                if (nuevosDatos.equals("y")) {
                    todosDatos[numerodeDatos] = anadeDatos(res, in);
                    numerodeDatos++;
                } else if (nuevosDatos.equals("n")) {
                    datos = false;
                    System.out.println("Datos anadidos a la tabla");
                } else {
                    System.out.println("RESULTADO INESPERADO");
                }
            }
            System.out.println("¿Cual es la primary key?");
            imprimeArray(todosDatos);
            Integer primaryKey = in.nextInt();
            res.write("PRIMARY KEY (" + todosDatos[primaryKey - 1] + ")\n");
            System.out.println("¿Tiene el código Foreign Keys? y/n");
            String foreign = in.nextLine();
            if (foreign.equals("y")) {
                boolean hayforeign = true;
                while(hayforeign){
                    System.out.println("¿Quieres anadir una foreign key? y/n");
                    String foreign2 = in.nextLine();
                    if(foreign2.equals("y")){
                        anadeForeign(res, in);
                    } else if (foreign.equals("n")){
                        hayforeign = false;
                        System.out.println("Todos los datos anadidos");
                    } else {
                        System.out.println("RESULTADO INESPERADO");
                    }
                }
            }
            res.write(");\n\n");
            System.out.println("Tabla creada con exito");
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
        String uniquenotnull = in.nextLine();
        try {
            res.write(dato + " ");
            if (datoType.equals("1") || datoType.equals("Int") || datoType.equals("int")) {
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
                System.out.println("RESULTADO INESPERADO, RESETEA");
            }
            if (uniquenotnull.equals("y")) {
                res.write(" UNIQUE NOT NULL,\n");
            } else {
                res.write(",\n");
            }
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
        return dato;
    }

    public static void anadeForeign (FileWriter res, Scanner in){
        System.out.println("Introduce el nombre de la key");
        String key = in.nextLine();
        System.out.println("Introduce la tabla referenciada con su numero");
        imprimeArray(tablas);
        Integer tablareferencia = in.nextInt()-1;
        System.out.println("¿Que dato referencia? Poner a mano");
        String datoExacto = in.nextLine();
        System.out.println("Si se borra debería\n1. Restringir\n2. Actualizar");
        String onDelete = in.nextLine();
        System.out.println("Si se actualiza debería\n1. Restringir\n2. Actualizar");
        String onUpdate = in.nextLine();
        try {
            res.write("FOREIGN KEY (" + key + ")\nREFERENCES " + tablas[tablareferencia] + " (" + datoExacto + ")\n");
            if (onDelete.equals("1") || onDelete.equals("Restringir")){
                res.write("ON DELETE RESTRICT");
            } else if (onDelete.equals("2") || onDelete.equals("Actualizar")){
                res.write("ON DELETE CASCADE");
            } else {
                System.out.println("RESULTADO INESPERADO RESETEA");
            }
            if (onUpdate.equals("1") || onUpdate.equals("Restringir")){
                res.write("ON UPDATE RESTRICT");
            } else if (onUpdate.equals("2") || onUpdate.equals("Actualizar")){
                res.write("ON UPDATE CASCADE");
            } else {
                System.out.println("RESULTADO INESPERADO RESETEA");
            }
        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    public static void imprimeArray(String[] arr) {
        int n = 0;
        while (arr.length > n) {
            int y = n + 1;
            System.out.println(y + ". " + arr[n]);
            n++;
        }
    }

}
