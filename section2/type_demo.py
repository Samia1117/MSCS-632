# type_demo.py
# Demonstrates Python's type system: dynamic typing, strong typing, duck typing

# --- 1. Dynamic typing ---
# variables are not bound to a type - they can hold anything
x = 42
print(type(x))        # <class 'int'>
x = "now a string"
print(type(x))        # <class 'str'>
x = [1, 2, 3]
print(type(x))        # <class 'list'>

# --- 2. Strong typing ---
# Python refuses implicit coercion between incompatible types
a = 10
b = "5"
try:
    result = a + b    # TypeError - no silent coercion
except TypeError as e:
    print(f"TypeError: {e}")

# explicit conversion is required
result = a + int(b)
print(f"explicit conversion: {result}")  # 15

# string + string is fine, just no mixing
print("hello" + " world")   # "hello world"

# --- 3. Duck typing ---
# Python doesn't care about the declared type, only whether the object
# has the method/attribute being used
class Dog:
    def speak(self):
        return "Woof"

class Cat:
    def speak(self):
        return "Meow"

class Robot:
    def speak(self):
        return "Beep boop"

def make_it_speak(thing):
    # works on any object that has a speak() method
    print(thing.speak())

make_it_speak(Dog())
make_it_speak(Cat())
make_it_speak(Robot())

# --- 4. Type hints (Python 3.5+) - optional, not enforced at runtime ---
def add_numbers(a: int, b: int) -> int:
    return a + b

# hints are ignored at runtime - this still runs
print(add_numbers("hello", " world"))  # "hello world" - no error
