%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "parser.tab.h"
%}

%%
"\n" {return '\n';}
0|[1-9][0-9]* { yylval.num = atoi(yytext); return NUM; }
[a-zA-Z][a-zA-z0-9]* { yylval.var_name = strdup(yytext); return VAR;}
[-+*/=%]  { return yytext[0]; }

[ \t]    {}   // Ignore whitespace
. { printf("%s", yytext); }

%%

int yywrap() {
    return 1;
}
