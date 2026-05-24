# Python: Calculate the sum of an array
# Bug introduced: used letter 'o' instead of digit '0'
# This causes a NameError at runtime - Python doesn't catch it until the line executes

def calculate_sum(arr):
    total = o   # NameError: name 'o' is not defined
    for num in arr:
        total += num
    return total

numbers = [1, 2, 3, 4, 5]
result = calculate_sum(numbers)
print("Sum in Python:", result)
