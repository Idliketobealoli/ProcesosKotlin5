import utils.OSController
import java.io.BufferedReader
import java.io.File
import java.nio.file.Paths
import kotlin.system.exitProcess

// Todas estas variables las usaremos en ambas partes del if, asi evitamos la generacion de 200 variables
private lateinit var pB: Process
private lateinit var pB2: Process
private lateinit var pB3: Process
private lateinit var filterFile: String
private lateinit var selectFile: String
private lateinit var readFile: String
private lateinit var showLines: BufferedReader

// Es el ejercicio 6, funcional tanto para Windows como Linux/Mac
fun main() {
    // Hacemos uso del comprobador del sistema operativo, que luego usaremos en el if principal de la ejecucion del main.
    val comprobador: Boolean = OSController.init()

    println("Iniciando lectura y filtrado\n")

    // Preparamos la ruta del archivo txt a leer.
    val userDir = System.getProperty("user.dir")
    val pathFile = Paths.get(userDir + File.separator + "data")

    // Es una variable que nos servira para leer/mostrar las diferentes lineas del archivo
    var line: String?

    // Primer IF = Windows || Else = Linux
    if (!comprobador) {

        // Haremos uso del primer proceso, nos desplazaremos hasta la ruta del archivo y haremos un dir/ls del directorio
        pB = ProcessBuilder("cmd.exe", "/c", "cd $pathFile & dir").start()
        // Recogemos la salida del anterior proceso y filtramos para los archivos que contengan txt en este caso
        filterFile = pB.inputStream.bufferedReader().lineSequence().filter { it.contains("txt") }.joinToString("\n")
        //println(filterFile)

        /*
        * En este momento, podremos "escoger el archivo en especifico, sin los demas datos,
        * para ver la diferencia claramente, se pueden descomentar ambos println que justo estan arriba y abajo de
        * este comentario.
        */
        selectFile = filterFile.lines().first().split(" ").last()
        //println(selectFile)

        /*
        * Aqui realizamos los siguientes y ultimos procesos necesarios:
        * El primero es, de nuevo, un desplazamiento a la ruta del archivo, sumado con un type/cat del archivo que antes
        * seleccionamos.
        * El segundo se encargara de realizar la busqueda de la palabra clave "obi-wan" con find/grep
        */
        pB2 = ProcessBuilder("cmd.exe", "/c", "cd $pathFile & type $selectFile").start()
        pB3 = ProcessBuilder("cmd.exe", "/c", """find "obi-wan" """).start()

        // Aqui leemos el archivo entero, aplicando ademas un lowercase para evitar problemas con la busqueda
        readFile = pB2.inputStream.bufferedReader().readText().lowercase()
        pB3.outputStream.bufferedWriter().use { it.write(readFile) }
        val exitValue1 = pB2.waitFor()

        // Aqui mostraremos las lineas del archivo que contengan la palabra clave, dejando 5 segundos entre cada linea
        showLines = pB3.inputStream.bufferedReader()
        while (showLines.readLine().also { line = it } != null) {
            println("$line\n")
            Thread.sleep(5000)
        }
        val exitValue2 = pB3.waitFor()

        // Por ultimo, nos aseguramos de que los dos procesos hayan terminado corretamente, y cerramos el programa
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

        // La parte Linux es exactamente la misma, pero las diferencias entre procesos hacen que sea necesario esta separacion
    } else {

        pB = ProcessBuilder("bash", "-c", "cd $pathFile && ls").start()
        filterFile = pB.inputStream.bufferedReader().lineSequence().filter { it.contains("txt") }.joinToString("\n")
        //println(filterFile)
        pB.waitFor()

        selectFile = filterFile.lines().first().split(" ").last()
        //println(selectFile)

        pB2 = ProcessBuilder("bash", "-c", "cd $pathFile && cat $selectFile").start()
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