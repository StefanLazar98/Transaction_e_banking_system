package clase;

import java.io.Serializable;

public class Client implements Serializable {
    private String idClient;
    private String numeClient;

    private static final long serialVersionUID = 1L;

    public Client(String idClient, String numeClient) {
        this.idClient = idClient;

        if(numeClient.matches( "^[a-zA-Z ]*$" )){
            this.numeClient = numeClient;
        } else {
            throw new InvalidValueException("Numele clientului trebuie sa contina doar litere!");
        }
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        if(numeClient.matches( "^[a-zA-Z ]*$" )){
            this.numeClient = numeClient;
        } else {
            throw new InvalidValueException("Numele clientului trebuie sa contina doar litere!");
        }
    }

    @Override
    public String toString() {

        return "Clientul '" + numeClient + '\'' +
                ", id='" + idClient + '\'';
    }
}
