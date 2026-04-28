package org.nahap.strore.infrastructure.jdbc.migration;

import org.nahap.strore.infrastructure.jdbc.config.JdbcConnectionProvider;
import org.nahap.strore.infrastructure.jdbc.script.SqlScriptRunner;

public class MigrationService {
    private static final String SCHEMA_SCRIPT = "sql/schema.sql";
    private static final String SEED_SCRIPT = "sql/seed.sql";
    private static final String SEED_TABLE = "department";

    private final SqlScriptRunner scriptRunner;

    public MigrationService(JdbcConnectionProvider connectionProvider) {
        this.scriptRunner = new SqlScriptRunner(connectionProvider);
    }

    public void runMigrations() {
        scriptRunner.runScript(SCHEMA_SCRIPT);
        scriptRunner.runScriptIfTableEmpty(SEED_TABLE, SEED_SCRIPT);
    }
}
