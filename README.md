# Laboratório de Revisão de Redes de Computadores

| Campo | Valor |
|---|---|
| Aluna | Laura Campos Pontara Lopes |
| Curso | Engenharia de Software |
| Matrícula | 874230 |
| Disciplina | Laboratório de Desenvolvimento de Aplicações Móveis e Distribuídas |
| Unidade | U0, Nivelamento de Redes de Computadores e Sistemas Operacionais |

## Sobre o exercício

Este repositório reúne o laboratório de revisão de redes de computadores, implementado em torno de um mesmo cenário (uma central de avisos da turma) de quatro formas diferentes, cada uma explorando um protocolo de rede:

- **Parte A (TCP):** conversa privada entre aluno e monitor, confiável e orientada a conexão.
- **Parte B (UDP):** o mesmo pedido, mas sem garantia de entrega.
- **Parte C (Multicast):** o professor avisa todos os alunos conectados de uma vez.
- **Parte D (WebSocket):** um mural de avisos em tempo real, com vários alunos conectados simultaneamente.

Cada parte foi implementada tanto em **Java** quanto em **Python**, com evidências de execução em `evidencias/` e as respostas conceituais de cada parte em [`RESPOSTAS.md`](RESPOSTAS.md).

## Uso de IA

Conforme a nota de transparência do roteiro, declaro o uso do Claude (Anthropic) ao longo deste laboratório, nas seguintes frentes:

- **Preparação do ambiente:** diagnóstico do porquê o Maven não era reconhecido no terminal (PATH desatualizado numa sessão já aberta).
- **Depuração:** identificação e correção de bugs encontrados durante os testes, como a resposta duplicada do servidor TCP ao tratar a mensagem `hora`, o encoding incorreto do cliente TCP em Python (acentos quebrados), a interface de rede errada usada pelo cliente/servidor Multicast em Java, e a sintaxe correta do comando `mvn exec:java` no PowerShell.
- **Revisão das respostas de `RESPOSTAS.md`:** as respostas foram redigidas com apoio da IA, sempre a partir de testes e observações que eu mesma fiz (erros reais capturados, comportamento observado ao desligar servidores, etc.), e depois revisadas por mim.

## Material de referência

- [Roteiro da atividade](Roteiro%202.md)
- [Slides da aula](2_Redes.pdf)
- [Respostas](RESPOSTAS.md)
