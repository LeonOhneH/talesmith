package main.java.org.ui;

import java.util.Scanner;

public class ConsoleRenderer implements GameRenderer {
    private Scanner scanner;
    private int currentScreenHeight = 0;

    public ConsoleRenderer() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void clearScreen() {
        // Statt den gesamten Bildschirm zu löschen, gehen wir zum Anfang zurück
        System.out.print("\033[H");
        System.out.flush();
    }

    @Override
    public void clearLine() {
        System.out.print("\r\033[K"); // Löscht die aktuelle Zeile
        System.out.flush();
    }

    @Override
    public void moveCursorUp(int lines) {
        System.out.print("\033[" + lines + "A");
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
    public void drawProgressBar(int current, int max, int width) {
        clearLine();

        int filledWidth = (int) ((double) current / max * width);
        StringBuilder bar = new StringBuilder("[");

        for (int i = 0; i < filledWidth; i++) {
            bar.append('#');
        }

        for (int i = filledWidth; i < width; i++) {
            bar.append(' ');
        }

        bar.append("] ").append(current).append("/").append(max);
        System.out.print("\r" + bar.toString());
        System.out.flush();
    }

    @Override
    public void drawBox(String title, String[] content) {
        // Speichern wir die Höhe des aktuellen Blocks
        currentScreenHeight = content.length + 4; // 4 für Titel und Rahmen

        int maxWidth = 70;
        String horizontal = "═".repeat(maxWidth - 2);

        System.out.print("\033[H"); // Cursor an den Anfang
        System.out.println("╔" + horizontal + "╗");

        // Titel zentrieren
        if (title != null && !title.isEmpty()) {
            int padding = (maxWidth - 2 - title.length()) / 2;
            String titleLine = " ".repeat(padding) + title + " ".repeat(maxWidth - 2 - padding - title.length());
            System.out.println("║" + titleLine + "║");
            System.out.println("╠" + horizontal + "╣");
            currentScreenHeight += 1; // Extra Zeile für Trennlinie
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
        System.out.flush();
    }

    @Override
    public void updateBoxContent(String[] content, int startRow) {
        int maxWidth = 70;

        // Bewege den Cursor zur angegebenen Zeile
        System.out.print("\033[" + (startRow + 2) + ";1H"); // +2 für Rahmen und ggf. Titel

        for (String line : content) {
            clearLine();
            int padding = maxWidth - 2 - line.replaceAll("[\u2600-\u27BF]", "").length();
            if (padding < 0)
                padding = 0;
            String contentLine = line + " ".repeat(padding);
            if (contentLine.length() > maxWidth - 2) {
                contentLine = contentLine.substring(0, maxWidth - 2);
            }
            System.out.println("║" + contentLine + "║");
        }
        System.out.flush();
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

    @Override
    public int getCurrentScreenHeight() {
        return currentScreenHeight;
    }
}