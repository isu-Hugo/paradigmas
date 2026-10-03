#### Objetivo da aula
Entender como as linguagens avaliam expressões e controlam o fluxo de execução.

Este conteúdo é importante para não cair em "pegadinhas" na análise de códigos, como diferenças entre linguagens, "se tem placa tem história", erros assim já aconteceram em projetos grandes, como comparar valores no PHP de forma errada.

#### Quiz de abertura 
_palpites antes de executar:_
1 Python: 4 16
2 Java: 2 3 9
3 JS: 3 52
4 C: 1
5 Python: True True
6 JS: true true true

_slide 06_
As linguagens trabalham de formas diferentes com a representação das funções aritméticas, isso faz com que ao não tomarmos cuidado com o entendimento e funcionamento da linguagem, criaremos erros que visualmente parecem corretos, mas o calculo é completamente errado e inesperado.
No caso do python por exemplo, a expressão ** tem uma precedência maior que o operador - assim calculando a exponenciação primeiro e após aplicando o valor negativo.
Uma forma de ""concertar"" o comportamento é aplicar explicitamente o comportamento usando ( ), no caso do python, poderia ser escrito como:
`print( (-2) **2 )`
![[Pasted image 20261002204658.png]]
Assim a atribuição do valor negativo acontece com uma precedência maior.


_slide 07_
A ordem de execuções em chamadas de funções interfere diretamente do resultado quando executadas pela direita ou esquerda, perceptível quando essa função altera valores globais, como
``` Java
int a = 5;
int fun1(void) {
    a = 17;        /* efeito colateral */
    return 3;
}
int main(void) {
    a = a + fun1();   /* 8 ou 20? */
    printf("a = %d\n", a);
}
```

O resultado é `8` por conta da ordem de execução, da esquerda para a direita, mas em outras linguagens como C ou GO. Esse comportamento pode variar e o valor pode ser `20`. 

_slide 08_
Sobrecarga de operadores, os mesmo operadores como  `+` podem ser usados para somar e concatenar, um exemplo fácil de perceber algo interessante é no Java, onde um `char` sofre um cast para inteiro quando somado
![[Pasted image 20261002205145.png]]
O caractere 'a' é traduzido para `97`, que pode ser adicionado 1.

_slide 09_
Algumas linguagens fazem a conversão de tipos quando necessário, como no Java de `int -> Long`(Alargamento) . Isso não é uma regra que acontece em todas as linguagens por isso podem ser causados erros de tipagem e conversão automática.
Mas também pode acontecer o inverso com o (Estreitamento) no caso de 
`double -> int`, isso pode causar uma perda de informações do valor armazenado.

_slide 10_
Coerção explicita, linguagens podem tratar valores de forma "estranha" ao fazer comparações de tipos diferentes ou propriedades, por exemplo o JS
![[Pasted image 20261002210455.png]]
Ele faz uma coerção na `linha 1`, por conta que uma `string` não pode ser subtraída, se tornando um inteiro para realizar a operação, já na `linha 2`, uma string pode ser concatenada pelo operador `+`.

_slide 11_
Expressões relacionais e booleanas, a interpretação das linguagens em IFs podem ser diferentes e ter comportamentos diferentes, comparando C e Python em um IF
``` c
int a = 3, b = 2, c = 1;
printf("%d\n", a > b > c);    // 0 !
// (3 > 2) vale 1, e 1 > 1 é falso
```

```python
a, b, c = 3, 2, 1
print(a > b > c)   
# True: (a > b) and (b > c)
```

Em casos de linguagens mais 'rígidas', são aceitas apenas valores booleanos, como no caso de Java, Rust...
``` java
boolean trueCond = true;
if (trueCond) {}
```

_slide 12_
Avaliação de curto circuito, em Java por exemplo podemos fazer a comparação com 
`& ou &&`, quando usamos `&` são comparadas os dois lados da expressão, mas ao usar o  `&&` caso o lado esquerdo seja falso, o lado direito não é executado.
![[Pasted image 20261002212914.png]]

_slide 13_ (revisar exemplos)
Sentenças de atribuição, em python por exemplo podemos fazer atribuições de formas 'diferentes' do convencional, como por exemplo a troca de valores entre variáveis sem a necessidade de uma intermediaria
```python
a, b = b, a
```
Ou em C, que um valor pode ser atribuído e testado dentro de um mesmo IF
```c
if (x = y) { ... }
```

_slide 15_
Estruturas de controle, elas existem para que o programador possa tomar decisões dentro do código, facilitando a legibilidade e compreensão do código
Sequência: 
	a = 1;
	b = a + 2;
Seleção:
	If (x > 0) 
Iteração:
	while (x > 0)
	x --;

_slide 16 e 17_
Temos os seletores de caminhos, os IFs, em que podem existir ou não os ELSEs, normalmente a regra de um else é, pertence ao if mais próximo que não possui else.
E como complemento para os caminhos de decisões, temos os switchs, e sua evolução simplificada no Java com o switch como expressão.

_slide 18 e 19_
Laços controlados por valores ou condições, neles é possível definir a repetição de uma lógica, baseada em um valor específico ou condição como true/false

_slide 20_ 
Desvio incondicional com o goto, é um comando que literalmente significa "vá para", que pode ser executado sem uma condicional.

**Realização dos execícios**
1
``` js
const precos = [10, 20, 30];
let total = 0;
for (const p in precos)
total += p;
console.log("Total: " + total);
```
Os índices do js são tratados como string no for, o que causa (0 0 1 2), concatenação de strings

2
```python
def desconto(preco, d=None):
    d = d or 10     # padrão: 10%
    return preco * (100 - d) / 100
print(desconto(200, 0))
```
O valor 0 passado como parâmetro para a função é interpretado como falso e substituído para o valor 10 padrão

3
``` java
int dia = 2; String nome = "";
switch (dia) {
  case 1: nome = "domingo";
  case 2: nome = "segunda";
  case 3: nome = "terça";
  default: nome = "inválido";
}
System.out.println(nome);
```
A falta de `break;` em cada `case` faz com que o case entre no 2 e execute todos abaixo até o default.

4
```c
int saldo = 100, saque = 50;
if (saque > saldo);
    printf("saldo insuficiente\n");
printf("fim\n");
```
O `;` após o `if(...); <-` faz com que independente da condição, ele não tenha um corpo para ser executado, a próxima linha é simplesmente executada normalmente, a edentação é desconsiderada em C.

