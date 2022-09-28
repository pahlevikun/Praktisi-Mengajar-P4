package id.pahlevikun.praktisimengajar.p4.builder;

public class BuilderSimpleSingleton {
    private static BuilderSimpleSingleton ourInstance;

    public static BuilderSimpleSingleton getInstance() {
        if (ourInstance == null) {
            throw new RuntimeException("Singleton belum diinit, Mohon gunakan BuilderSimpleSingleton.Builder().init()");
        }
        return ourInstance;
    }

    private int count;

    private BuilderSimpleSingleton(int initialCount) {
        this.count = initialCount;
    }

    public void countIntentToPermission() {
        count = count + 1;
    }

    public int getPermissionCount() {
        return count;
    }

    public static class Builder {
        private int count = 0;

        public Builder setInitialCount(int count) {
            this.count = count;
            return this;
        }

        public BuilderSimpleSingleton init() {
            ourInstance = new BuilderSimpleSingleton(count);
            return ourInstance;
        }
    }
}
