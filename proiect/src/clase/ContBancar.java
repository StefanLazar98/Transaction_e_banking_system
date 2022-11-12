package clase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ContBancar implements OperatiiContBancar, Serializable {
    private String IBAN;
    private String BIC;
    private TipContBancar tipContBancar;
    private double soldCurent;
    private static int numarTranzactii = 0;
    private Map<Integer, String> istoricTranzactii = new HashMap<>();
    private Client client;

    private static final long serialVersionUID = 1L;

    public ContBancar(String IBAN, String BIC, TipContBancar tipContBancar) {
        if(IBAN.length() == 24) {
            this.IBAN = IBAN;
        } else {
            throw new InvalidValueException("Lungimea IBAN-ului trebuie sa fie de 24 caractere!");
        }

        if(BIC.length() == 8) {
            this.BIC = BIC;
        } else {
            throw new InvalidValueException("Lungimea BIC-ului trebuie sa fie de 8 caractere!");
        }

        this.tipContBancar = tipContBancar;
    }

    public ContBancar(String IBAN, String BIC, TipContBancar tipContBancar, double soldCurent, Client client) {
        if(IBAN.length() == 24) {
            this.IBAN = IBAN;
        } else {
            throw new InvalidValueException("Lungimea IBAN-ului trebuie sa fie de 24 caractere!");
        }

        if(BIC.length() == 8) {
            this.BIC = BIC;
        } else {
            throw new InvalidValueException("Lungimea BIC-ului trebuie sa fie de 8 caractere!");
        }

        this.tipContBancar = tipContBancar;

        if(soldCurent > 0) {
            this.soldCurent = soldCurent;
        } else {
            throw new InvalidValueException("Soldul contului nu poate fi negativ!");
        }

        if(client != null) {
            this.client = client;
        } else {
            throw new InvalidValueException("Contul bancar trebuie sa aiba un client");
        }
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        if(IBAN.length() == 24) {
            this.IBAN = IBAN;
        } else {
            throw new InvalidValueException("Lungimea IBAN-ului trebuie sa fie de 24 caractere!");
        }
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        if(BIC.length() == 8) {
            this.BIC = BIC;
        } else {
            throw new InvalidValueException("Lungimea BIC-ului trebuie sa fie de 8 caractere!");
        }
    }

    public TipContBancar getTipContBancar() {
        return tipContBancar;
    }

    public void setTipContBancar(TipContBancar tipContBancar) {
        this.tipContBancar = tipContBancar;
    }

    public double getSoldCurent() {
        return soldCurent;
    }

    public void setSoldCurent(double soldCurent) {
        if(soldCurent > 0) {
            this.soldCurent = soldCurent;
        } else {
            throw new InvalidValueException("Soldul contului nu poate fi negativ!");
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if(client != null) {
            this.client = client;
        } else {
            throw new InvalidValueException("Clientul trebuie sa existe!");
        }
    }

    public Map<Integer, String> getIstoricTranzactii() {
        return istoricTranzactii;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContBancar{");
        sb.append(", IBAN='").append(IBAN).append('\'');
        sb.append(", BIC='").append(BIC).append('\'');
        sb.append(", tipContBancar=").append(tipContBancar).append('\'');
        sb.append(", soldCurent=").append(soldCurent);
        sb.append(", clientul ").append(client.getNumeClient()).append(" cu id-ul: ").append(client.getIdClient());
        if(istoricTranzactii != null) {
            Iterator<String> iterator = istoricTranzactii.values().iterator();
            int tranzactii = 1;
            while (iterator.hasNext()) {
                String mesaj = iterator.next();
                System.out.println("tranzactia nr " + tranzactii + ": " + mesaj);
                tranzactii++;
            }
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void adaugaBani(double sumaBani)
    {
        if(sumaBani > 0) {
            soldCurent += sumaBani;
            String mesaj = "Soldul curent este: " + soldCurent + " , iar suma adaugata a fost " + sumaBani;
            istoricTranzactii.put(numarTranzactii, mesaj);
            numarTranzactii++;
        } else {
            throw new InvalidValueException("Nu poti adauga o suma de bani negativa!");
        }
    }

    @Override
    public void retrageBani(double sumaBani)
    {
        if(soldCurent < sumaBani) {
            throw new InvalidValueException("Nu poti retrage o suma de bani mai mare decat soldul curent!");
        }

        if(sumaBani > 0) {
            soldCurent -= sumaBani;
            String mesaj = "Soldul curent este: " + soldCurent + " , iar suma retrasa a fost: " + sumaBani;
            istoricTranzactii.put(numarTranzactii, mesaj);
            numarTranzactii++;

        } else if(sumaBani < 0) {
            throw new InvalidValueException("Nu poti retrage o suma de bani negativa!");
        }
    }
}
