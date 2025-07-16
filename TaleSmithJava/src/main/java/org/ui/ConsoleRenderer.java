package main.java.org.ui;

import java.util.Scanner;

public class ConsoleRenderer implements GameRenderer {
    private Scanner scanner;

    public ConsoleRenderer() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void clearScreen() {
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }

    @Override
    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void animatedPrint(String text, int delay) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            sleep(delay);
        }
    }

    @Override
    public void drawBox(String title, String[] content) {
        clearScreen();

        int maxWidth = 70;
        String horizontal = "═".repeat(maxWidth - 2);

        System.out.println("╔" + horizontal + "╗");

        // Titel zentrieren
        if (title != null && !title.isEmpty()) {
            int padding = (maxWidth - 2 - title.length()) / 2;
            String titleLine = " ".repeat(padding) + title + " ".repeat(maxWidth - 2 - padding - title.length());
            System.out.println("║" + titleLine + "║");
            System.out.println("╠" + horizontal + "╣");
        }

        // Content
        for (String line : content) {
            int padding = maxWidth - 2 - line.replaceAll("[\u2600-\u27BF]", "").length();
            if (padding < 0)
                padding = 0;
            String contentLine = line + " ".repeat(padding);
            if (contentLine.length() > maxWidth - 2) {
                contentLine = contentLine.substring(0, maxWidth - 2);
            }
            System.out.println("║" + contentLine + "║");
        }

        // Fülle den Rest bis zur gewünschten Höhe (ca. 20 Zeilen)
        for (int i = content.length; i < 15; i++) {
            System.out.println("║" + " ".repeat(maxWidth - 2) + "║");
        }

        System.out.println("╚" + horizontal + "╝");
    }

    @Override
    public String getInput() {
        return scanner.nextLine();
    }

    @Override
    public int getValidInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(getInput());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.print("❌ Ungültige Eingabe (" + min + "-" + max + "): ");
            } catch (NumberFormatException e) {
                System.out.print("❌ Bitte gib eine Zahl ein: ");
            }
        }
    }

    @Override
    public void flush() {
        System.out.flush();
    }
}