/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexathor;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Josito
 */
public class InsertSong {
    private Mp3File mp3;
    private File f;
    public InsertSong(String url){
        try {
            f = new File(url);
            mp3 = new Mp3File(f);
        } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
            Logger.getLogger(InsertSong.class.getName()).log(Level.SEVERE, null, ex);
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

            //Start transacction
            session.beginTransaction();
            System.out.println("Inserto una fila en la tabla DEPARTAMENTOS.");

            //Create a new songId
            SongsId songId = new SongsId();

            //Get the name of the file split by . and get the first part.
            songId.setNomCancion(f.getName().split("\\.")[0]);

            //Create a ID3v2 to get metadata.
            ID3v2 metadata = mp3.getId3v2Tag();
            if(metadata.getArtist() == null) songId.setArtista("Anonymous");
            else songId.setArtista(metadata.getArtist());

            //To get the duration of the song in mins we need to get a long and parse to double
            Long l = mp3.getLengthInSeconds();
            double duration = l.doubleValue()/60;

            //Create a new Songs
            Songs song = new Songs();
            song.setAlbum(metadata.getAlbum());
            song.setAno(metadata.getYear());
            song.setId(songId);
            song.setDuracion(duration);


            //Save and upload to db
            session.save(song);
            session.getTransaction().commit();

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
