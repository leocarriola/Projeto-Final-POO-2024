@file:Suppress("UNREACHABLE_CODE")

import java.io.BufferedReader
import java.io.FileReader
import java.util.*

fun VisualizarCarro(nomeArquivo: String, userID: String): Boolean {
    val users = carregarUsers("users.txt")
    var carros = carregarCarros("carros.txt")
    var contador = 1

    while (true) {
        println("\nFiltrar carros por:")
        println("   1 - Marca")
        println("   2 - Preço")
        println("   3 - Cor")
        println("   4 - Ano")
        println("   5 - Quilometragem")
        println("   6 - Combustível")
        println("   7 - Mostrar Todos")

        println("\n   0 - Voltar")
        print("Escolha uma opção: ")
        val op = readlnOrNull()?.toIntOrNull() ?: -1

        when (op) {
            1 -> {
                println("Escolha a marca:")
                val marcasDisponiveis = carros.map { it.marca }.distinct()
                marcasDisponiveis.forEachIndexed { index, marca ->
                    println("   ${index + 1} - $marca")
                }
                print("\nOpção: ")

                val marcaOpcao = readlnOrNull()?.toIntOrNull()
                if (marcaOpcao != null && marcaOpcao in 1..marcasDisponiveis.size) {
                    val marcaSelecionada = marcasDisponiveis[marcaOpcao - 1]
                    val carrosFiltrados = carros.filter { it.marca == marcaSelecionada }
                    ProcessoCompra(carrosFiltrados, userID, users)
                } else {
                    println("Opção inválida.")
                }
            }
            2 -> {
                print("Insira o preço mínimo: ")
                val precoMinimo = readlnOrNull()?.toDoubleOrNull() ?: Double.MIN_VALUE
                print("Insira o preço máximo: ")
                val precoMaximo = readlnOrNull()?.toDoubleOrNull() ?: Double.MAX_VALUE
                val carrosFiltrados = carros.filter { it.precoCompra in precoMinimo..precoMaximo }
                ProcessoCompra(carrosFiltrados, userID, users)
            }
            3 -> {
                print("Insira a cor: ")
                val cor = readlnOrNull()?.trim()?.lowercase(Locale.getDefault()) ?: ""
                val carrosFiltrados = carros.filter { it.cor.lowercase(Locale.getDefault()) == cor }
                ProcessoCompra(carrosFiltrados, userID, users)
            }
            4 -> {
                print("Insira o ano mínimo: ")
                val anoMinimo = readlnOrNull()?.toIntOrNull() ?: Int.MIN_VALUE
                val carrosFiltrados = carros.filter { it.ano >= anoMinimo }
                ProcessoCompra(carrosFiltrados, userID, users)
            }
            5 -> {
                print("Insira a quilometragem máxima: ")
                val kmMaximo = readlnOrNull()?.toDoubleOrNull() ?: Double.MAX_VALUE
                val carrosFiltrados = carros.filter { it.quilometragem <= kmMaximo }
                ProcessoCompra(carrosFiltrados, userID, users)
            }
            6 -> {
                println("Escolha o tipo de combustível:")
                println("   1 - Gasolina")
                println("   2 - Gasóleo")
                print("\nOpção: ")

                val tipoCombustivelOpcao = readlnOrNull()?.toIntOrNull()
                val carrosFiltrados = when (tipoCombustivelOpcao) {
                    1 -> carros.filter { it.tipoCombustivel == TipoCombustivel.GASOLINA }
                    2 -> carros.filter { it.tipoCombustivel == TipoCombustivel.GASOLEO }
                    else -> emptyList()
                }
                ProcessoCompra(carrosFiltrados, userID, users)
            }
            7 -> {
                var carros = carregarCarros("carros.txt")
                ProcessoCompra(carros, userID, users)
            }
            0 -> return true // Voltar
            else -> println("Opção inválida. Tente novamente.")
        }
    }
}

fun mostrarCarros(carros: List<Carro>, users: Map<String, String>) {
    println("\nLista de carros:")
    if (carros.isNotEmpty()) {
        carros.forEachIndexed { index, carro ->
            val nomeProprietario = users[carro.userId]
            println(
                """
                        
                | Nº ${index + 1}
                |=========================================
                | Vendedor: $nomeProprietario
                | Marca: ${carro.marca}
                | Modelo: ${carro.modelo}
                | Preço: ${carro.precoCompra}€
                | Estado: ${carro.estado}
                |=========================================
            """.trimMargin()
            )
        }
    } else{
        println("   Não foram encontrados carros com o filtro escolhido!")
    }
}

fun ProcessoCompra(filtro: List<Carro>, userID: String, users: Map<String, String>): Boolean {
    while (true) {
        mostrarCarros(filtro, users)
        println("\nDigite o número do anuncio para ver mais detalhes ou 0 para voltar:")
        val input = readlnOrNull()

        val choice = input?.toIntOrNull()
        if (choice == null || choice !in 0..filtro.size) {
            println("Escolha inválida, por favor tente novamente.")
            continue
        } else if (choice == 0) {
            return true // Retorna true para indicar que o usuário escolheu sair
        }

        val carroEscolhido = filtro[choice - 1]
        val nomeProprietario = users[carroEscolhido.userId]
        val detalhes = """
                    | Detalhes do carro selecionado:
                    | 
                    |   Marca: ${carroEscolhido.marca}
                    |   Modelo: ${carroEscolhido.modelo}
                    |   Cor: ${carroEscolhido.cor}
                    |   Ano: ${carroEscolhido.ano}
                    |   Quilometragem: ${carroEscolhido.quilometragem}
                    |   Preço de Compra: ${carroEscolhido.precoCompra}
                    |   Emissão de CO2: ${carroEscolhido.co2}
                    |   Cilindrada: ${carroEscolhido.cilindrada}
                    |   Tipo de Combustível: ${carroEscolhido.tipoCombustivel}
                    |   Distância em KM: ${carroEscolhido.distanciaKm}
                    |   Custos de Importação: ${carroEscolhido.custosImportacao}€
                    """.trimMargin()

        val linhas = detalhes.lines()
        val largura = linhas.maxOf { it.length } + 2
        val margemEsquerda = "    "  // Quatro espaços para a margem esquerda

        println()
        val barraHorizontal = margemEsquerda + "+".padEnd(largura, '-') + "+"

        println(barraHorizontal)
        for (linha in linhas) {
            println("$margemEsquerda| ${linha.padEnd(largura - 2)} |")
        }
        println(barraHorizontal)

        var continuar = true

        while (continuar) {

            println("   1 - Comprar Carro\n   0 - Voltar")
            print("Escolha a opção:")
            val op = readlnOrNull()?.toIntOrNull()

            when (op) {
                1 -> {
                    if(carroEscolhido.userId == userID){
                        println("   Não é possivel comprar o seu carro!")
                    } else{
                        ComprarVeiculo(carroEscolhido.userId,carroEscolhido.idCarro, userID)
                    }

                    println("Pressionar enter para voltar...")
                    continuar = false
                    readln()
                }
                0 -> {
                    continuar = false
                }

                else -> println("Opção inválida")
            }

            clearConsole()
        }
    }
}