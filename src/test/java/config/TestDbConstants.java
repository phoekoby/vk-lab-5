package config;

import org.jetbrains.annotations.NotNull;

public class TestDbConstants {
    public static final @NotNull String CONNECTION = "jdbc:postgresql://127.0.0.1:5432/";

    public static final @NotNull String DB_NAME = "test";

    public static final @NotNull String USERNAME = "postgres";

    public static final @NotNull String PASSWORD = "postgres";

    private TestDbConstants(){}
}
