// memory_demo.cpp
// Demonstrates manual memory management in C++:
//   - raw new/delete (correct usage)
//   - memory leak (allocate, forget to free)
//   - dangling pointer (use after delete)
//   - smart pointer (unique_ptr) as the modern safe alternative
//
// Compile with leak detection:
//   g++ -std=c++14 -fsanitize=address,leak memory_demo.cpp -o memory_demo

#include <iostream>
#include <memory>
using namespace std;

// --- correct allocation and deallocation ---
void correct_alloc() {
    int* arr = new int[5];
    for (int i = 0; i < 5; i++) arr[i] = (i + 1) * 10;
    cout << "[correct] arr[2] = " << arr[2] << endl;
    delete[] arr;   // programmer's responsibility - must not forget this
    arr = nullptr;  // good habit: null the pointer after delete
}

// --- memory leak: allocate but never free ---
void leak_memory() {
    int* leaked = new int[100];
    leaked[0] = 42;
    cout << "[leak] allocated 100 ints, not freeing them" << endl;
    // leaked goes out of scope here - memory is gone but not returned to OS
    // AddressSanitizer will report this as a leak
}

// --- dangling pointer: access memory after it's been freed ---
void dangling_pointer() {
    int* p = new int(99);
    cout << "[dangling] before delete: " << *p << endl;
    delete p;
    // *p here would be undefined behavior - accessing freed memory
    // commenting out to avoid a crash, but in real code this can corrupt heap
    // cout << *p << endl;  // DO NOT DO THIS
    p = nullptr;  // set to null so any accidental dereference gives a clear crash
    cout << "[dangling] pointer nulled after delete - safe" << endl;
}

// --- smart pointer: unique_ptr handles deallocation automatically ---
void smart_pointer_demo() {
    unique_ptr<int[]> arr = make_unique<int[]>(5);
    for (int i = 0; i < 5; i++) arr[i] = (i + 1) * 10;
    cout << "[smart] arr[2] = " << arr[2] << endl;
    // no delete needed - destructor runs automatically when arr goes out of scope
    // also can't accidentally copy it (unique ownership enforced at compile time)
}

int main() {
    correct_alloc();
    leak_memory();
    dangling_pointer();
    smart_pointer_demo();
    return 0;
}
