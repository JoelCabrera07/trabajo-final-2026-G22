package persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Conexion instancia;
    private final Connection conexion;

    //Constructor para el patron Singleton
    private Conexion() throws SQLException {
        //Genera el archivo juego.db en la raiz automaticamente
        conexion = DriverManager.getConnection("jdbc:sqlite:juego.db");
    }
    public static synchronized Conexion getInstancia() throws SQLException {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
    public Connection get() {
        return conexion;
    }
}
