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

void yyerror(const char *s);
int yylex(void);

extern FILE* yyin;

%}

%union {
    int num;        // Semantic value for numerical tokens
    char* var_name; // Semantic value for variable tokens
}

%token <num> NUM
%token <var_name> VAR
%type <num> program
%type <num> assignment
%type <num> expression
%type <num> variable
%type <num> mul_div_mod_expression

%left '+' '-'
%left '*' '/'
%left '%'

%%

program:   
    assignment '\n' assignment '\n' assignment '\n' expression { 
        if (number_variables != REQUIREMENTS_NUMBER_VARIABLES) {
            yyerror("You must create enought variables.");
        } else if (number_operations != REQUIREMENTS_NUMBER_OPERATIONS) {
            yyerror("The expression must has 3 variables and 2 operations.");
        } else {
            FILE* output_file = fopen("output.txt", "w");
            if (!output_file) {
                perror("Error opening output file");
                exit(EXIT_FAILURE);
            }

            fprintf(output_file, "Result: %d\n", $7); 
            fclose(output_file);

            exit(EXIT_SUCCESS);
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

expression: mul_div_mod_expression
    | expression '+' mul_div_mod_expression { 
        $$ = $1 + $3; 
        number_operations++;
    }
    | expression '-' mul_div_mod_expression { 
        $$ = $1 - $3;
        number_operations++; 
    }

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

mul_div_mod_expression: variable
    | mul_div_mod_expression '*' variable {
        $$ = $1 * $3;
        number_operations++;
    }
    | mul_div_mod_expression '/' variable {
        if ($3 == 0) {
            yyerror("Divide by zero.");
        }
        $$ = $1 / $3;
        number_operations++;
    }
    | mul_div_mod_expression '%' variable {
        if ($3 == 0) {
            yyerror("Modulo by zero.");
        }
        $$ = $1 % $3;
        number_operations++;
    }

%%



void yyerror(const char *s) {
    printf("Error: %s\n", s);
    exit(EXIT_FAILURE);
}

int main() {
    FILE* file = fopen("input.txt", "r");  // Open the input file for reading
    if (!file) {
        perror("Error opening file");
        return EXIT_FAILURE;
    }

    yyin = file;  // Set the global input file pointer

    yyparse();

    fclose(file);  // Close the input file

    return 0;
}
