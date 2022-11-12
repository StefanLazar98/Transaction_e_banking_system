package clase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaClienti implements OperatiiFisier, Serializable {

    private List<Client> clienti;

    private static ListaClienti listaClienti = null;
    private static final long serialVersionUID = 1L;

    private ListaClienti() {
        this.clienti = new ArrayList<>();
    }

    public List<Client> getClienti() {
        return clienti;
    }

    public void setClienti(List<Client> clienti) {
        this.clienti = clienti;
    }

    public static ListaClienti getListaClienti() {
        return listaClienti;
    }

    public static void setListaClienti(ListaClienti listaClienti) {
        ListaClienti.listaClienti = listaClienti;
    }

    public void adaugaClientNou(Client client) {
        this.clienti.add(client);
    }

    public static synchronized ListaClienti getInstance() {
        if(listaClienti == null) {
            listaClienti = new ListaClienti();
        }
        return listaClienti;
    }

    @Override
    public String toString() {
        return "ListaClienti{" +
                "clienti=" + clienti +
                '}';
    }

    @Override
    public void salveazaLista() {
        Fisier fisier = new Fisier("listaClienti.dat");
        try {
            fisier.deschideFisier();
            fisier.serializareLista(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void populeazaLista() {
        Fisier fisier = new Fisier("listaClienti.dat");
        try {
            fisier.deschideFisier();
            if(!fisier.getFisier().exists()) {
                return;
            }
            fisier.deserializareListaClienti();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
