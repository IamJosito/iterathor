/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexathor;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Josito
 */
public class InsertSong {
    private File f;
    private HashMap routes = new HashMap();
    private HashMap dbRoutes = new HashMap();
    public InsertSong(String url){
        f = new File(url);
        this.getFiles(f);
        //System.out.println(routes);
        
    }

    private void getFiles(File parentFile){
        
        for(File files : parentFile.listFiles()){
            if(files.isDirectory()){
                this.getFiles(files);
            }else{
                if(files.getName().split("\\.")[1].equals("mp3")){
                    routes.put(files.getName(),files.getParent());
                }
            }
        }
        
    }
    
    public boolean insert(){
        boolean songInserted = true;
        // Create a new session
        HibernateUtil.buildSessionFactory();
        try {
            //Open session
            HibernateUtil.openSessionAndBindToThread();

            //Variable to controlate version
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            String hql = "FROM Songs";
            List<Songs> result = session.createQuery(hql).list();
            
            //Show all data for DB
            for (Object object : result) {
                Songs song = (Songs) object;
                System.out.println(song.getId().getArtista());
            }
            
            //TODO if data not in DB (LIST) insert, else, dont insert
            
            
            //System.out.println(result.get(0));
                    
            //dbRoutes.put(, session.createQuery("SELECT artista FROM songs"));
            
            /*for (Object key : dbRoutes.keySet()) {
                System.out.println("Key: " + key + " Ruta: "+ dbRoutes.get(key));
            }*/
            

            /*for (Object key : routes.keySet()) {
                try {
                    //Start transacction
                    session.beginTransaction();
                    //Create a new songId
                    SongsId songId = new SongsId();
                    Mp3File mp3 = new Mp3File(routes.get(key)+"/"+key);
                    

                    //Create a ID3v2 to get metadata.
                    ID3v2 metadata = mp3.getId3v2Tag();
                    
                    System.out.println("Nombre de la cancion: " + key + " |||| Ruta: " + routes.get(key));
                    
                    songId.setNomCancion(key.toString().split("\\.")[0]);
                    
                    if(metadata.getArtist() == null) songId.setArtista("Anonymous");
                    else songId.setArtista(metadata.getArtist());

                    int duration = (int) mp3.getLengthInSeconds();

                    //Create a new Songs
                    Songs song = new Songs();
                    song.setAlbum(metadata.getAlbum());
                    song.setAno(metadata.getYear());
                    song.setId(songId);
                    song.setDuracion(duration);
                    song.setRuta((String) routes.get(key));

                    //Save and upload to db
                    session.save(song);
                    session.getTransaction().commit();
                    session.clear();
                    
                    
                    
                } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
                    Logger.getLogger(InsertSong.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }*/
            

        } catch (ConstraintViolationException e) {
            System.out.println("DUPLICATE SONG AND ARTIST");
            System.out.printf("MENSAJE: %s%n", e.getMessage());
            System.out.printf("COD ERROR: %d%n", e.getErrorCode());
            System.out.printf("ERROR SQL: %s%n",
            e.getSQLException().getMessage());
            songInserted = false;
        } finally {
            //Cerramos siempre la session incluso si hay una excepcion
            HibernateUtil.closeSessionAndUnbindFromThread();
        }
        //Cerramos la sesion
        HibernateUtil.closeSessionFactory();

        return songInserted;
    }

}
