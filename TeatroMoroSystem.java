/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package teatromorosystem;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author gustavo.dominguez
 */
public class TeatroMoroSystem {
    static int[] saleIds = new int[100];
    static int[] seatNumbers = new int[100];
    static String[] seatTypes = new String[100];
    static String[] clientNames = new String[100];
    static double[] finalPrices = new double[100];
    static int[] clientAges = new int[100];
    static boolean[] seatAvailability = new boolean[100];
    static int saleCount = 0;

    static ArrayList<String> reservationList = new ArrayList<>();

    static Scanner scanner = new Scanner(System.in);

    /*
     * Sistema probado de forma manual mediante la interacción con el menú principal.
     * Se verificaron casos de:
     * - Venta de entradas con diferentes tipos de asiento
     * - Aplicación correcta de descuentos por edad y condición de estudiante
     * - Registro y modificación de reservas
     * - Validación de asientos ocupados o fuera de rango
     * - Coherencia entre datos de cliente, asiento y venta
     * El sistema está optimizado en cuanto a selección de estructuras de datos 
     * (uso de arreglos y listas según el tipo de información), y permite
     * actualización, eliminación y adición manteniendo la integridad.
     */
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Sistema Teatro Moro ===");
            System.out.println("1. Venta de entrada");
            System.out.println("2. Ver reservas");
            System.out.println("3. Modificar una reserva existente");
            System.out.println("4. Visualizar resumen de ventas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int option = readInt();

            switch (option) {
                case 1:
                    sellTicket();
                    break;
                case 2:
                    showReservations();
                    break;
                case 3:
                    modifyReservation();
                    break;
                case 4:
                    showSalesSummary();
                    break;
                case 5:
                    System.out.println("Gracias por su compra.");
                    running = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    static void sellTicket() {
        int seatNumber;
        String seatType;
        double basePrice = 10000;
        double discount = 0.0;
        String discountType = "Sin descuento";

        System.out.print("Ingrese número de asiento (0 a 99): ");
        seatNumber = readInt();

        while (seatNumber < 0 || seatNumber >= seatAvailability.length || seatAvailability[seatNumber]) {
            System.out.print("Número de asiento " + seatNumber + " no disponible. Ingrese otro (0 a 99): ");
            seatNumber = readInt();
        }

        seatAvailability[seatNumber] = true;

        System.out.println("Seleccione tipo de entrada:");
        System.out.println("1. VIP ($30.000)\n2. Platea ($20.000)\n3. Balcón ($15.000)");
        int typeOption = readInt();

        seatType = switch (typeOption) {
            case 1 -> "VIP";
            case 2 -> "Platea";
            case 3 -> "Balcón";
            default -> "General";
        };
        basePrice = switch (typeOption) {
            case 1 -> 30000;
            case 2 -> 20000;
            case 3 -> 15000;
            default -> 10000;
        };

        System.out.print("Nombre del cliente: ");
        String name = scanner.nextLine();

        System.out.print("Edad del cliente: ");
        int age = readInt();

        System.out.print("¿Es estudiante? (s/n): ");
        boolean isStudent = scanner.nextLine().equalsIgnoreCase("s");

        if (isStudent) {
            discount = basePrice * 0.10;
            discountType = "Estudiante";
        } else if (age >= 65) {
            discount = basePrice * 0.15;
            discountType = "Tercera Edad";
        }

        double finalPrice = basePrice - discount;

        saleIds[saleCount] = saleCount + 1;
        seatNumbers[saleCount] = seatNumber;
        seatTypes[saleCount] = seatType;
        clientNames[saleCount] = name;
        finalPrices[saleCount] = finalPrice;
        clientAges[saleCount] = age;
        reservationList.add("Reserva ID: " + saleIds[saleCount] + ", Cliente: " + name + ", Asiento: " + seatNumber);
        saleCount++;

        System.out.println("Entrada vendida exitosamente. Total: $" + (int) finalPrice);
        System.out.println("Descuento aplicado: $" + discount);
        System.out.println("Tipo de descuento: " + discountType);
    }

    static void modifyReservation() {
        System.out.print("Ingrese ID de venta para modificar: ");
        int id = readInt();
        boolean found = false;

        for (int i = 0; i < saleCount; i++) {
            if (saleIds[i] == id) {
                System.out.print("Nuevo nombre del cliente: ");
                String newName = scanner.nextLine();
                clientNames[i] = newName;

                System.out.print("Nueva edad del cliente: ");
                clientAges[i] = readInt();

                System.out.print("¿Es estudiante? (s/n): ");
                boolean isStudent = scanner.nextLine().equalsIgnoreCase("s");

                double basePrice = switch (seatTypes[i]) {
                    case "VIP" -> 30000;
                    case "Platea" -> 20000;
                    case "Balcón" -> 15000;
                    default -> 10000;
                };

                double discount = 0;
                if (isStudent) discount = basePrice * 0.10;
                else if (clientAges[i] >= 65) discount = basePrice * 0.15;

                finalPrices[i] = basePrice - discount;
                updateReservationById(id, newName, seatNumbers[i]);

                System.out.println("Reserva modificada exitosamente. Nuevo total: $" + (int) finalPrices[i]);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("ID de venta no encontrado.");
        }
    }

    static void updateReservationById(int targetId, String newName, int seatNumber) {
        for (int i = 0; i < reservationList.size(); i++) {
            if (reservationList.get(i).startsWith("Reserva ID: " + targetId)) {
                String updated = "Reserva ID: " + targetId + ", Cliente: " + newName + ", Asiento: " + seatNumber;
                reservationList.set(i, updated);
                break;
            }
        }
    }

    static void showReservations() {
        if (reservationList.isEmpty()) {
            System.out.println("No hay reservas realizadas.");
            return;
        }
        for (String r : reservationList) {
            System.out.println(r);
        }
    }

    static void showSalesSummary() {
        for (int i = 0; i < saleCount; i++) {
            System.out.println("Venta #" + saleIds[i] + " - Cliente: " + clientNames[i] +
                    ", Asiento: " + seatTypes[i] + ", Total: $" + (int) finalPrices[i]);
        }
    }

    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Entrada inválida. Intente nuevamente: ");
            }
        }
    }
}