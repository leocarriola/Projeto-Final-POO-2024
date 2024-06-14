import java.io.BufferedReader
import java.io.FileReader

fun carregarCarros(nomeArquivo: String): List<Carro> {
    val carros = mutableListOf<Carro>()
    try {
        BufferedReader(FileReader(nomeArquivo)).use { reader ->
            var linha: String?
            while (reader.readLine().also { linha = it } != null) {
                val partes = linha!!.split(",")
                if (partes.size == 14) {
                    val carro = Carro(
                        idCarro = partes[0].trim(),
                        userId = partes[1].trim(),
                        marca = partes[2].trim(),
                        modelo = partes[3].trim(),
                        cor = partes[4].trim(),
                        ano = partes[5].trim().toInt(),
                        quilometragem = partes[6].trim().toDouble(),
                        precoCompra = partes[7].trim().toDouble(),
                        co2 = partes[8].trim().toFloat(),
                        cilindrada = partes[9].trim().toInt(),
                        tipoCombustivel = TipoCombustivel.valueOf(partes[10].trim()),
                        distanciaKm = partes[11].trim().toInt(),
                        custosImportacao = partes[12].trim().toFloat(),
                        estado = partes[13].trim()
                    )
                    carros.add(carro)
                }
            }
        }
    } catch (ex: Exception) {
        println("Erro ao carregar carros do arquivo $nomeArquivo: ${ex.message}")
    }
    return carros
}
