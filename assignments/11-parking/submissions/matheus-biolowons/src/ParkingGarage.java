import java.util.Scanner;

public class ParkingGarage {
    public static double calculateCharges(double hours) {
        if (hours <= 3)  return 2;

        double extraHours = Math.ceil(hours) - 3;
        double totalCharge = 2 + (extraHours * 0.50);

        return Math.min(totalCharge, 10);
    }

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {

            double totalReceipts = 0.0;
            int clientCount = 1;

            System.out.println("Bem-vindo ao sistema de cálculo do Estacionamento.");
            System.out.println("Regras: R$2.00 min (3h), +R$0.50/h add, Max R$10.00.");

            while (true) {
                System.out.printf("%nDigite o número de horas estacionadas para o cliente %d (ou -1 para sair): ", clientCount);

                if (!scanner.hasNextDouble()) {
                    System.out.println("Entrada inválida. Por favor digite um número.");
                    scanner.next();
                    continue;
                }
                double hours = scanner.nextDouble();

                if (hours == -1) break;
                if (hours < 0 || hours > 24) {
                    System.out.println("Erro: As horas devem estar entre 0 e 24.");
                    continue;
                }

                double charge = calculateCharges(hours);
                totalReceipts += charge;

                System.out.printf("Cliente %d: Taxa de estacionamento: R$ %.2f%n", clientCount, charge);
                clientCount++;
            }

            System.out.println("\n-------------------------------------------");
            System.out.printf("Total arrecadado ontem: R$ %.2f%n", totalReceipts);
            System.out.println("-------------------------------------------");
        }
    }
}