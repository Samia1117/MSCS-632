// type_demo.cpp
// Demonstrates C++'s type system: static typing, type inference with auto,
// function overloading, and templates as compile-time structural typing

#include <iostream>
#include <string>
using namespace std;

// --- 1. Function overloading based on declared type ---
// the compiler picks the right version at compile time
int add(int a, int b) {
    cout << "[int add] ";
    return a + b;
}

double add(double a, double b) {
    cout << "[double add] ";
    return a + b;
}

// --- 2. Template - closest C++ gets to duck typing ---
// works for any type T that supports operator+
template<typename T>
T doubled(T x) {
    return x + x;
}

int main() {
    // --- 3. Static typing ---
    int a = 10;
    double b = 3.7;
    string s = "hello";

    // a = "text";  // uncomment to see: error: invalid conversion from 'const char*' to 'int'

    // --- 4. auto - compiler infers the type from the initializer ---
    auto c = 42;        // int
    auto d = 3.14;      // double
    auto e = string("world");  // string
    cout << "auto types: " << c << ", " << d << ", " << e << endl;

    // --- 5. Implicit narrowing conversion (may lose data) ---
    double f = 9.99;
    int g = (int)f;     // explicit cast required to make the intent clear
    cout << "9.99 cast to int: " << g << endl;  // 9 - truncated

    // mixing int and double - int is implicitly widened, not truncated
    double result = a + b;   // 10 + 3.7 = 13.7
    cout << "int + double: " << result << endl;

    // --- 6. Overloading demo ---
    cout << add(3, 4) << endl;        // calls int version -> 7
    cout << add(1.5, 2.5) << endl;    // calls double version -> 4.0

    // --- 7. Template demo ---
    cout << doubled(6) << endl;               // 12
    cout << doubled(string("hi")) << endl;    // "hihi"

    return 0;
}
