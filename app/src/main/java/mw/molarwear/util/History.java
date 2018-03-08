package mw.molarwear.util;

import java.util.LinkedList;

/**
 * @author Sean Pesce
 */

public class History<E> {

    private final LinkedList<E> _history = new LinkedList<>();

    private int _maxEntries = 0; // If zero, no history limit
    private boolean _allowRepeatItems = false;


    //////////// Constructors ////////////

    public History(){}

    public History(int maxEntries) {
        _maxEntries = maxEntries;
    }


    //////////// Actions ////////////

    public E goBack() {
        return pop();
    }

    //////////// Accessors ////////////

    public boolean isEmpty() {
        return _history.isEmpty();
    }

    public boolean isFull() {
        return (_maxEntries > 0) && (_history.size() >= _maxEntries);
    }

    public int size() {
        return _history.size();
    }

    public int maxSize() {
        return _maxEntries;
    }

    public boolean repeatItemsAllowed() {
        return _allowRepeatItems;
    }


    //////////// Mutators ////////////

    public void clear() {
        _history.clear();
    }

    public void add(E item) {
        if ((!_allowRepeatItems) && (!_history.isEmpty()) && item.equals(_history.getLast())) {
            return;
        }
        if ((_maxEntries > 0) && (_history.size() >= _maxEntries)) {
            _history.removeFirst();
        }
        _history.addLast(item);
    }

    public E pop() {
        if (_history.isEmpty()) {
            return null;
        }
        return _history.removeLast();
    }

    public void setMaxSize(int maxSize) {
        if (maxSize >= 0) {
            _maxEntries = maxSize;
        }
        while (_maxEntries > 0 && _history.size() > _maxEntries) {
            _history.removeFirst();
        }
    }
}
