package editor_grafuri;


public class RunApplication {

    public static void main(String[] args) {

        View view = new View("Editor de grafuri."); //instatiam un obiect de tip view care il lansam si se afiseaza plansa de desen
        view.add(new Desen());
    }
}
