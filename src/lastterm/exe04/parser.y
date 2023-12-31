%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    char name[20];
    int value;
} VariableEntry;

VariableEntry symbolTable[100];
int size = 0;


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

%%

statement:   /* empty */
    | assignment '\n' statement { 
        $$ = $1;
    }
    | expression '\n' { 
        if (size != 3) {
            yyerror("You must create enought 3 variables.");
        } else {
            printf("Result: %d\n", $1); 
            exit(EXIT_FAILURE);
        }
    }
    ;

assignment: VAR '=' NUM {
        if (size >= 3) {
            yyerror("Reached maximum number of variables.");
        } else {
            strncpy(symbolTable[size].name, $1, sizeof(symbolTable[size].name) - 1);
            symbolTable[size].value = $3;
            size++;
        }
    }

expression:   NUM
    | VAR {
        int i;
        for (i = 0; i < 3; i++) {
            if (strcmp(symbolTable[i].name, $1) == 0) {
                $$ = symbolTable[i].value;
                break;
            }
        }

        if (i == 3) {
            fprintf(stderr, "Error: Variable '%s' not defined\n", $1);
        }
    }
    | expression '+' expression { 
        $$ = $1 + $3; 
    }
    | expression '-' expression { 
        $$ = $1 - $3; 
    }
    | expression '*' expression { 
        $$ = $1 * $3; 
    }
    | expression '/' expression { 
        if ($3 != 0) $$ = $1 / $3; else yyerror("Division by zero"); 
    }
    | expression '%' expression {
         if ($3 != 0) $$ = $1 % $3; else yyerror("Modulo by zero"); 
    }
    ;

%%

void yyerror(const char *s) {
    printf("Error: %s\n", s);
    exit(EXIT_FAILURE);
}

int main() {
    yyparse();
    return 0;
}
