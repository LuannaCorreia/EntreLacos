package entrelacos.modelo;

public abstract class Plano {
    protected String nomePlano;

     protected Plano(String nomePlano) {
         this.nomePlano = nomePlano;
     }

     public String getNomePlano() {
         return nomePlano;
     }

     public abstract int getLimiteCurtidas();
}