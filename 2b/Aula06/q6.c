/* Exercício Aula 06 - Questão 6
 * ANTES de executar: qual é a saída? O problema é detectado na compilação, na execução ou nunca?
 * Online: https://onecompiler.com/c  (cole o código inteiro)
 * Local:  gcc q6.c -o q6 && ./q6
 */
#include <stdio.h>

int main(void) {
    union { int i; float f; } u;
    u.f = 1.0f;
    printf("%d\n", u.i);
    return 0;
}
