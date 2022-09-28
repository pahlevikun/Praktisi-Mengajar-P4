package id.pahlevikun.praktisimengajar.p4.builder;

public class SimpleSingleton {
    private static final SimpleSingleton ourInstance = new SimpleSingleton();

    public static SimpleSingleton getInstance() {
        return ourInstance;
    }

    private int count = 0;

    private SimpleSingleton() {
    }

    public void addIntent() {
        count = count + 1;
    }

    public int getIntentCount() {
        return count;
    }
}
