#### Conteúdo aula 03

Pesquisar uma grámatica de uma linguagem, fazer uma derivação de uma pequena parte da linguagem.
Link da gramatica,
regras,
derivação

#### Código
```
if (3 > 2) { }
```

#### Regras utilizadas
```
<if_statement>    ::= if ( <condition> ) <block>
<condition>       ::= <expression> <relational_op> <expression>
<expression>      ::= <integer_literal> | <identifier>
<relational_op>   ::= > | < | == | >= | <= | !=
<integer_literal> ::= 2 | 3
<block>           ::= { <statement_list> } | { }
```

#### Derivação
```
<if_statement>
if ( <condition> ) <block>
if ( <expression> <relation_op> <expression> ) <block>
if ( <integer_literal> <relation_op> <expression> ) <block>
if ( 3 <relative_op> <expression> ) <block>
if ( 3 > <expression> ) <block>
if ( 3 > <integer_literal> ) <block>
if ( 3 > 2 ) <block>
if ( 3 > 2 ) { }
```

![[Pasted image 20260825220342.png]]

Fonte
https://docs.oracle.com/javase/specs/jls/se8/html/jls-14.html#jls-14.9
