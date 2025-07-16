package main.java.org.ui;

public interface GameRenderer {
    void drawBox(String title, String[] content);

    void updateBoxContent(String[] content, int startRow);

    void animatedPrint(String text, int delay);

    void clearScreen();

    void clearLine();

    void moveCursorUp(int lines);

    void sleep(int milliseconds);

    void drawProgressBar(int current, int max, int width);

    String getInput();

    int getValidInput(int min, int max);

    void flush();

    int getCurrentScreenHeight();
}