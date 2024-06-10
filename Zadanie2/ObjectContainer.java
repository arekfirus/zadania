package Zadanie2;

import Exceptions.ConditionNotMatchedException;
import Exceptions.EmptyListException;
import Exceptions.FileReadingException;
import Exceptions.FileWritingException;
import Zadanie2.Interface.SerializablePredicate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectContainer<T> implements Serializable {
    private Node<T> head;
    private int size;
    private  Predicate<T> condition;
    private final SerializablePredicate<T> serializableCondition;

    public ObjectContainer(SerializablePredicate<T> condition) {
        this.serializableCondition = condition;
        this.condition = condition;
    }

    public void add(T object) {
        if (!condition.test(object)) {
            throw new ConditionNotMatchedException();
        }

        size++;

        if (head == null) {
            head = new Node<>(object);
            return;
        }

        Node<T> current = head;
        while (current.next != null) {
            current = current.next;
        }

        current.next = new Node<>(object);
    }

    public void remove(int index) {
        if (head == null) {
            throw new EmptyListException();
        }

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            head = head.next;
            return;
        }

        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        current.next = current.next.next;

        size--;
    }

    public void removeIf(Predicate<T> condition) {
        if (head == null) {
            throw new EmptyListException();
        }

        Node<T> current = head;
        while (current != null && current.next != null) {
            if (condition.test(current.next.data)) {
                current.next = current.next.next;
                size--;
            }
            current = current.next;
        }
    }

    public Node<T> get(int index) {
        if (index < 0 || index >= getSize()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (head == null) {
            throw new EmptyListException();
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    public List<T> getWithFilter(Predicate<T> condition) {
        List<T> withFilter = new ArrayList<>();

        if (head == null) {
            return withFilter;
        }

        Node<T> current = head;
        while (current != null) {
            if (condition.test(current.data)) {
                withFilter.add(current.data);
            }
            current = current.next;
        }

        return withFilter;

    }

    public void storeToFile(String path, Predicate<T> condition, Function<T, String> formatter) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty.");
        }

        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null.");
        }

        if (formatter == null) {
            throw new IllegalArgumentException("Formatter cannot be null.");
        }

        File file = new File(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Node<T> current = head;
            while (current != null) {
                if (condition.test(current.data)) {
                    String formattedData = formatter.apply(current.data);
                    writer.write(formattedData);
                    writer.newLine();
                }
                current = current.next;
            }
        } catch (IOException e) {
            throw new FileWritingException(path);
        }
    }


    public void storeToFile(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty.");
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(this);

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileWritingException(path);
        }
    }

    public static <T> ObjectContainer<T> fromFile(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty.");
        }

        ObjectContainer<T> objectContainer = null;

        try (FileInputStream fileInputStream = new FileInputStream(path);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            objectContainer = (ObjectContainer<T>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new FileReadingException(path);
        }

        return objectContainer;
    }

    public Node<T> getHead() {
        return head;
    }

    public Predicate<T> getCondition() {
        return condition;
    }

    public int getSize() {
        return size;
    }

    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.condition = this.serializableCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectContainer<?> that)) return false;
        return size == that.size && Objects.equals(head, that.head) && Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, condition, size);
    }

    @Override
    public String toString() {
        return "ObjectContainer - type: " + getHead().getClass() + ", size: " + getSize() + ", condition: " + getCondition();
    }
}
