package main.java.org.ui;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleRenderer implements GameRenderer {
    private Scanner scanner;
    private int currentScreenHeight = 0;
    private final int BOX_WIDTH = 70; // Konstante Boxbreite
    private static final Pattern EMOJI_PATTERN = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27BF]");

    public ConsoleRenderer() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void clearScreen() {
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

        String horizontal = "═".repeat(BOX_WIDTH - 2);

        System.out.print("\033[H"); // Cursor an den Anfang
        System.out.println("╔" + horizontal + "╗");

        // Titel zentrieren
        if (title != null && !title.isEmpty()) {
            int padding = (BOX_WIDTH - 2 - title.length()) / 2;
            String titleLine = " ".repeat(padding) + title + " ".repeat(BOX_WIDTH - 2 - padding - title.length());
            System.out.println("║" + titleLine + "║");
            System.out.println("╠" + horizontal + "╣");
            currentScreenHeight += 1; // Extra Zeile für Trennlinie
        }

        // Content mit exakter Ausrichtung
        for (String line : content) {
            String formattedLine = formatLineForBox(line, BOX_WIDTH - 2);
            System.out.println("║" + formattedLine + "║");
        }

        // Fülle den Rest bis zur gewünschten Höhe (ca. 20 Zeilen)
        for (int i = content.length; i < 15; i++) {
            System.out.println("║" + " ".repeat(BOX_WIDTH - 2) + "║");
        }

        System.out.println("╚" + horizontal + "╝");
        System.out.flush();
    }

    @Override
    public void updateBoxContent(String[] content, int startRow) {
        // Bewege den Cursor zur angegebenen Zeile
        System.out.print("\033[" + (startRow + 2) + ";1H"); // +2 für Rahmen und ggf. Titel

        for (String line : content) {
            clearLine();
            String formattedLine = formatLineForBox(line, BOX_WIDTH - 2);
            System.out.println("║" + formattedLine + "║");
        }
        System.out.flush();
    }

    // Verbesserte Methode für bessere Emoji-Unterstützung
    private String formatLineForBox(String line, int targetWidth) {
        if (line == null)
            line = "";

        // Wenn es eine Tabellenzeile ist, behandle sie speziell
        if (line.contains("|")) {
            return formatTableLineForBox(line, targetWidth);
        }

        // Berechne die visuelle Länge unter Berücksichtigung von Emojis
        int visualLength = calculateVisualLength(line);

        // Wenn die Zeile zu lang ist, kürzen
        if (visualLength > targetWidth) {
            return truncateToVisualLength(line, targetWidth - 3) + "...";
        }

        // Fülle mit Leerzeichen auf für exakte Breite
        return line + " ".repeat(targetWidth - visualLength);
    }

    // Spezialbehandlung für Tabellenzeilen
    private String formatTableLineForBox(String line, int targetWidth) {
        // Wir wissen, dass Tabellenzeilen eine feste Struktur haben
        // und müssen sicherstellen, dass sie immer gleich breit sind

        // Bei Tabellenzeilen mit Formatierung kann die visuelle Länge unterschiedlich
        // sein
        int visualLength = calculateVisualLength(line);

        if (visualLength > targetWidth) {
            // Kürzen, wenn zu lang
            return truncateToVisualLength(line, targetWidth - 3) + "...";
        }

        // Fülle mit Leerzeichen auf für exakte Breite
        // Wichtig: Exakt so viele Leerzeichen, dass die Gesamtbreite targetWidth ist
        return line + " ".repeat(targetWidth - visualLength);
    }

    // Verbesserte Methode zur Berechnung der visuellen Länge mit korrekter
    // Emoji-Handhabung
    private int calculateVisualLength(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        // Extrahiere alle Emoji-Matches und berechne ihre visuelle Länge als 1
        java.util.regex.Matcher matcher = EMOJI_PATTERN.matcher(text);

        // Ersetze zuerst alle Emojis durch einen einzelnen Platzhalter
        // und zähle dann die resultierende Länge
        String processedText = matcher.replaceAll("X");

        return processedText.length();
    }

    // Hilfsmethode zum Kürzen eines Strings auf eine bestimmte visuelle Länge
    private String truncateToVisualLength(String text, int maxVisualLength) {
        // Wenn Text null oder leer ist oder maxVisualLength <= 0, gib leeren String
        // zurück
        if (text == null || text.isEmpty() || maxVisualLength <= 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int currentVisualLength = 0;
        int index = 0;

        while (index < text.length() && currentVisualLength < maxVisualLength) {
            char currentChar = text.charAt(index);

            // Prüfe auf Emoji am aktuellen Index
            if (isEmojiStart(text, index)) {
                // Emoji gefunden, füge es zum Ergebnis hinzu
                int emojiLength = getEmojiLength(text, index);
                result.append(text.substring(index, index + emojiLength));
                index += emojiLength;
                currentVisualLength += 1; // Ein Emoji zählt als ein Zeichen
            } else {
                // Kein Emoji, normales Zeichen
                result.append(currentChar);
                index++;
                currentVisualLength++;
            }
        }

        return result.toString();
    }

    // Prüft, ob ein Emoji am gegebenen Index beginnt
    private boolean isEmojiStart(String text, int index) {
        if (index >= text.length())
            return false;

        char c = text.charAt(index);
        return (c >= '\u2600' && c <= '\u27BF') ||
                (c == '\uD83C' || c == '\uD83D');
    }

    // Bestimmt die Länge eines Emojis ab dem gegebenen Index
    private int getEmojiLength(String text, int index) {
        // Die meisten einfachen Emojis sind 1-2 Zeichen lang
        if (index + 1 >= text.length())
            return 1;

        char c = text.charAt(index);
        if (c >= '\u2600' && c <= '\u27BF')
            return 1;

        if (c == '\uD83C' || c == '\uD83D') {
            // Surrogate pair, typischerweise 2 Zeichen
            return 2;
        }

        return 1;
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