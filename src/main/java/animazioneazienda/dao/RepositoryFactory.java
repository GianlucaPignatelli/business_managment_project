package animazioneazienda.dao;

import animazioneazienda.dao.animatore.*;
import animazioneazienda.exception.DaoException;
import java.io.File;
import java.sql.Connection;
import java.util.Arrays;

public class RepositoryFactory {
    private static RepositoryFactory instance;
    private RepositoryFactory() {}

    public static RepositoryFactory getInstance() {
        if (instance == null) instance = new RepositoryFactory();
        return instance;
    }

    // Crea la repo "composita" che scrive su entrambi i sistemi
    public StatusAnimatoreRepository getDoublePersistenceStatusAnimatoreRepository(Connection conn, File jsonFile) throws DaoException {
        StatusAnimatoreRepository mysqlRepo = new StatusAnimatoreMySQLRepository(conn);
        StatusAnimatoreRepository jsonRepo  = new StatusAnimatoreJsonRepository(jsonFile);
        return new StatusAnimatoreCompositeRepository(Arrays.asList(mysqlRepo, jsonRepo));
    }
}