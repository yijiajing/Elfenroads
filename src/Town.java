public class Town {

    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private TownPanel panel;

    public Town(String name, int x, int y, int pWidth, int pHeight, GameScreen pScreen) {
        this.name = name;
        this.x = pScreen.getWidth() * x / 1440;
        this.y = pScreen.getHeight() * y / 900;
        this.width = pScreen.getWidth() * pWidth / 1440;
        this.height = pScreen.getHeight() * pHeight / 900;
        this.panel = new TownPanel(name, this.x, this.y, width, height, pScreen);
    }

    public String getName() {
        return name;
    }

    public TownPanel getPanel() {
        return panel;
    }
}
