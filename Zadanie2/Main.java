package Zadanie2;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ObjectContainer<Person> peopleFromWarsaw = new ObjectContainer<>(p -> p.getCity().equals("Warsaw"));
        peopleFromWarsaw.add(new Person("Jan", "Warsaw", 30));
        peopleFromWarsaw.add(new Person("Weronika", "Warsaw", 20));
        peopleFromWarsaw.add(new Person("Mikołaj", "Warsaw", 80));
        // peopleFromWarsaw.add(new Person("Waldek", "Monaco", 34)); // rzucamy wyjatkiem ConditionNotMatchedException

        System.out.println("Cala lista: ");
        for (int i = 0; i < peopleFromWarsaw.getSize(); i++) {
            System.out.println(peopleFromWarsaw.get(i));
        }

        System.out.println("\nTylko kobiety");
        List<Person> females = peopleFromWarsaw.getWithFilter(p -> p.getName().endsWith("a"));
        for (Person female : females) {
            System.out.println(female);
        }

        System.out.println("\nTylko mlodzi ludzie: ");
        peopleFromWarsaw.removeIf(p -> p.getAge() > 50);
        for (int i = 0; i < peopleFromWarsaw.getSize(); i++) {
            System.out.println(peopleFromWarsaw.get(i));
        }

        peopleFromWarsaw.storeToFile("C:\\Users\\aware\\IdeaProjects\\Kurs\\Test3Poprawka\\src\\Zadanie2\\file", p -> p.getAge() < 30, p -> p.getName() + ";" + p.getAge() + ";" + p.getCity());

        peopleFromWarsaw.storeToFile("C:\\Users\\aware\\IdeaProjects\\Kurs\\Test3Poprawka\\src\\Zadanie2\\file");

        ObjectContainer<Person> peopleFromWarsawFromFile = ObjectContainer.fromFile("C:\\Users\\aware\\IdeaProjects\\Kurs\\Test3Poprawka\\src\\Zadanie2\\file");

        System.out.println("\nSprawdzanie poprawnosci serializacji / deserializacji\n\npeopleFromWarsaww: ");
        for (int i = 0; i < peopleFromWarsaw.getSize(); i++) {
            System.out.println(peopleFromWarsaw.get(i));
        }
        System.out.println("\nwarsawPeople.txt: ");
        for (int i = 0; i < peopleFromWarsawFromFile.getSize(); i++) {
            System.out.println(peopleFromWarsawFromFile.get(i));
        }

       //  peopleFromWarsawFromFile.add(new Person("Michal", "Radom", 25)); // ConditionNotMatchedException

        }
    }

