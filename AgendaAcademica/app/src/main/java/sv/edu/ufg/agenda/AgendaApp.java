package sv.edu.ufg.agenda;

import android.app.Application;
import java.io.File;
import sv.edu.ufg.agenda.dao.DAOFactory;

public class AgendaApp extends Application {
    private DAOFactory daos;
    @Override public void onCreate() {
        super.onCreate();
        daos = new DAOFactory(new File(getFilesDir(), "datos"));
    }
    public DAOFactory daos() { return daos; }
}
