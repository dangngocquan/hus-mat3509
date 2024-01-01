%{
#include "parser.tab.h"
%}

%%
[0-9]+      { yylval.num = atoi(yytext); return NUM; }
[a-zA-Z][a-zA-z0-9]* { yylval.var_name = strdup(yytext); return VAR;}
[-+*/=%\n]  { return yytext[0]; }
[ \t]    {}   // Ignore whitespace
.           { printf("Invalid character: %s\n", yytext); }

%%

int yywrap() {
    return 1;
}
