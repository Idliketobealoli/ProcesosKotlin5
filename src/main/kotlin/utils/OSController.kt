package utils

object OSController {

    fun init(): Boolean{
        var controller = false
        val os = System.getProperty("os.name").lowercase()

        if (os.contains("linux") || os.contains("mac")) controller = true
        println("$os\n")
        //println(controller)

        return controller
    }

}