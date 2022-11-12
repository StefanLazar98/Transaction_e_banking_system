package clase;

import java.io.*;
import java.util.LinkedList;

public class Fisier {

    private File fisier;
    private final String numeFisier;

    public Fisier(String numeFisier) {
        this.numeFisier = numeFisier;
    }

    public File getFisier() {
        return fisier;
    }

    public void deschideFisier()
    {
        fisier = new File(numeFisier);
    }

    public <T> void serializareLista(T lista) {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.numeFisier));
            objectOutputStream.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null)
                try
                {
                    objectOutputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
    }

    public void deserializareListaConturiBancare() {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(this.numeFisier));
            ListaConturiBancare conturiBancare = (ListaConturiBancare) objectInputStream.readObject();
            for(ContBancar contBancar : conturiBancare.getConturiBancare()) {
                ListaConturiBancare.getInstance().adaugaContBancarNou(contBancar);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null)
                try
                {
                    objectInputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
    }

    public void deserializareListaClienti() {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(this.numeFisier));
            ListaClienti clienti = (ListaClienti) objectInputStream.readObject();
            for(Client client : clienti.getClienti()) {
                ListaClienti.getInstance().adaugaClientNou(client);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null)
                try
                {
                    objectInputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
    }

    public void genereazaFisierTextPrinColectie(LinkedList<ContBancar> conturiBanci) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        try {
            this.deschideFisier();
            if(!this.getFisier().exists()) {
                fileWriter = new FileWriter(this.numeFisier);
                printWriter = new PrintWriter(fileWriter);
                for(ContBancar contBancar : conturiBanci) {
                    printWriter.println(contBancar.toString());
                    if(contBancar.getIstoricTranzactii().size() > 0) {
                        printWriter.println(contBancar.getIstoricTranzactii());
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(fileWriter != null && printWriter != null) {
                try {
                    fileWriter.close();
                    printWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void genereazaFisierTextPrinVector(String[] istoricTranzactii) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        try {
            this.deschideFisier();
            if(!this.getFisier().exists()) {
                fileWriter = new FileWriter(this.numeFisier);
                printWriter = new PrintWriter(fileWriter);
                for (String s : istoricTranzactii) {
                    printWriter.println(s);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(fileWriter != null && printWriter != null) {
                try {
                    fileWriter.close();
                    printWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void genereazaFisierTextMatrice(String[][] istoricTranzactii) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        try {
            this.deschideFisier();
            if(!this.getFisier().exists()) {
                fileWriter = new FileWriter(this.numeFisier);
                printWriter = new PrintWriter(fileWriter);
                int i = 0;
                for(ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
                    int coloane = contBancar.getIstoricTranzactii().size();
                    if(coloane != 0) {
                        printWriter.print(contBancar.getClient().getNumeClient() + ":");
                        for(int j = 0; j < istoricTranzactii[i].length; j++) {
                            if(j == 0) {
                                printWriter.print(" " + istoricTranzactii[i][j] + ",");
                            }
                            if(j != 0 && j != istoricTranzactii[i].length - 1) {
                                printWriter.print(" " + istoricTranzactii[i][j] + ",");
                            }
                            if(j != 0 && j == istoricTranzactii[i].length - 1) {
                                printWriter.println(" " + istoricTranzactii[i][j]);
                            }
                        }
                        i++;
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(fileWriter != null && printWriter != null) {
                try {
                    fileWriter.close();
                    printWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String caleFisier() throws IOException
    {
        fisier = new File(numeFisier);
        if (fisier.exists())
            return fisier.getCanonicalPath();
        else
            return null;
    }
}
