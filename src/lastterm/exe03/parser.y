%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    char name[20];
    int value;
} Variable;

int REQUIREMENTS_NUMBER_VARIABLES = 3;
int REQUIREMENTS_NUMBER_OPERATIONS = 2;
int number_variables = 0;
int number_operations = 0;
Variable symbolTable[100];

%}

%union {
    int num;        // Semantic value for numerical tokens
    char* var_name; // Semantic value for variable tokens
}

%token <num> NUM
%token <var_name> VAR
%type <num> statement
%type <num> assignment
%type <num> expression
%type <num> value
%type <num> value_or_variable
%type <num> variable
%type <num> mul_div_mod_expression

%%

statement:   /* empty */
    | assignment '\n' statement { 
        $$ = $1;
    }
    | expression '\n' { 
        if (number_variables != REQUIREMENTS_NUMBER_VARIABLES) {
            yyerror("You must create enought variables.");
        } else if (number_operations != REQUIREMENTS_NUMBER_OPERATIONS) {
            yyerror("THe expression must has 3 variables and 2 operations.");
        } else {
            printf("Result: %d\n", $1); 
            exit(EXIT_FAILURE);
        }
    }
    ;

assignment: VAR '=' NUM {
        if (number_variables >= REQUIREMENTS_NUMBER_VARIABLES) {
            yyerror("Reached maximum number of variables.");
        } else {
            for (int i = 0; i < number_variables; i++) {
                if (strcmp($1, symbolTable[i].name) == 0) {
                    yyerror("Duplicated variable name.");
                    exit(EXIT_FAILURE);
                }
            }
            strncpy(symbolTable[number_variables].name, $1, sizeof(symbolTable[number_variables].name) - 1);
            symbolTable[number_variables].value = $3;
            number_variables++;
        }
    }

expression: value_or_variable
    | mul_div_mod_expression
    | expression '+' mul_div_mod_expression { 
        $$ = $1 + $3; 
        number_operations++;
    }
    | expression '-' mul_div_mod_expression { 
        $$ = $1 - $3;
        number_operations++; 
    }
    
    
value: NUM

variable: VAR {
    int i;
    for (i = 0; i < number_variables; i++) {
        if (strcmp(symbolTable[i].name, $1) == 0) {
            $$ = symbolTable[i].value;
            break;
        }
    }

    if (i == number_variables) {
        fprintf(stderr, "Error: Variable '%s' not defined\n", $1);
    }
}

value_or_variable: value | variable 

mul_div_mod_expression: value_or_variable
    | mul_div_mod_expression '*' value_or_variable {
        $$ = $1 * $3;
        number_operations++;
    }
    | mul_div_mod_expression '/' value_or_variable {
        $$ = $1 / $3;
        number_operations++;
    }
    | mul_div_mod_expression '%' value_or_variable {
        $$ = $1 % $3;
        number_operations++;
    }

%%



void yyerror(const char *s) {
    printf("Error: %s\n", s);
    exit(EXIT_FAILURE);
}

int main() {
    yyparse();
    return 0;
}
