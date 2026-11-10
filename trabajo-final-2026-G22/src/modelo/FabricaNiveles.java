package modelo;

// Factory: la unica clase que decide que nivel concreto crear segun el numero de pantalla.
public class FabricaNiveles {

    public static Nivel crear(int pantalla) {
        switch (pantalla) {
            case 1:
                return new Nivel1();
            case 2:
                return new Nivel2();
            case 3:
                return new Nivel3();
            case 4:
                return new Nivel4();
            case 5:
                return new Nivel5();
            case 6:
                return new Nivel6();
            case 7:
                return new Nivel7();
            default:
                throw new IllegalArgumentException("No existe la pantalla " + pantalla);
        }
    }
}
