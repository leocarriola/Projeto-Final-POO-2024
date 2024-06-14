import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

import java.io.*

fun ComprarVeiculo(vendedor: String, idCarro: String, comprador: String) {
    val nomeArquivo_vendas = "vendas.txt"
    val nomeArquivo_carros = "carros.txt"

    try {
        // Verificar se o carro já foi vendido
        val linhasCarros = File(nomeArquivo_carros).readLines().toMutableList()
        for (linha in linhasCarros) {
            val campos = linha.split(",").toMutableList()
            if (campos[0] == idCarro && campos[campos.size - 1].equals("Vendido", ignoreCase = true)) {
                println("   Não é possível comprar este carro, já foi vendido!")
                return
            }
        }

        // Adicionar registro de venda
        BufferedWriter(FileWriter(nomeArquivo_vendas, true)).use { writer ->
            writer.write("${vendedor},${idCarro},${comprador}\n")
        }

        // Atualizar o estado do carro para "Vendido"
        for (i in linhasCarros.indices) {
            val campos = linhasCarros[i].split(",").toMutableList()
            if (campos[0] == idCarro) {
                campos[campos.size - 1] = "Vendido"
                linhasCarros[i] = campos.joinToString(",")
                break
            }
        }

        BufferedWriter(FileWriter(nomeArquivo_carros)).use { writer ->
            for (linha in linhasCarros) {
                writer.write(linha)
                writer.newLine()
            }
        }

        println("   Compra efetuada com sucesso!")
    } catch (ex: Exception) {
        println("Erro ao comprar: ${ex.message}")
    }
}
