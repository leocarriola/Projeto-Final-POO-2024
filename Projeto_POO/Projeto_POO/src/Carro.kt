class Carro(
    val idCarro: String,
    val userId: String,
    val marca: String,
    val modelo: String,
    val cor: String,
    val ano: Int,
    val quilometragem: Double,
    val precoCompra: Double,
    val co2: Float,
    val cilindrada: Int,
    val tipoCombustivel: TipoCombustivel,
    val distanciaKm: Int,
    val custosImportacao: Float,
    var estado: String
)
