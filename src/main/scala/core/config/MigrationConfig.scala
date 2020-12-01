package core.config


/**
 * Config file for database migration
 */
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
trait MigrationConfig extends DbConfig {
  private val flyway = Flyway.configure.dataSource(dbUrl, dbUser, dbPassword).load

  def migrate(): MigrateResult = {
    //Uncomment this line in case the database needs to be repaired
    // flyway.repair()
    flyway.migrate()
  }

  // By default this cleans the whole database and instantiates a new person table
  def reloadSchema(): MigrateResult = {
    flyway.clean()
    flyway.migrate()
  }

  // def dropDatabase(): Unit = flyway.clean()
}

