import java.io.File

fun main() {
    val users = carregarUsers("users.txt").toMutableMap()
    var userId: String? = null

    println(" __________________________")
    println("|                          |")
    println("|      Login/Register      |")
    println("|                          |")
    println("| 1. Login                 |")
    println("| 2. Registar              |")
    println("|__________________________|")

    print("\n-> ")
    val opusers = readlnOrNull()?.toInt()

    when (opusers) {
        1 -> {
            userId = fazerLogin(users)
            if (userId == null) {
                return
            }
        }
        2 -> {
            val newUserId = registarNovoUser(users)
            if (newUserId != null) {
                userId = newUserId
            } else {
                return
            }
        }
        else -> {
            println("Opção inválida.")
            return
        }
    }

    var continuar = true

    while (continuar) {
        println(" _________________________")
        println("|                         |")
        println("|       Importação        |")
        println("|                         |")
        println("| 1. Vender carro         |")
        println("| 2. Ver/Comprar carros   |")

        if (verificarUltimaVenda("vendas.txt", userId)) {
            println("| 3. Ver compras          |")
        }

        if (verificarAnuncios("carros.txt", userId)) {
            println("| 4. Os meus anuncios     |")
        }

        println("|                         |")
        println("| 0. Sair                 |")
        println("|_________________________|")

        print("\n-> ")
        val op = readlnOrNull()?.toIntOrNull()

        when (op) {
            1 -> InserirCarro(userId)
            2 -> VisualizarCarro("carros.txt", userId)
            3 -> VisualizarCompras("vendas.txt", userId)
            4 -> VisualizarAnuncios("carros.txt", userId)
            0 -> {
                continuar = false
            }
            else -> println("Opção inválida")
        }

        println("Pressione enter para continuar...")
        readlnOrNull()
        clearConsole()
    }
}

fun verificarUltimaVenda(nomeArquivo: String, userID: String): Boolean {
    val linhas = File(nomeArquivo).readLines()
    return linhas.lastOrNull()?.split(",")?.lastOrNull() == userID
}

fun verificarAnuncios(nomeArquivo: String, userID: String): Boolean {
    val linhas = File(nomeArquivo).readLines()
    return linhas.any { linha ->
        val campos = linha.split(",")
        campos.getOrNull(1) == userID
    }
}