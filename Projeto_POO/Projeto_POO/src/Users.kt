import java.io.BufferedReader
import java.io.FileReader
import java.io.BufferedWriter
import java.io.FileWriter

fun carregarUsers(nomeArquivo: String): Map<String, String> {
    val users = mutableMapOf<String, String>()

    try {
        BufferedReader(FileReader(nomeArquivo)).use { reader ->
            var linha: String?
            while (reader.readLine().also { linha = it } != null) {
                val partes = linha!!.split(",")
                if (partes.size == 2) {
                    val id = partes[0].trim()
                    val nome = partes[1].trim()
                    users[id] = nome
                }
            }
        }
    } catch (ex: Exception) {
        println("Erro ao carregar users do arquivo $nomeArquivo: ${ex.message}")
    }

    return users
}

fun fazerLogin(users: Map<String, String>): String {
    println("______Login_______")
    var id: String?
    var nome: String?

    while (true) {
        print("ID: ")
        id = readlnOrNull()?.trim()
        print("Nome: ")
        nome = readlnOrNull()?.trim()

        if (id != null && nome != null && users.containsKey(id) && users[id] == nome) {
            clearConsole()
            println("Login bem-sucedido!")
            return id
        } else {
            println("ID ou nome inválido. Por favor, tente novamente.")
        }
    }
}


fun registarNovoUser(users: MutableMap<String, String>): String? {
    println("______Registo_______")
    print("ID: ")

    var novoId = readlnOrNull()?.trim() ?: ""
    while (users.containsKey(novoId)) {
        println("ID já está em uso. Por favor, escolha outro.")
        print("ID: ")
        novoId = readlnOrNull()?.trim() ?: ""
    }

    print("Nome: ")
    val novoNome = readlnOrNull()?.trim() ?: ""

    users[novoId] = novoNome
    try {
        BufferedWriter(FileWriter("users.txt", true)).use { writer ->
            writer.write("$novoId,$novoNome\n")
        }
        clearConsole()
        println("Registo bem-sucedido! Você foi automaticamente logado.")
        return novoId
    } catch (ex: Exception) {
        println("Erro ao salvar o novo usuário no arquivo users.txt: ${ex.message}")
        return null
    }
}