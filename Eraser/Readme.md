# Eraser Program

## Overview
Eraser is a simple Java program that removes a specified number of spaces from the beginning of each line in a text. This is useful for cleaning up text with unnecessary indentation.
When I was working on something, I realized I needed exactly that, so I wrote it as a Java program and fixed the basic issue. I hope it helps.

## Features
- Removes a user-specified number of spaces from the beginning of each line
- If a line has fewer spaces than specified, it removes all available spaces
- Default setting removes eight spaces if no number is specified
- Processes multi-line text input

## How to Use
1. Run the program
2. Enter the text you want to process, line by line
3. Type "END" on a new line when you're done entering text
4. Enter the number of spaces you want to remove from the beginning of each line
5. The program will display the processed text with spaces removed

## Example
```
Give a text to remove the spaces from the top:
        This line has 8 spaces at the beginning.
            This line has 12 spaces at the beginning.
    This line has 4 spaces at the beginning.
END
How many spaces should be deleted from the beginning of each line?
8

Processed text:
This line has 8 spaces at the beginning.
    This line has 12 spaces at the beginning.
This line has 4 spaces at the beginning.
```

## Technical Details
- Written in Java
- Handles input validation for the number of spaces
- Uses a default value of 8 spaces if invalid input is provided
- Preserves line breaks in the original text
