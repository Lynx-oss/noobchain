# NoobChain - Implementación Básica de Blockchain en Java

Este proyecto es una implementación didáctica de una cadena de bloques (Blockchain) construida desde cero en Java. Se basa en el ecosistema básico de UTXO (Unspent Transaction Output) similar a Bitcoin y utiliza criptografía para asegurar su integridad.

## Características
*   **(Proof-of-Work)**: Sistema de minería ajustado por dificultad.
*   **Transacciones Seguras**: Implementadas con firmas digitales mediante criptografía de curva elíptica (ECDSA) con BouncyCastle.
*   **Sistema de UTXO**: Manejo estricto de dinero no gastado para evitar el doble gasto.
*   **Árbol de Hash (Merkle Tree)**: Condensación de transacciones dentro del bloque mediante iteración SHA-256.

## Tecnologías Utilizadas
*   Java
*   BouncyCastle (Criptografía)
*   Gson (Estructuración JSON)

## Aprendizajes Clave
Con este proyecto pude comprender los conceptos base y mecánicas internas que mantienen seguras a las criptomonedas, como la relación clave pública/privada para validar balances, el sistema que impide gastar tu dinero mas de una vez y por qué minar un bloque requiere procesamiento real de CPU.
