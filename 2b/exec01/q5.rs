// Exercício Aula 06 - Questão 5
// ANTES de executar: qual é a saída? O problema é detectado na compilação, na execução ou nunca?
// Online: https://onecompiler.com/rust  (cole o código inteiro)
// Local:  rustc q5.rs && ./q5
fn main() {
    let s = String::from("oi");
    let t = s;
    println!("{} {}", s, t);
}
