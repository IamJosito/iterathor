package indexathor;
// Generated 2 feb. 2021 19:41:54 by Hibernate Tools 4.3.1



/**
 * SongsId generated by hbm2java
 */
public class SongsId  implements java.io.Serializable {


     private String nomCancion;
     private String artista;

    public SongsId() {
    }

    public SongsId(String nomCancion, String artista) {
       this.nomCancion = nomCancion;
       this.artista = artista;
    }
   
    public String getNomCancion() {
        return this.nomCancion;
    }
    
    public void setNomCancion(String nomCancion) {
        this.nomCancion = nomCancion;
    }
    public String getArtista() {
        return this.artista;
    }
    
    public void setArtista(String artista) {
        this.artista = artista;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SongsId) ) return false;
		 SongsId castOther = ( SongsId ) other; 
         
		 return ( (this.getNomCancion()==castOther.getNomCancion()) || ( this.getNomCancion()!=null && castOther.getNomCancion()!=null && this.getNomCancion().equals(castOther.getNomCancion()) ) )
 && ( (this.getArtista()==castOther.getArtista()) || ( this.getArtista()!=null && castOther.getArtista()!=null && this.getArtista().equals(castOther.getArtista()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getNomCancion() == null ? 0 : this.getNomCancion().hashCode() );
         result = 37 * result + ( getArtista() == null ? 0 : this.getArtista().hashCode() );
         return result;
   }   


}

