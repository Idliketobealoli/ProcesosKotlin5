import utils.OSController
import java.io.BufferedReader
import java.io.File
import java.nio.file.Paths
import kotlin.system.exitProcess

private lateinit var pB: Process
private lateinit var pB2: Process
private lateinit var pB3: Process
private lateinit var filterFile: String
private lateinit var onlyFile: String
private lateinit var readFile: String
private lateinit var showLines: BufferedReader
private var FS = File.separator


fun main() {
    val comprobador: Boolean = OSController.init()

    println("Iniciando lectura y filtrado\n")

    val userDir = System.getProperty("user.dir")
    val pathFile = Paths.get(userDir + FS + "data")

    var line: String?


    if (!comprobador) {
        pB = ProcessBuilder("cmd.exe", "/c", "cd $pathFile & dir").start()
        filterFile = pB.inputStream.bufferedReader().lineSequence().filter { it.contains("txt") }.joinToString("\n")
        //println(filterFile)
        //pB.waitFor()

        onlyFile = filterFile.lines().first().split(" ").last()
        //println(onlyFile)

        pB2 = ProcessBuilder("cmd.exe", "/c", "cd $pathFile & type $onlyFile").start()
        pB3 = ProcessBuilder("cmd.exe", "/c", """find "obi-wan" """).start()

        readFile = pB2.inputStream.bufferedReader().readText().lowercase()
        pB3.outputStream.bufferedWriter().use { it.write(readFile) }
        val exitValue1 = pB2.waitFor()

        showLines = pB3.inputStream.bufferedReader()
        while (showLines.readLine().also { line = it } != null) {
            println("$line\n")
            Thread.sleep(5000)
        }
        val exitValue2 = pB3.waitFor()

        if (exitValue1 == 0 && exitValue2 == 0) {
            println(
                """Ambos procesos terminaron correctamente
                |$exitValue1 and $exitValue2
            """.trimMargin()
            )
            exitProcess(1)
        } else {
            println(
                """Algun proceso no termino correctamente
                |$exitValue1 and $exitValue2
            """.trimMargin()
            )
            exitProcess(2)
        }

    } else {

        pB = ProcessBuilder("bash", "-c", "cd $pathFile && ls").start()
        filterFile = pB.inputStream.bufferedReader().lineSequence().filter { it.contains("txt") }.joinToString("\n")
        //println(filterFile)
        pB.waitFor()

        onlyFile = filterFile.lines().first().split(" ").last()
        //println(onlyFile)

        pB2 = ProcessBuilder("bash", "-c", "cd $pathFile && cat $onlyFile").start()
        pB3 = ProcessBuilder("bash", "-c", """grep obi-wan """).start()

        readFile = pB2.inputStream.bufferedReader().readText().lowercase()
        pB3.outputStream.bufferedWriter().use { it.write(readFile) }
        val exitValue1 = pB2.waitFor()

        showLines = pB3.inputStream.bufferedReader()
        while (showLines.readLine().also { line = it } != null) {
            println("$line\n")
            Thread.sleep(5000)
        }
        val exitValue2 = pB3.waitFor()

        if (exitValue1 == 0 && exitValue2 == 0) {
            println(
                """Ambos procesos terminaron correctamente
                |$exitValue1 and $exitValue2
            """.trimMargin()
            )
            exitProcess(1)
        } else {
            println(
                """Algun proceso no termino correctamente
                |$exitValue1 and $exitValue2
            """.trimMargin()
            )
            exitProcess(2)
        }
    }
}