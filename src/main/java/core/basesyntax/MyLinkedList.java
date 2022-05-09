package core.basesyntax;

import java.util.List;
import java.util.Objects;

public class MyLinkedList<T> implements MyLinkedListInterface<T> {
    private int iterator = 0;
    private Entry<T> firstElement;
    private Entry<T> lastElement;

    @Override
    public boolean add(T value) {
        addLast(value);
        return true;
    }

    @Override
    public void add(T value, int index) {
        indexCheck(index);
        if (index == iterator) {
            addLast(value);
            return;
        }
        Entry<T> foundElementByIndex = scan(index);
        if (foundElementByIndex.previous != null) {
            Entry<T> previousElement = foundElementByIndex.previous;
            Entry<T> newElement = new Entry<>(value, previousElement, foundElementByIndex);
            previousElement.next = newElement;
            foundElementByIndex.previous = newElement;
        } else {
            Entry<T> newElement = new Entry<>(value, null, foundElementByIndex);
            firstElement = newElement;
            foundElementByIndex.previous = newElement;
        }
        iterator++;
    }

    @Override
    public boolean addAll(List<T> list) {
        for (T element : list) {
            addLast(element);
        }
        return true;
    }

    @Override
    public T get(int index) {
        return scan(index).element;
    }

    @Override
    public T set(T value, int index) {
        indexCheck(index);
        Entry<T> currentElement = scan(index);
        T oldValue = currentElement.element;
        currentElement.element = value;
        return oldValue;
    }

    @Override
    public T remove(int index) {
        indexCheck(index);
        Entry<T> currentElement = scan(index);
        T banana = currentElement.element;
        unlink(currentElement);
        return banana;
    }

    @Override
    public boolean remove(T t) {
        for (Entry<T> i = firstElement; i != null; i = i.next) {
            if (Objects.equals(t, i.element)) {
                unlink(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return iterator;
    }

    @Override
    public boolean isEmpty() {
        return iterator == 0;
    }

    private void addLast(T value) {
        Entry<T> previous = lastElement;
        Entry<T> newElement = new Entry<>(value, previous, null);
        lastElement = newElement;
        if (previous == null) {
            firstElement = newElement;
        } else {
            previous.next = newElement;
        }
        iterator++;
    }

    private Entry<T> scan(int index) {
        if (index < 0 || index >= iterator) {
            throw new IndexOutOfBoundsException("Can't find " + index);
        }
        Entry<T> f;
        if (index < (iterator >> 1)) {
            f = firstElement;
            for (int i = 0; i < index; i++) {
                f = f.next;
            }
        } else {
            f = lastElement;
            for (int i = iterator - 1; i > index; i--) {
                f = f.previous;
            }
        }
        return f;
    }

    private void unlink(Entry<T> entry) {
        Entry<T> one = entry.previous;
        Entry<T> three = entry.next;
        if (three != null) {
            three.previous = one;
            entry.next = null;
        } else {
            lastElement = one;
        }
        if (one != null) {
            one.next = three;
            entry.previous = null;
        } else {
            firstElement = three;
        }
        entry.element = null;
        iterator--;
    }

    private void indexCheck(int index) {
        if (index < 0 || index > iterator) {
            throw new IndexOutOfBoundsException("Can't find such index");
        }
    }

    private static class Entry<T> {
        T element;
        Entry<T> previous;
        Entry<T> next;

        Entry(T element, Entry<T> previous, Entry<T> next) {
            this.element = element;
            this.previous = previous;
            this.next = next;
        }
    }
}
