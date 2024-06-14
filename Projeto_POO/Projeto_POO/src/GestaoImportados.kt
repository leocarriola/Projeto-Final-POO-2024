class GestaoImportados {
    private val carrosImportados = mutableListOf<Carro>()

    fun adicionarCarro(idCarro: String, userId: String, marca: String, modelo: String, cor: String, ano: Int, quilometragem: Double, precoCompra: Double, co2: Float, cilindrada: Int, tipoCombustivel: TipoCombustivel, distanciaKm: Int, custosImportacao: Float, estado: String) {
        carrosImportados.add(Carro(idCarro, userId, marca, modelo, cor, ano, quilometragem, precoCompra, co2, cilindrada, tipoCombustivel, distanciaKm, custosImportacao, estado))
    }

    fun calcularCustosImportacao(idCarro: String, userId: String, marca: String, modelo: String, cor: String, ano: Int, quilometragem: Double, precoCompra: Double, co2: Float, cilindrada: Int, tipoCombustivel: TipoCombustivel, distanciaKm: Int): Double {
        val isv = calcularISV(ano, co2, cilindrada)
        val custoTransporte = calcularCustoTransporte(distanciaKm)
        val InspecaoB = 76.25
        val PassagemNome = 55
        return precoCompra + isv + custoTransporte + PassagemNome + InspecaoB
    }

    private fun calcularISV(ano: Int, co2: Float, cilindrada: Int): Double {
        val fatorAno = 0.2 * ano
        val fatorCO2 = 0.6 * co2
        val fatorCilindrada = 0.4 * cilindrada
        return fatorAno + fatorCO2 + fatorCilindrada
    }

    private fun calcularCustoTransporte(distanciaKm: Int): Double {
        val custoPorKm = 0.5
        val custoSeguro = 60
        val custoMatriculasExportacao = 30
        val custoIUCExportacao = 90
        return custoPorKm * distanciaKm
    }

    fun gerirDocumentacao(carro: Carro) {
        println("Gerenciando a documentação para a importação do carro:")
        println(" - Emitindo certificados de conformidade...")
        println(" - Preparando documentos de transporte...")
        println(" - Organizando registos alfandegários...")
    }

    fun gerirVendas(cliente: String, carro: Carro) {
        println("Registrando venda do carro para o cliente $cliente:")
        println(" - Registrando o cliente no sistema...")
        println(" - Atualizando o registro de vendas...")
        println(" - Emitindo fatura e recibo...")
    }

    fun listarCarrosDisponiveis(): List<Carro> {
        return carrosImportados.toList()
    }
}