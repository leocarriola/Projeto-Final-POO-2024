import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

fun escolherTipoCombustivel(): TipoCombustivel {
    while (true) {
        println("Tipo de combustível:")
        println("  1. GASOLINA")
        println("  2. GASOLEO")
        print("Opção: ")

        when (readlnOrNull()) {
            "1" -> return TipoCombustivel.GASOLINA
            "2" -> return TipoCombustivel.GASOLEO
            else -> println("Opção inválida.")
        }
    }
}

fun InserirCarro(userId: String?) {

    if (userId == null) {
        println("Erro: Nenhum USER logado.")
        return
    }
    val sistema = GestaoImportados()

    print("Marca: ")
    val marca: String = readlnOrNull()?.toString() ?: ""

    print("Modelo: ")
    val modelo: String = readlnOrNull()?.toString() ?: ""

    print("Cor: ")
    val cor: String = readlnOrNull()?.toString() ?: ""

    var ano: Int = 0
    while (ano <= 0) {
        print("Ano: ")
        ano = readlnOrNull()?.toIntOrNull() ?: 0
        if (ano <= 0) {
            println("Ano inválido. Por favor, insira um valor válido.")
        }
    }

    var quilometragem: Double = 0.0
    while (quilometragem <= 0) {
        print("Quilometragem: ")
        quilometragem = readlnOrNull()?.toDoubleOrNull() ?: 0.0
        if (quilometragem <= 0) {
            println("Quilometragem inválida. Por favor, insira um valor válido.")
        }
    }

    var precoCompra: Double = 0.0
    while (precoCompra <= 0) {
        print("Preço de compra: ")
        precoCompra = readlnOrNull()?.toDoubleOrNull() ?: 0.0
        if (precoCompra <= 0) {
            println("Preço de compra inválido. Por favor, insira um valor válido.")
        }
    }

    var co2: Float = 0f
    while (co2 <= 0) {
        print("Co2: ")
        co2 = readlnOrNull()?.toFloatOrNull() ?: 0f
        if (co2 <= 0) {
            println("Valor de CO2 inválido. Por favor, insira um valor válido.")
        }
    }

    var cilindrada: Int = 0
    while (cilindrada <= 0) {
        print("Cilindrada: ")
        cilindrada = readlnOrNull()?.toIntOrNull() ?: 0
        if (cilindrada <= 0) {
            println("Cilindrada inválida. Por favor, insira um valor válido.")
        }
    }

    val combustivel = escolherTipoCombustivel()

    var distanciaKm: Int = 0
    while (distanciaKm <= 0) {
        print("Distância em Km: ")
        distanciaKm = readlnOrNull()?.toIntOrNull() ?: 0
        if (distanciaKm <= 0) {
            println("Distância inválida. Por favor, insira um valor válido.")
        }
    }
    val idCarro = (obterUltimoIdCarro() + 1).toString()
    val custosImportacao = sistema.calcularCustosImportacao(idCarro, userId, marca, modelo, cor, ano, quilometragem, precoCompra, co2, cilindrada, combustivel, distanciaKm)
    val custosImportacaoFormatado = String.format("%.2f", custosImportacao)
    println("Custos de importação para o carro: $custosImportacaoFormatado €")

    val custosImportacaoFloat = custosImportacaoFormatado.replace(",", ".").toFloat()
    val carro = Carro(idCarro, userId, marca, modelo, cor, ano, quilometragem, precoCompra, co2, cilindrada, combustivel, distanciaKm, custosImportacaoFloat, estado = "Disponivel")
    sistema.adicionarCarro(idCarro, userId, marca, modelo, cor, ano, quilometragem, precoCompra, co2, cilindrada, combustivel, distanciaKm, custosImportacaoFloat, estado = "Disponivel")

    salvarCarroEmArquivo(carro)
}

fun obterUltimoIdCarro(): Int {
    var ultimoIdCarro = 0

    try {
        val arquivo = File("carros.txt")
        if (arquivo.exists()) {
            val linhas = arquivo.readLines()
            if (linhas.isNotEmpty()) {
                val ultimaLinha = linhas.last()
                val partes = ultimaLinha.split(",")
                if (partes.isNotEmpty()) {
                    ultimoIdCarro = partes[0].toInt()
                }
            }
        }
    } catch (e: IOException) {
        println("Erro ao ler o arquivo: ${e.message}")
    }

    return ultimoIdCarro
}

fun salvarCarroEmArquivo(carro: Carro) {
    val nomeArquivo = "carros.txt"

    try {
        BufferedWriter(FileWriter(nomeArquivo, true)).use { writer ->
            writer.write(
                "${carro.idCarro},${carro.userId},${carro.marca},${carro.modelo},${carro.cor},${carro.ano},${carro.quilometragem},${carro.precoCompra},${carro.co2},${carro.cilindrada},${carro.tipoCombustivel.name},${carro.distanciaKm},${carro.custosImportacao},${carro.estado}\n"
            )
        }
        println("Carro disponivel para venda, com sucesso!")
        println("Pressionar enter para continuar...")
        readln()
        clearConsole()
    } catch (ex: Exception) {
        println("Erro ao salvar o carro em $nomeArquivo: ${ex.message}")
    }
}