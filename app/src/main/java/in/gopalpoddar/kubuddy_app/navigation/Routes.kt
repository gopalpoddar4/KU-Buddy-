package `in`.gopalpoddar.kubuddy_app.navigation

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val LIST = "list"

    fun listWithArgs(materialType: String,semester: String): String{
        return "list/$materialType/$semester"
    }
    const val LIST_WITH_ARGS = "list/{materialType}/{semester}"
    const val PDF_WITH_ARGS = "pdf/{pdfkey}/{pdfName}"
    const val SETTINGS = "settings"
}