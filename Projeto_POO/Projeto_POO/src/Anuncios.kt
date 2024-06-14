import java.io.BufferedReader
import java.io.FileReader
import java.io.File
import java.io.*

fun VisualizarAnuncios(nomeArquivo: String, userID: String): Boolean {
    val users = carregarUsers("users.txt")
    var carros = carregarCarros("carros.txt")
    val carrosVendidos = mutableListOf<String>()

    try {
        val vendasReader = BufferedReader(FileReader("vendas.txt"))
        var linhaVendas: String?
        while (vendasReader.readLine().also { linhaVendas = it } != null) {
            val partes = linhaVendas!!.split(",")
            if (partes.size >= 2) {
                val idCarroVendido = partes[1].trim()
                carrosVendidos.add(idCarroVendido)
            }
        }

        carros.forEach { carro ->
            if (carro.idCarro in carrosVendidos) {
                carro.estado = "vendido"
            }
        }

    } catch (ex: Exception) {
        println("Erro ao carregar carros do arquivo $nomeArquivo: ${ex.message}")
        return false
    }

    while (true) {
        println("\nOs meus Anuncios:\n")
        carros = carregarCarros("carros.txt").filter { it.userId == userID }
        if (carros.isEmpty()) {
            println("   Não foram encontrados anuncios!")
        } else {
            carros.forEachIndexed { index, carro ->
                println("     ${index + 1} - Carro: ${carro.modelo}, Preço: ${carro.precoCompra}€")
            }
        }

        println("\nInsira o número do anuncio para ver mais detalhes ou 0 para voltar:")
        val input = readlnOrNull()

        val choice = input?.toIntOrNull()
        if (choice == null || choice !in 0..carros.size) {
            println("Escolha inválida, por favor tente novamente.")
            continue
        } else if (choice == 0) {
            return true // Retorna true para indicar que o usuário escolheu sair
        }

        val carroEscolhido = carros[choice - 1]
        val nomeProprietario = users[carroEscolhido.userId]
        val detalhes = """
            | Detalhes do carro selecionado:
            | 
            |   Marca: ${carroEscolhido.marca}
            |   Modelo: ${carroEscolhido.modelo}
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

            println("\n   1 - Editar Anuncio\n   2 - Eliminar Anuncio\n   0 - Voltar")
            print("Escolha uma opção: ")
            val op = readlnOrNull()?.toIntOrNull()

            when (op) {
                1 -> {
                    if (carroEscolhido.estado != "Vendido") {
                        EditarAnuncio(carroEscolhido.idCarro, carroEscolhido.userId)
                    } else{
                        println("   Este carro foi vendido, não é possivel edita-lo!")
                    }
                }
                2 -> EliminarAnuncio(carroEscolhido.idCarro)
                0 -> {
                    continuar = false
                }

                else -> println("Opção inválida")
            }

            println("Pressionar enter para voltar...")
            continuar = false
            readln()
            clearConsole()
        }
    }
}

fun EditarAnuncio(idCarro: String, userID: String): Boolean {
    val arquivo = "carros.txt"
    val file = File(arquivo)

    if (!file.exists()) {
        println("Arquivo não encontrado.")
        return false
    }

    println("Selecione o campo que deseja editar:")
    println("   1 - Marca")
    println("   2 - Modelo")
    println("   3 - Cor")
    println("   4 - Ano")
    println("   5 - Quilometragem")
    println("   6 - Preço de compra")
    println("   7 - Emissão de CO2")
    println("   8 - Cilindrada")
    println("   9 - Distância em KM")

    print("\nInsira o número correspondente ao campo: ")
    val opcao = readlnOrNull()?.toIntOrNull() ?: return false

    println("Insira o novo valor:")
    val novoValor = readlnOrNull()?.trim() ?: return false

    val linhasEditadas = mutableListOf<String>()
    var encontrado = false

    file.forEachLine { linha ->
        val sistema = GestaoImportados()
        val campos = linha.split(",")
        if (campos.size == 14 && campos[0] == idCarro && campos[1] == userID) {
            encontrado = true
            val tipoCombustivel: TipoCombustivel = when (campos[10]) {
                "GASOLINA" -> TipoCombustivel.GASOLINA
                "GASOLEO" -> TipoCombustivel.GASOLEO
                else -> throw IllegalArgumentException("Tipo de combustível inválido: ${campos[10]}")
            }

            val novaLinha = when (opcao) {
                1 -> "${campos[0]},${userID},${novoValor},${campos[3]},${campos[4]},${campos[5]},${campos[6]},${campos[7]},${campos[8]},${campos[9]},${campos[10]},${campos[11]}"
                2 -> "${campos[0]},${userID},${campos[2]},${novoValor},${campos[4]},${campos[5]},${campos[6]},${campos[7]},${campos[8]},${campos[9]},${campos[10]},${campos[11]}"
                3 -> "${campos[0]},${userID},${campos[2]},${campos[3]},${novoValor},${campos[5]},${campos[6]},${campos[7]},${campos[8]},${campos[9]},${campos[10]},${campos[11]}"
                4 -> "${campos[0]},${userID},${campos[2]},${campos[3]},${campos[4]},${novoValor},${campos[6]},${campos[7]},${campos[8]},${campos[9]},${campos[10]},${campos[11]}"
                5 -> "${campos[0]},${userID},${campos[2]},${campos[3]},${campos[4]},${campos[5]},${novoValor},${campos[7]},${campos[8]},${campos[9]},${campos[10]},${campos[11]}"
                6 -> "${campos[0]},${userID},${campos[2]},${campos[3]},${campos[4]},${campos[5]},${campos[6]},${novoValor},${campos[8]},${campos[9]},${campos[10]},${campos[11]}"
                7 -> "${campos[0]},${userID},${campos[2]},${campos[3]},${campos[4]},${campos[5]},${campos[6]},${campos[7]},${novoValor},${campos[9]},${campos[10]},${campos[11]}"
                8 -> "${campos[0]},${userID},${campos[2]},${campos[3]},${campos[4]},${campos[5]},${campos[6]},${campos[7]},${campos[8]},${novoValor},${campos[10]},${campos[11]}"
                9 -> "${campos[0]},${userID},${campos[2]},${campos[3]},${campos[4]},${campos[5]},${campos[6]},${campos[7]},${campos[8]},${campos[9]},${campos[10]},${novoValor}"
                else -> linha
            }

            // Recalcular custos de importação
            val camposAtualizados = novaLinha.split(",")
            val custosImportacao = sistema.calcularCustosImportacao(
                idCarro, userID, camposAtualizados[2], camposAtualizados[3], camposAtualizados[4], camposAtualizados[5].toInt(),
                camposAtualizados[6].toDouble(), camposAtualizados[7].toDouble(), camposAtualizados[8].toFloat(), camposAtualizados[9].toInt(),
                tipoCombustivel, camposAtualizados[11].toInt()
            )
            val custosImportacaoFormatado = String.format("%.2f", custosImportacao)
            val custosImportacaoFloat = custosImportacaoFormatado.replace(",", ".").toFloat()
            val novaLinhaComCustos = "$novaLinha,$custosImportacaoFloat,${campos[13]}"

            linhasEditadas.add(novaLinhaComCustos)
        } else {
            linhasEditadas.add(linha)
        }
    }

    if (!encontrado) {
        println("Carro não encontrado.")
        return false
    }

    file.writeText(linhasEditadas.joinToString("\n"))
    return true
}


fun EliminarAnuncio(idCarro: String) {
    val arquivo = "carros.txt"
    val carros = carregarCarros(arquivo).toMutableList()

    val iterator = carros.iterator()
    var encontrou = false

    while (iterator.hasNext()) {
        val carro = iterator.next()
        if (carro.idCarro == idCarro) {
            iterator.remove()
            encontrou = true
            break
        }
    }

    if (encontrou) {
        File(arquivo).writeText(carros.joinToString("\n") {
            "${it.idCarro},${it.userId},${it.marca},${it.modelo},${it.cor},${it.ano},${it.quilometragem},${it.precoCompra},${it.co2},${it.cilindrada},${it.tipoCombustivel},${it.distanciaKm},${it.custosImportacao},${it.estado}"
        })
        println("Anúncio removido com sucesso!")
    } else {
        println("Anúncio não encontrado.")
    }
}