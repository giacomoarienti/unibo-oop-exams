package a01a.e2;

public class GUI {
    public GUI(int size) {
        final Controller controller = new ControllerImpl(size);
        final View view = new ViewImpl(size, controller);
        view.show();
    }

    public static void main(String[] args) {
        new GUI(5);
    }
}
