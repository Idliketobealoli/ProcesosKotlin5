package jar

import utils.OSController
import java.io.File
import java.nio.file.Paths

private lateinit var pB: Process
private lateinit var pB2: Process
private lateinit var filterFile: String
private lateinit var onlyFile: String
private lateinit var readFile: String
private var FS = File.separator

fun main(){
    val comprobador: Boolean = OSController.init()
    val userDir = System.getProperty("user.dir")
    val pathFile = Paths.get(userDir + FS + "data" )

    if (!comprobador){
        pB = ProcessBuilder("cmd.exe", "/c", "cd $pathFile & dir").start()
        filterFile = pB.inputStream.bufferedReader().lineSequence().filter { it.contains("txt") }.joinToString("\n")
        //println(filterFile)
        pB.waitFor()

        onlyFile = filterFile.lines().first().split(" ").last()
        //println(onlyFile)

        pB2 = ProcessBuilder("cmd.exe", "/c", "cd $pathFile & type $onlyFile").start()
        pB2.waitFor()

        readFile = pB2.inputStream.bufferedReader().readText()
        println(readFile)
    } else {
        pB = ProcessBuilder("bash", "-c", "cd $pathFile && ls").start()
        filterFile = pB.inputStream.bufferedReader().lineSequence().filter { it.contains("txt") }.joinToString("\n")
        //println(filterFile)
        pB.waitFor()

        onlyFile = filterFile.lines().first().split(" ").last()
        //println(onlyFile)

        pB2 = ProcessBuilder("bash", "-c", "cd $pathFile && cat $onlyFile").start()
        pB2.waitFor()

        readFile = pB2.inputStream.bufferedReader().readText()
        println(readFile)
    }
}