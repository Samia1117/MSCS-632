// type_demo.js
// Demonstrates JavaScript's type system: dynamic typing, weak/loose typing,
// implicit coercions, and how they differ from Python

// --- 1. Dynamic typing ---
let x = 42;
console.log(typeof x);        // 'number'
x = "now a string";
console.log(typeof x);        // 'string'
x = true;
console.log(typeof x);        // 'boolean'

// --- 2. Weak typing - implicit coercion (JS-specific behavior) ---
// + with a string triggers string concatenation
console.log("5" + 1);         // "51"  (number coerced to string)
console.log("5" + true);      // "5true"

// arithmetic operators other than + coerce to numbers
console.log("5" - 1);         // 4
console.log("5" * "3");       // 15
console.log("5" / 2);         // 2.5

// booleans coerce to numbers in arithmetic
console.log(true + 1);        // 2
console.log(false + 10);      // 10

// null and undefined
console.log(null + 1);        // 1  (null -> 0)
console.log(undefined + 1);   // NaN

// --- 3. Loose vs strict equality ---
// == performs type coercion before comparing
console.log(1 == "1");        // true
console.log(0 == false);      // true
console.log(null == undefined); // true

// === does not coerce - compares type AND value
console.log(1 === "1");       // false
console.log(0 === false);     // false

// --- 4. Functions accept any type - no enforcement ---
function double(val) {
    return val + val;
}
console.log(double(5));          // 10
console.log(double("hello"));    // "hellohello"
console.log(double(true));       // 2  (true + true = 1 + 1)

// --- 5. typeof and runtime type checks ---
function safeAdd(a, b) {
    if (typeof a !== "number" || typeof b !== "number") {
        throw new TypeError(`expected numbers, got ${typeof a} and ${typeof b}`);
    }
    return a + b;
}
console.log(safeAdd(3, 4));      // 7
try {
    safeAdd(3, "4");
} catch (e) {
    console.log(e.message);      // expected numbers, got number and string
}
