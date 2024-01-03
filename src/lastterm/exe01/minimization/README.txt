INPUT FORMAT:
The first line contains a number n - the number of state transition function.
In next n lines, each line defines a State Transition Function, contains 3 words, separate by whitespace:
	+ The first word is the Current State of function
	+ The second word is the Character Input of function
	+ The third word is the Target State of function
Nest line contains a word - the initial state.
Next line contains a number m words - m finish states.
Example for valid input form:
16
A 0 B
A 1 F
B 0 G
B 1 C
C 0 A
C 1 C
D 0 C
D 1 G
E 0 H
E 1 F
F 0 C
F 1 G
G 0 G
G 1 E
H 0 G
H 1 C
A
C