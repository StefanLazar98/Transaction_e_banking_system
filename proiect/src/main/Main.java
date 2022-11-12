package main;

import clase.*;

import java.io.IOException;
import java.util.*;

public class Main {

    static List<Map.Entry<Integer, ContBancar>> entryList;

    static String[][] istoricTranzactii;

    public static void main(String[] args) throws IOException {
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        int optiune;


        ListaConturiBancare listaConturiBancare = ListaConturiBancare.getInstance();
        listaConturiBancare.populeazaLista();
        entryList = new ArrayList<>(ListaConturiBancare.conturiBancareMap.entrySet());

        ListaClienti listaClienti = ListaClienti.getInstance();
        listaClienti.populeazaLista();

        incarcaIstoricTranzactii();

        System.out.println("----------------------------------Bun venit in aplicatia de e-banking!----------------------------------");
        while (!exit) {
            afiseazaOptiuni();
            while (true) {
                try {
                    optiune = scanner.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("----------------Valoare incorecta! Introdu o cifra intre 0-8 inclusiv!----------------");
                    scanner.nextLine();
                }
            }
            if (optiune == 0) {
                listaClienti.salveazaLista();
                listaConturiBancare.salveazaLista();
                exit = true;
            } else if (optiune == 1) {
                if (listaConturiBancare.getConturiBancare().isEmpty()) {
                    System.out.println("Nu exista conturi bancare");
                } else {
                    for (ContBancar contBancar : listaConturiBancare.getConturiBancare()) {
                        System.out.println(contBancar.toString());
                    }
                }
            } else if (optiune == 2) {
                System.out.println("Apasa x pentru a anula");
                ContBancar contBancar = creeazaContBancar();
                if (contBancar != null) {
                    listaConturiBancare.adaugaContBancarNou(contBancar);
                    ListaConturiBancare.conturiBancareMap.put(entryList.get(entryList.size() - 1).getKey() + 1, contBancar);
                    entryList = new ArrayList<>(ListaConturiBancare.conturiBancareMap.entrySet());
                }
            } else if (optiune == 3) {
                raportConturiBancareClient();
            } else if (optiune == 4) {
                actualizeazaContBancar();
            } else if (optiune == 5) {
                stergeContBancar();
            } else if (optiune == 6) {
                raportCategorieContBancar();
            } else if (optiune == 7) {
                afiseazaRaportIstoricTranzactiiClientExistent();
            } else if (optiune == 8) {
                afiseazaIstoricTranzactii();
            } else {
                System.out.println("----------------Comanda inexistenta!----------------");
            }
        }

    }

    public static void afiseazaOptiuni() {
        System.out.println();
        System.out.println("Alege o optiune:");
        System.out.println("0 - iesi din aplicatie");
        System.out.println("1 - afiseaza toate conturile bancare                 2 - adauga un nou cont bancar");
        System.out.println("3 - raport cont bancar tranzactii                    4 - actualizeaza cont bancar");
        System.out.println("5 - sterge cont bancar                               6 - filtreaza conturile bancare dupa categorie");
        System.out.println("7 - cauta client si afiseaza daca are tranzactii     8 - afiseaza istoricul tranzactiilor tuturor clientilor");
        System.out.println();
    }

    public static Client creeazaClient() {
        System.out.print("ID client = ");
        String ID = new Scanner(System.in).nextLine();
        if (ID.equals("x")) {
            return null;
        }

        System.out.println("Numele trebuie sa contina doar litere si sa aiba lungimea minima de 3 caractere (Acesta poate contine si spatiu intre nume si prenume)");
        System.out.print("Nume client = ");
        String nume = new Scanner(System.in).nextLine();
        boolean ok = true;
        while (ok) {
            if (!nume.matches("^[a-zA-Z ]*$") && nume.length() >= 3) {
                System.out.println("Numele trebuie sa contina doar litere si sa aiba lungimea minima de 3 caractere (Acesta poate contine si spatiu intre nume si prenume)");
                System.out.print("Nume client = ");
                nume = new Scanner(System.in).nextLine();
            } else if (nume.equals("x")) {
                return null;
            } else {
                ok = false;
            }
        }

        Client client = new Client(ID, nume);

        System.out.println("Clientul a fost creat cu succes");
        return client;
    }

    public static ContBancar creeazaContBancar() {
        System.out.println("IBAN-ul trebuie sa contina fix 24 de caractere, EX: RO15RNCB1234567890123456");
        System.out.print("IBAN = ");
        String IBAN = new Scanner(System.in).nextLine();
        boolean ok = true;
        while (ok) {
            if (IBAN.length() != 24 && !(IBAN.equals("x"))) {
                System.out.println("IBAN-ul trebuie sa contina fix 24 de caractere");
                System.out.print("IBAN = ");
                IBAN = new Scanner(System.in).nextLine();
            } else if (IBAN.equals("x")) {
                return null;
            } else {
                ok = false;
            }
        }

        System.out.println("BIC-ul trebuie sa contina fix 8 caractere, EX: RNCBROBU ");
        System.out.print("BIC = ");
        String BIC = new Scanner(System.in).nextLine();
        ok = true;
        while (ok) {
            if (BIC.length() != 8 && !(BIC.equals("x"))) {
                System.out.println("BIC-ul trebuie sa contina fix 8 caractere");
                System.out.print("BIC = ");
                BIC = new Scanner(System.in).nextLine();
            } else if (BIC.equals("x")) {
                return null;
            } else {
                ok = false;
            }
        }

        for (TipContBancar tip : TipContBancar.tipContBancarMap.values()) {
            System.out.println(tip.name());
        }
        System.out.print("Tip cont bancar = ");
        String tip = new Scanner(System.in).nextLine();
        if (tip.equals("x")) {
            return null;
        }

        TipContBancar tipContBancar = null;
        ok = true;
        while (ok) {
            if (TipContBancar.tipContBancarMap.containsKey(tip)) {
                tipContBancar = TipContBancar.tipContBancarMap.get(tip);
                ok = false;
            } else {
                System.out.println("----------------Valoare incorecta!----------------");
                System.out.println("Introdu o noua valoare");
                System.out.print("Tip cont bancar = ");
                tip = new Scanner(System.in).nextLine();
            }
        }

        System.out.println("Soldul curent trebuie sa fie un numar pozitiv > 0 (double)");
        System.out.print("Sold curent = ");
        double soldCurent;
        ok = true;
        ContBancar contBancar = new ContBancar(IBAN, BIC, tipContBancar);
        while (ok) {
            try {
                soldCurent = new Scanner(System.in).nextDouble();
                if (soldCurent < 0) {
                    System.out.println("Soldul curent trebuie sa fie un numar pozitiv > 0 (double)");
                    System.out.print("Sold curent = ");
                } else {
                    contBancar.setSoldCurent(soldCurent);
                    ok = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("----------------Valoare incorecta! Introdu un numar pozitiv > 0 (double) !----------------");
                System.out.print("Sold curent = ");
            } catch (InvalidValueException e) {
                System.out.println("Introdu un numar pozitiv > 0 (double)");
                System.out.print("Sold curent = ");
            }
        }

        contBancar.setClient(creeazaClient());
        contBancar.adaugaBani(1000d);
        contBancar.adaugaBani(1500d);
        contBancar.retrageBani(2200d);

        System.out.println("Contul bancar a fost creat cu succes");
        return contBancar;
    }

    public static void raportConturiBancareClient() throws IOException {
        LinkedList<ContBancar> conturiBancareClient = new LinkedList<>();
        System.out.print("Nume Client = ");
        String Client = new Scanner(System.in).nextLine();
        if (ListaConturiBancare.getInstance().getConturiBancare().isEmpty()) {
            System.out.println("Nu exista conturi bancare pentru clientul cu acest nume");
        } else {
            for (ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
                if (contBancar.getClient().getNumeClient().equals(Client)) {
                    conturiBancareClient.add(contBancar);
                }
            }
            if (conturiBancareClient.isEmpty()) {
                System.out.println("Nu exista conturi bancare pentru clientul cu acest nume");
            } else {
                for (ContBancar contBancar : conturiBancareClient) {
                    System.out.println(contBancar.toString());
                }
                System.out.println("Doresti sa salvezi acest raport?");
                System.out.println("1 - Da                    0 - Nu");
                int alegere;
                while (true) {
                    while (true) {
                        try {
                            alegere = new Scanner(System.in).nextInt();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("----------------Valoare incorecta! Apasa 1 sau 0!----------------");
                        }
                    }
                    if (alegere == 1) {
                        Fisier fisier = new Fisier("ConturiBancare " + conturiBancareClient.get(0).getClient().getNumeClient() + ".txt");
                        fisier.genereazaFisierTextPrinColectie(conturiBancareClient);
                        try {
                            System.out.println("Se genereaza raportul");
                            System.out.println("Waiting...");
                            Thread.sleep(2 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Fisierul a fost creat si se afla la " + fisier.caleFisier());
                        break;
                    } else if (alegere == 0) {
                        break;
                    } else {
                        System.out.println("----------------Comanda inexistenta! Apasa 1 sau 0!----------------");
                    }
                }
            }
        }
    }

    public static void actualizeazaContBancar() {
        System.out.println();
        System.out.println("Apasa 0 pentru a anula");
        if (ListaConturiBancare.getInstance().getConturiBancare().isEmpty()) {
            System.out.println("Nu exista conturi bancare");
        } else {
            ListaConturiBancare.conturiBancareMap.forEach((key, value) -> System.out.println(key + " - " + value));
            System.out.println();
            System.out.print("Numarul contului bancar din lista de conturi bancare = ");
            int numar;
            while (true) {
                try {
                    numar = new Scanner(System.in).nextInt();
                    if (numar < 0 || numar > entryList.get(entryList.size() - 1).getKey()) {
                        throw new InvalidValueException("Numar incorect");
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("----------------Valoare incorecta! Introdu un numar!----------------");
                } catch (InvalidValueException e) {
                    System.out.println("Introdu un numar care se afla in lista");
                }
            }
            if (numar != 0) {
                System.out.println();
                System.out.println("Apasa 0 pentru a anula");
                System.out.println("1 - IBAN = " + ListaConturiBancare.conturiBancareMap.get(numar).getIBAN());
                System.out.println("2 - BIC = " + ListaConturiBancare.conturiBancareMap.get(numar).getBIC());
                System.out.println("3 - Tip cont bancar = " + ListaConturiBancare.conturiBancareMap.get(numar).getTipContBancar());
                System.out.println("Ce doresti sa editezi?");
                int atribut;
                while (true) {
                    try {
                        atribut = new Scanner(System.in).nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("----------------Valoare incorecta! Introdu un numar!----------------");
                    } catch (InvalidValueException e) {
                        System.out.println("Introdu un numar care se afla in lista");
                    }
                }
                if (atribut != 0) {
                    if (atribut == 1) {
                        System.out.println();
                        System.out.println("Apasa x pentru a anula");
                        System.out.print("IBAN = ");
                        String IBAN = new Scanner(System.in).nextLine();
                        if (!IBAN.equals("x")) {
                            for (ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
                                if (contBancar.getIBAN().equals(ListaConturiBancare.conturiBancareMap.get(numar).getIBAN())) {
                                    contBancar.setIBAN(IBAN);
                                }
                            }
                            ListaConturiBancare.conturiBancareMap.get(numar).setIBAN(IBAN);
                            System.out.println("IBAN cont bancar actualizat cu succes");
                        }
                    } else if (atribut == 2) {
                        System.out.println();
                        System.out.println("Apasa x pentru a anula");
                        System.out.print("BIC = ");
                        String BIC = new Scanner(System.in).nextLine();
                        if (!BIC.equals("x")) {
                            for (ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
                                if (contBancar.getBIC().equals(ListaConturiBancare.conturiBancareMap.get(numar).getBIC())) {
                                    contBancar.setBIC(BIC);
                                }
                            }
                            ListaConturiBancare.conturiBancareMap.get(numar).setBIC(BIC);
                            System.out.println("BIC cont bancar actualizat cu succes");
                        }
                    } else if (atribut == 3) {
                        for (TipContBancar tip : TipContBancar.tipContBancarMap.values()) {
                            System.out.println(tip.name());
                        }
                        System.out.println();
                        System.out.println("Apasa x pentru a anula");
                        System.out.print("Tip cont bancar = ");
                        String tip = new Scanner(System.in).nextLine();
                        if (!tip.equals("x")) {
                            TipContBancar tipContBancar;
                            while (true) {
                                if (TipContBancar.tipContBancarMap.containsKey(tip)) {
                                    tipContBancar = TipContBancar.tipContBancarMap.get(tip);
                                    break;
                                } else {
                                    System.out.println("----------------Valoare incorecta!----------------");
                                    System.out.println("Introdu o noua valoare");
                                    tip = new Scanner(System.in).nextLine();
                                }
                            }
                            for (ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
                                if (contBancar.getIBAN().equals(ListaConturiBancare.conturiBancareMap.get(numar).getIBAN())) {
                                    contBancar.setTipContBancar(tipContBancar);
                                }
                            }
                            ListaConturiBancare.conturiBancareMap.get(numar).setTipContBancar(tipContBancar);
                            System.out.println("Tip cont bancar actualizat cu succes");
                        }
                    } else {
                        System.out.println("Comanda incorecta");
                    }
                }
            }
        }
    }

    public static void stergeContBancar() {
        System.out.println();
        System.out.println("Apasa 0 pentru a anula");
        if (ListaConturiBancare.getInstance().getConturiBancare().isEmpty()) {
            System.out.println("Nu exista conturi bancare");
        } else {
            ListaConturiBancare.conturiBancareMap.forEach((key, value) -> System.out.println(key + " - " + value));
            System.out.println();
            System.out.print("Numarul contului bancar = ");
            int numar;
            while (true) {
                try {
                    numar = new Scanner(System.in).nextInt();
                    if (numar < 0 || numar > entryList.get(entryList.size() - 1).getKey()) {
                        throw new InvalidValueException("Numar incorect");
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("----------------Valoare incorecta! Introdu un numar!----------------");
                } catch (InvalidValueException e) {
                    System.out.println("Introdu un numar care se afla in lista");
                }
            }
            if (numar != 0) {
                ListaConturiBancare.getInstance().getConturiBancare().remove(ListaConturiBancare.conturiBancareMap.get(numar));
                ListaConturiBancare.conturiBancareMap.remove(numar);
                System.out.println("Contul bancar a fost sters cu succes");
            }
        }
    }

    public static void raportCategorieContBancar() throws IOException {
        LinkedList<ContBancar> categorieConturiBancare = new LinkedList<>();
        for (TipContBancar tip : TipContBancar.tipContBancarMap.values()) {
            System.out.println(tip.name());
        }
        System.out.print("Tip cont bancar = ");
        String tip = new Scanner(System.in).nextLine();
        TipContBancar tipContBancar;
        while (true) {
            if (TipContBancar.tipContBancarMap.containsKey(tip)) {
                tipContBancar = TipContBancar.tipContBancarMap.get(tip);
                break;
            } else {
                System.out.println("----------------Valoare incorecta!----------------");
                System.out.println();
                System.out.print("Tip cont bancar = ");
                tip = new Scanner(System.in).nextLine();
            }
        }
        if (ListaConturiBancare.getInstance().getConturiBancare().isEmpty()) {
            System.out.println("Nu exista conturi bancare");
        } else {
            for (ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
                if (contBancar.getTipContBancar().equals(tipContBancar)) {
                    categorieConturiBancare.add(contBancar);
                }
            }
            if (categorieConturiBancare.isEmpty()) {
                System.out.println("Nu exista conturi bancare din aceasta categorie");
            } else {
                for (ContBancar contBancar : categorieConturiBancare) {
                    System.out.println(contBancar.toString());
                }
                System.out.println("Doresti sa salvezi acest raport?");
                System.out.println("1 - Da                    0 - Nu");
                int alegere;
                while (true) {
                    while (true) {
                        try {
                            alegere = new Scanner(System.in).nextInt();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("----------------Valoare incorecta! Apasa 1 sau 0!----------------");
                        }
                    }
                    if (alegere == 1) {
                        Fisier fisier = new Fisier("ContBancar " + categorieConturiBancare.get(0).getTipContBancar() + ".txt");
                        fisier.genereazaFisierTextPrinColectie(categorieConturiBancare);
                        try {
                            System.out.println("Se genereaza raportul");
                            System.out.println("Waiting...");
                            Thread.sleep(2 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Fisierul a fost creat si se afla la " + fisier.caleFisier());
                        break;
                    } else if (alegere == 0) {
                        break;
                    } else {
                        System.out.println("----------------Comanda inexistenta! Apasa 1 sau 0!----------------");
                    }
                }
            }
        }
    }

    public static void afiseazaRaportIstoricTranzactiiClientExistent() throws IOException {
        System.out.println("Doresti ca cauti un client?");
        System.out.println("1 - DA              0 - NU, doresc sa ma intorc la meniul principal");
        int alegere;
        while (true) {
            while (true) {
                try {
                    alegere = new Scanner(System.in).nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("----------------Valoare incorecta! Apasa 1 sau 0!----------------");
                }
            }
            if (alegere == 1) {
                cautareClient();
                break;
            } else if (alegere == 0) {
                afiseazaOptiuni();
            } else {
                System.out.println();
                System.out.println("Comanda inexistenta, apasa 1 sau 0!");
                System.out.println();
                System.out.println("Doresti ca cauti un client?");
                System.out.println("1 - DA              0 - NU, doresc sa ma intorc la meniul principal");
            }
        }
    }

    public static void cautareClient() throws IOException {
        System.out.println();
        System.out.println("Apasa x pentru a anula");
        System.out.print("Introdu id-ul clientului dorit = ");
        String id = new Scanner(System.in).nextLine();
        if (!id.equals("x")) {
            boolean gasit = false;
            for (ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
                if (contBancar.getClient().getIdClient().equals(id)) {
                    System.out.println();
                    System.out.println("Hello, " + contBancar.getClient().getNumeClient() + "!");
                    System.out.println();
                    gasit = true;
                    break;
                }
            }
            if (!gasit) {
                System.out.println("Clientul cu id-ul introdus nu exista");
            } else {
                System.out.println("0 - Raport istoric tranzactii");
                System.out.println();
                int actiune;
                while (true) {
                    try {
                        actiune = new Scanner(System.in).nextInt();
                        if (actiune != 0) {
                            System.out.println();
                            System.out.println("Apasa 0 pentru a putea genera raportul istoric al tranzactiilor");
                            System.out.println();
                            break;
                        } else {
                            raportIstoricTranzactii(id);
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("----------------Valoare incorecta! Apasa 0!----------------");
                    }
                }
            }
        }
    }

    private static void raportIstoricTranzactii(String id) throws IOException {
        String[] istoricTranzactii;
        for (ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
            if (contBancar.getClient().getIdClient().equals(id)) {
                if (contBancar.getIstoricTranzactii().isEmpty()) {
                    System.out.println("Nu exista tranzactii");

                } else {
                    int i = 0;
                    int dimensiune = contBancar.getIstoricTranzactii().size();
                    istoricTranzactii = new String[dimensiune];
                    for (Map.Entry<Integer, String> entry : contBancar.getIstoricTranzactii().entrySet()) {
                        istoricTranzactii[i] = entry.getValue();
                        i++;
                    }

                    System.out.println("Clientul " + contBancar.getClient().getNumeClient() + " cu id-ul " + contBancar.getClient().getIdClient());
                    for (int index = 0; index < dimensiune; index++) {
                        System.out.println("Tranzactia nr " + index + " : " + istoricTranzactii[index]);
                    }

                    System.out.println();
                    System.out.println("Doresti sa salvezi acest raport?");
                    System.out.println("1 - Da                    0 - Nu");
                    int alegere;
                    while (true) {
                        while (true) {
                            try {
                                alegere = new Scanner(System.in).nextInt();
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("----------------Valoare incorecta! Apasa 1 sau 0!----------------");
                            }
                        }
                        if (alegere == 1) {
                            Fisier fisier = new Fisier("IstoricTranzactii " + contBancar.getClient().getNumeClient() + ".txt");
                            fisier.genereazaFisierTextPrinVector(istoricTranzactii);
                            try {
                                System.out.println("Se genereaza raportul");
                                System.out.println("Waiting...");
                                Thread.sleep(2 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Fisierul a fost creat si se afla la " + fisier.caleFisier());
                            System.out.println();
                            afiseazaOptiuni();
                            break;
                        } else if (alegere == 0) {
                            afiseazaOptiuni();
                            break;
                        } else {
                            System.out.println("----------------Comanda inexistenta! Apasa 1 sau 0!----------------");
                        }
                    }
                }
                break;
            }
        }
    }

    public static void incarcaIstoricTranzactii() {
        if (!ListaConturiBancare.getInstance().getConturiBancare().isEmpty()) {
            int i = 0;
            int randuri = ListaConturiBancare.getInstance().getConturiBancare().size();
            istoricTranzactii = new String[randuri][];
            for (ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
                int coloane = contBancar.getIstoricTranzactii().size();
                if (coloane != 0) {
                    istoricTranzactii[i] = new String[coloane];
                    int j = 0;
                    for (Map.Entry<Integer, String> entry : contBancar.getIstoricTranzactii().entrySet()) {
                        istoricTranzactii[i][j] = entry.getValue();
                        j++;
                    }
                    i++;
                }
            }
        }
    }

    public static void afiseazaIstoricTranzactii() throws IOException {
        if (istoricTranzactii.length == 0) {
            System.out.println("Nu a fost realizata nicio tranzactie");
        } else {
            int i = 0;
            for (ContBancar contBancar : ListaConturiBancare.getInstance().getConturiBancare()) {
                int coloane = contBancar.getIstoricTranzactii().size();
                if (coloane != 0) {
                    System.out.print("Clientul : " + contBancar.getClient().getNumeClient());
                    System.out.println();
                    for (int j = 0; j < istoricTranzactii[i].length; j++) {
                        if (j == 0) {
                            System.out.print(istoricTranzactii[i][j].toCharArray());
                        }
                        if (j != 0 && j != istoricTranzactii[i].length - 1) {
                            System.out.println();
                            System.out.print(istoricTranzactii[i][j].toCharArray());
                        }
                        if (j != 0 && j == istoricTranzactii[i].length - 1) {
                            System.out.println();
                            System.out.println(istoricTranzactii[i][j].toCharArray());
                        }
                    }
                    i++;
                }
            }
            System.out.println();
            System.out.println("Doresti sa salvezi acest raport?");
            System.out.println("1 - Da                    0 - Nu");
            System.out.println();
            int alegere;
            while (true) {
                while (true) {
                    try {
                        alegere = new Scanner(System.in).nextInt();
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("----------------Valoare incorecta! Apasa 1 sau 0!----------------");
                    }
                }
                if (alegere == 1) {
                    Fisier fisier = new Fisier("IstoricTranzactii" + ".txt");
                    fisier.genereazaFisierTextMatrice(istoricTranzactii);
                    try {
                        System.out.println("Se genereaza raportul");
                        System.out.println("Waiting...");
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Fisierul a fost creat si se afla la " + fisier.caleFisier());
                    break;
                } else if (alegere == 0) {
                    break;
                } else {
                    System.out.println("----------------Comanda inexistenta! Apasa 1 sau 0!----------------");
                }
            }
        }
    }
}
