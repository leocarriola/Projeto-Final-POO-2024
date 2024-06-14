import java.io.BufferedReader
import java.io.FileReader

fun VisualizarCompras(nomeArquivo: String, userID: String): Boolean {
    val users = carregarUsers("users.txt")
    val vendas = mutableListOf<Venda>()
    val carros = carregarCarros("carros.txt")

    try {
        BufferedReader(FileReader(nomeArquivo)).use { reader ->
            var linha: String?
            while (reader.readLine().also { linha = it } != null) {
                val partes = linha!!.split(",")
                if (partes.size == 3) {
                    val vendedor = partes[0].trim()
                    val idCarroVendido = partes[1].trim()
                    val comprador = partes[2].trim()
                    val venda = Venda(vendedor, idCarroVendido, comprador)
                    vendas.add(venda)
                }
            }
        }
    } catch (ex: Exception) {
        println("Erro ao carregar vendas do arquivo $nomeArquivo: ${ex.message}")
        return false
    }

    val comprasUser = vendas.filter { it.comprador == userID }
    if (comprasUser.isEmpty()) {
        println("Não efetuaste nenhuma compra.")
        return false
    }
    
    while (true) {
        println("As minhas compras:")
        comprasUser.forEachIndexed { index, venda ->
            val carro = carros.find { it.idCarro == venda.idCarro }
            if (carro != null) {
                println("   ${index + 1} - Carro: ${carro.marca} ${carro.modelo}")
            }
        }

        println("\nDigite o número do anúncio para ver mais detalhes ou 0 para voltar:")
        val input = readlnOrNull()

        val choice = input?.toIntOrNull()
        if (choice == null || choice !in 0..comprasUser.size) {
            println("Escolha inválida, por favor tente novamente.")
            continue
        } else if (choice == 0) {
            return true // Retorna true para indicar que o usuário escolheu sair
        }

        val carroEscolhido = carros.find { it.idCarro == comprasUser[choice - 1].idCarro }
        if (carroEscolhido != null) {
            val nomeProprietario = users[carroEscolhido.userId]
            val detalhes = """
                | Detalhes do carro selecionado:
                |
                |   Marca: ${carroEscolhido.marca}
                |   Modelo: ${carroEscolhido.modelo}
                |   Cor: ${carroEscolhido.cor}
                |   Ano: ${carroEscolhido.ano}
                |   Quilometragem: ${carroEscolhido.quilometragem}
                |   Preço de Compra: ${carroEscolhido.precoCompra}€
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

            println("Pressionar enter para voltar...")
            readln()
            clearConsole()
        }
    }

    return true
}