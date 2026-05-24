// C++: Calculate the sum of an array
// Bug introduced: used letter 'o' instead of digit '0' in multiple places
// The compiler catches all of these at compile time - nothing runs at all

#include <iostream>
using namespace std;

int calculateSum(int arr[], int size) {
    int total = o;           // error: 'o' was not declared in this scope
    for (int i = o; i < size; i++) {   // same error again
        total += arr[i];
    }
    return total;
}

int main() {
    int numbers[] = {1, 2, 3, 4, 5};
    int size = sizeof(numbers) / sizeof(numbers[o]);  // same error
    int result = calculateSum(numbers, size);
    cout << "Sum in C++: " << result << endl;
    return o;   // same error
}
