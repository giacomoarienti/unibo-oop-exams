package a01a.e2;

public interface View {

    static final String EMPTY_BTN_LABEL = " ";
    static final String ACTIVE_BTN_LABEL = "*";

    public void setController(Controller controller);

    public void show();
}
