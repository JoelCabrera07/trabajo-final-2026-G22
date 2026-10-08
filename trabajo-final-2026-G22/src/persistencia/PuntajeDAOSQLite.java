package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Puntaje;
import modelo.PuntajeDAO;

public class PuntajeDAOSQLite implements PuntajeDAO {
    private final Connection conexion;

    //Recibe la conexion por constructor (Inversion de dependencia)
    public PuntajeDAOSQLite(Connection conexion){
        this.conexion = conexion;
        crearTablaSiNoExiste();
    }
    //Metodo de seguridad para que la base de datos se arme sola la primera vez que abris el juego
    private void crearTablaSiNoExiste(){
        String sql = "CREATE TABLE IF NOT EXISTS puntajes (jugador TEXT,puntos INTEGER)";
        try(Statement stmt = conexion.createStatement()){
        stmt.execute(sql);
    } catch (SQLException e){
        System.err.println("Error al crear tabla: " + e.getMessage());
    }
}
@Override
public boolean guardar(Puntaje p){
    //Usamos los huecos (?) para evitar inyeccion SQL
    String sql = "INSERT INTO puntajes (jugador, puntos) VALUES (?,?)";

    //El try-with-resources cierra el PreparedStatement, pero NO la conexion principal
    try(PreparedStatement s = conexion.prepareStatement(sql)){
        s.setString(1,p.getJugador());
        s.setInt(2,p.getPuntos());
        return s.executeUpdate() == 1; // Devuelve true si afecto 1 fila
    } catch (SQLException e){
        System.err.println("Error al guardar puntaje: " + e.getMessage());
        return false;
    }
}
@Override
public List<Puntaje> mejores (int cantidad){
    List<Puntaje> top = new ArrayList <>();
   // Ordemanos por puntos de mayor a menor y limitamos la cantidad
   String sql = "SELECT jugador, puntos FROM puntajes ORDER BY puntos DESC LIMIT ?";

   try (PreparedStatement s = conexion.prepareStatement(sql)){
    s.setInt(1,cantidad);

    try (ResultSet filas = s.executeQuery()){
        while (filas.next()) {
            String jugador = filas.getString("jugador");
            int puntos = filas.getInt("puntos");
            top.add(new Puntaje(jugador,puntos));
        }
    }
   } catch (SQLException e){
    System.err.println("Error al leer mejores puntajes: " + e.getMessage());
   }
   return top;
}
}