#ifndef STORE_H
#define STORE_H

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <cstdlib>

using namespace std;

inline vector<string> splitLine(const string& line, char delim) {
    vector<string> tokens;
    string token = "";
    for (size_t i = 0; i < line.length(); ++i) {
        if (line[i] == delim) {
            tokens.push_back(token);
            token = "";
        } else {
            token += line[i];
        }
    }
    tokens.push_back(token);
    return tokens;
}

// Class template managing heap objects of type T
template <typename T>
class Store {
private:
    vector<T*> items;

public:
    Store() {}
    ~Store() { clear(); }

    void clear() {
        for (size_t i = 0; i < items.size(); ++i) delete items[i];
        items.clear();
    }

    void add(T* item) {
        if (item != NULL) items.push_back(item);
    }

    bool remove(int id) {
        for (typename vector<T*>::iterator it = items.begin(); it != items.end(); ++it) {
            if ((*it)->getId() == id) {
                delete *it;
                items.erase(it);
                return true;
            }
        }
        return false;
    }

    T* findById(int id) const {
        for (size_t i = 0; i < items.size(); ++i) {
            if (items[i]->getId() == id) return items[i];
        }
        return NULL;
    }

    size_t size() const { return items.size(); }
    const vector<T*>& getAll() const { return items; }

    bool saveToFile(const string& filename) const {
        ofstream fout(filename.c_str());
        if (!fout.is_open()) return false;
        for (size_t i = 0; i < items.size(); ++i) {
            fout << items[i]->toLine() << "\n";
        }
        fout.close();
        return true;
    }
};

// Function template
template <typename T>
void showAll(const Store<T>& store) {
    const vector<T*>& list = store.getAll();
    if (list.empty()) {
        cout << "No records found.\n";
        return;
    }
    for (size_t i = 0; i < list.size(); ++i) list[i]->show();
}

#endif
