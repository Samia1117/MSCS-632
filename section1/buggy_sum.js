// JavaScript: Calculate the sum of an array
// Two bugs introduced:
//   1. 'o' instead of '0' in the initializer -> ReferenceError at runtime
//   2. space in the function call 'calculate Sum' -> SyntaxError caught before execution

function calculateSum(arr) {
    let total = o;   // ReferenceError: o is not defined
    for (let num of arr) {
        total += num;
    }
    return total;
}

let numbers = [1, 2, 3, 4, 5];
let result = calculate Sum(numbers);   // SyntaxError: Unexpected identifier 'Sum'
console.log("Sum in JavaScript:", result);
