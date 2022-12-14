package app.workers;

import app.beans.Departement;
import app.beans.Localite;
import app.beans.Personne;
import app.exceptions.MyDBException;
import app.workers.dao.FileDao;
import app.workers.dao.JpaDao;
import app.workers.extracters.DepartementExtracter;
import app.workers.extracters.LocaliteExtracter;
import java.io.File;
import java.util.List;
import app.workers.dao.FileDaoItf;
import app.workers.dao.JpaDaoItf;

/**
 * Couche "métier" gérant les accès de et vers la base de données.
 *
 * @author ramalhom
 */
public class DbWorker implements DbWorkerItf {

    private static final String JPA_PU = "PU_MYSQL";
    private final JpaDaoItf<Personne, Integer> persWrk;
    private final JpaDaoItf<Localite, Integer> locWrk;
    private final JpaDaoItf<Departement, Integer> depWrk;
    private final FileDaoItf<Localite> ficLocWrk;
    private final FileDaoItf<Departement> ficDepWrk;

    /**
     * Constructeur.
     *
     * @throws app.exceptions.MyDBException
     */
    public DbWorker() throws MyDBException {
        persWrk = new JpaDao<>(JPA_PU, Personne.class);
        locWrk = new JpaDao<>(JPA_PU, Localite.class);
        depWrk = new JpaDao<>(JPA_PU, Departement.class);
        ficLocWrk = new FileDao<>(new LocaliteExtracter("\t"));
        ficDepWrk = new FileDao<>(new DepartementExtracter(";"));
    }

    /*
   * AUTRES
     */
    @Override
    public void fermerBD() {

    }

    @Override
    public boolean estConnecte() {
        return persWrk.estConnectee();
    }

    @Override
    public List<Personne> lirePersonnes() throws MyDBException {
        return persWrk.lireListe();
    }

    @Override
    public long compterPersonnes() throws MyDBException {
        return persWrk.compter();

    }

    @Override
    public void ajouterPersonne(Personne p) throws MyDBException {
        persWrk.creer(p);

    }

    @Override
    public Personne lirePersonne(Personne p) throws MyDBException {
        return persWrk.lire(p.getPkPers());
    }

    @Override
    public void modifierPersonne(Personne p) throws MyDBException {
        persWrk.modifier(p);
    }

    @Override
    public void effacerPersonne(Personne p) throws MyDBException {
        persWrk.effacer(p.getPkPers());

    }

    @Override
    public Personne rechercherPersonneAvecNom(String nomARechercher) throws MyDBException {
        return persWrk.rechercher("nom", nomARechercher);
    }

    @Override
    public List<Localite> lireLocalites() throws MyDBException {
        return locWrk.lireListe();
    }

    @Override
    public long compterLocalites() throws MyDBException {
        return locWrk.compter();
    }

    @Override
    public int lireEtSauverLocalites(File fichier, String nomCharset) throws Exception {
        List<Localite> list = ficLocWrk.lireFichierTexte(fichier, nomCharset);
        return locWrk.sauverListe(list);
    }

    @Override
    public List<Departement> lireDepartements() throws MyDBException {
        return depWrk.lireListe();
    }

    @Override
    public long compterDepartements() throws MyDBException {
        return depWrk.compter();
    }

    @Override
    public int lireEtSauverDepartements(File fichier, String nomCharset) throws Exception {
        List<Departement> list = ficDepWrk.lireFichierTexte(fichier, nomCharset);
        return depWrk.sauverListe(list);
    }

}
