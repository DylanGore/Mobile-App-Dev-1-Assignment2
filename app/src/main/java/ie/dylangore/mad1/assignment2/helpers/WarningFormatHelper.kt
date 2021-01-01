package ie.dylangore.mad1.assignment2.helpers

object WarningFormatHelper {
    @Suppress("SpellCheckingInspection")
    // Map of region codes to region names
    val regionMap: Map<String, String> = mapOf(
            "EI01" to "Carlow",
            "EI02" to "Cavan",
            "EI03" to "Clare",
            "EI04" to "Cork",
            "EI06" to "Donegal",
            "EI07" to "Dublin",
            "EI10" to "Galway",
            "EI11" to "Kerry",
            "EI12" to "Kildare",
            "EI13" to "Kilkenny",
            "EI15" to "Laois",
            "EI14" to "Leitrim",
            "EI16" to "Limerick",
            "EI18" to "Longford",
            "EI19" to "Louth",
            "EI20" to "Mayo",
            "EI21" to "Meath",
            "EI22" to "Monaghan",
            "EI23" to "Offaly",
            "EI24" to "Roscommon",
            "EI25" to "Sligo",
            "EI26" to "Tipperary",
            "EI27" to "Waterford",
            "EI29" to "Westmeath",
            "EI30" to "Wexford",
            "EI31" to "Wicklow",
            // Marine
            "EI805" to "Malin-Fair",
            "EI806" to "Fair-Belfast",
            "EI807" to "Belfast-Strang",
            "EI808" to "Strang-Carl",
            "EI809" to "Carling-Howth",
            "EI810" to "Howth-Wicklow",
            "EI811" to "Wicklow-Carns",
            "EI812" to "Carns-Hook",
            "EI813" to "Hook-Dungarvan",
            "EI814" to "Dungarvan-Roches",
            "EI815" to "Roches-Mizen",
            "EI816" to "Mizen-Valentia",
            "EI817" to "Valentia-Loop",
            "EI818" to "Loop-Slayne",
            "EI819" to "Slayne-Ennis",
            "EI820" to "Erris-Rossan",
            "EI821" to "Rossan-BloodyF",
            "EI822" to "BloodyF-Malin",
            "EI823" to "IrishSea-South",
            "EI824" to "IrishSea-IOM-S",
            "EI825" to "IrishSea-IOM-N"
    )

    @Suppress("SpellCheckingInspection")
    // List containing all counties that Met Éireann will provide warnings for, in the order that they generally use
    val allCounties: List<String> = listOf("Carlow", "Dublin", "Kildare", "Kilkenny", "Laois", "Longford", "Louth",
            "Meath", "Offaly", "Westmeath", "Wexford", "Wicklow", "Clare", "Cork", "Kerry", "Limerick", "Tipperary",
            "Waterford", "Galway", "Leitrim", "Mayo", "Roscommon", "Sligo", "Cavan", "Donegal", "Monaghan")
}