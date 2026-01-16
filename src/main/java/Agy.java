public class Agy {
    public static void main(String[] args) {
        printMessage("Hello! I'm Agy\nWhat can I do for you?");

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                printMessage("Bye. Hope to see you again soon!");
                break;
            }
            printMessage(input);
        }
        scanner.close();
    }

    private static void printMessage(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(" " + message.replace("\n", "\n "));
        System.out.println("____________________________________________________________");
    }
}
