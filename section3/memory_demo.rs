// memory_demo.rs
// Demonstrates Rust's ownership-based memory management:
//   - each value has exactly one owner; freed automatically when owner goes out of scope
//   - move semantics prevent use-after-free at compile time
//   - borrowing (&) lets you use a value without taking ownership
//   - the borrow checker prevents dangling references at compile time
//
// Run with: rustc memory_demo.rs -o memory_demo && ./memory_demo
// (also available at https://play.rust-lang.org)

fn main() {
    // --- 1. Ownership and automatic deallocation ---
    {
        let data = vec![1, 2, 3, 4, 5];  // heap-allocated Vec
        println!("[ownership] data[2] = {}", data[2]);
    }  // data goes out of scope here - Drop trait frees the heap memory automatically
    // no delete, no free, no GC needed

    // --- 2. Move semantics prevent use-after-move ---
    let s1 = String::from("hello");
    let s2 = s1;    // s1 is MOVED to s2; s1 is invalidated
    // println!("{}", s1);  // uncommenting this gives:
    //   error[E0382]: borrow of moved value: `s1`
    println!("[move] s2 = {}", s2);

    // --- 3. Clone when you need a real copy ---
    let s3 = s2.clone();
    println!("[clone] s2 = {}, s3 = {}", s2, s3);  // both valid

    // --- 4. Borrowing - use without taking ownership ---
    let len = get_length(&s2);   // lend s2, ownership stays with this function
    println!("[borrow] length of '{}' is {}", s2, len);  // s2 still usable

    // --- 5. Box<T> - explicit heap allocation ---
    let boxed_array = Box::new([0i32; 1000]);
    println!("[box] boxed_array[0] = {}", boxed_array[0]);
    // freed automatically when boxed_array goes out of scope

    // --- 6. Borrow checker prevents dangling references at compile time ---
    // The code below does NOT compile - uncomment to see the error:
    //
    // let dangling = make_dangling();
    //
    // fn make_dangling() -> &String {
    //     let s = String::from("gone");
    //     &s   // error[E0106]: missing lifetime specifier
    //          // s is dropped at end of function, so &s would dangle
    // }
    //
    // Rust refuses to compile this entirely - no runtime check needed.

    println!("[done] no leaks, no dangling pointers - enforced at compile time");
}

// borrows s with &String - does not take ownership
fn get_length(s: &String) -> usize {
    s.len()
}
