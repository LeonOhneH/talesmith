package main.java.org.ui;

public interface GameRenderer {
    void drawBox(String title, String[] content);

    void animatedPrint(String text, int delay);

    void clearScreen();

    void sleep(int milliseconds);

    String getInput();

    int getValidInput(int min, int max);

    void flush();
}