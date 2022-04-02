import me.scolastico.tools.ebean.DatabaseMigrationGenerator
import java.io.IOException

/**
 * Class to generate database migration files.
 * Should be executed after changing database classes.
 * Don't forget to change the version every time.
 */
internal object GenerateDBMigration {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        DatabaseMigrationGenerator.create("1.0.0", "init")
    }
}
