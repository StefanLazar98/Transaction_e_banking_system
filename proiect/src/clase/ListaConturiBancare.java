package clase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListaConturiBancare implements OperatiiFisier, Serializable {

    private List<ContBancar> conturiBancare;

    private static ListaConturiBancare listaConturiBancare = null;
    private static final long serialVersionUID = 1L;

    public static Map<Integer,ContBancar> conturiBancareMap;

    private ListaConturiBancare() {
        this.conturiBancare = new ArrayList<>();
    }

    public List<ContBancar> getConturiBancare() {
        return conturiBancare;
    }

    public void adaugaContBancarNou(ContBancar contBancar) {
        this.conturiBancare.add(contBancar);
    }

    public static Map<Integer, ContBancar> getConturiBancareMap() {
        return conturiBancareMap;
    }

    public static void setConturiBancareMap(Map<Integer, ContBancar> conturiBancareMap) {
        ListaConturiBancare.conturiBancareMap = conturiBancareMap;
    }

    public static synchronized ListaConturiBancare getInstance() {
        if(listaConturiBancare == null) {
            listaConturiBancare = new ListaConturiBancare();
        }
        return listaConturiBancare;
    }

    @Override
    public void salveazaLista() {
        Fisier fisier = new Fisier("ListaConturiBancare.dat");
        try {
            fisier.deschideFisier();
            fisier.serializareLista(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void populeazaLista() {
        Fisier fisier = new Fisier("ListaConturiBancare.dat");
        try {
            fisier.deschideFisier();
            if(!fisier.getFisier().exists()) {
                return;
            }
            fisier.deserializareListaConturiBancare();
            conturiBancareMap = new LinkedHashMap<>();
            int i = 1;
            for(ContBancar contBancar : listaConturiBancare.getConturiBancare()) {
                conturiBancareMap.put(i, contBancar);
                i++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ListaConturiBancare{" +
                "conturiBancare=" + conturiBancare +
                '}';
    }
}
