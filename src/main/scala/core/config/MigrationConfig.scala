package core.config


import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.MigrateResult
trait MigrationConfig extends DbConfig {
  private val flyway = Flyway.configure.dataSource(dbUrl, dbUser, dbPassword).load

  def migrate(): MigrateResult = {
    flyway.repair()
    flyway.migrate()
  }

  def reloadSchema(): MigrateResult = {
    flyway.clean()
    flyway.migrate()
  }

 // def dropDatabase(): Unit = flyway.clean()
}

