package windparks.model

import java.awt.Toolkit
import java.net.URL
import java.text.DecimalFormatSymbols
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.loadImageBitmap

enum class WindparkState( val description: String) {
    PROJECTED("geplant"),
    OPERATIONAL("in Betrieb"),
    UNDER_CONSTRUCTION("in Bau")
}

private fun String?.asWindparkState() : WindparkState? = WindparkState.values().find { it.description == this }

//TODO: Review
// die optionalen Felder sind als nullable definiert. 'null' bedeutet also 'Wert unbekannt'
// wie ist das genau gemacht?

class Windpark(val id: Int = System.currentTimeMillis().toInt(), name: String? = null,
               status: String? = null, constructionStart: Int? = null, completion: Int? = null, installedPower_KW: Int? = null,
               production2015_MWH: Double? = null, production2016_MWH: Double? = null, production2017_MWH: Double? = null, production2018_MWH: Double? = null,
               count: Int? = null, communes: String? = null, canton: String? = null, longitude: Double? = null, latitude: Double? = null, imageUrl: String? = null) {

    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // all the attributes we need to display
    var name by mutableStateOf(name)
        private set

    var status by mutableStateOf(status.asWindparkState())
        private set

    var constructionStart by mutableStateOf(constructionStart)
        private set

    var completion by mutableStateOf(completion)
        private set

    var installedPower_KW by mutableStateOf(installedPower_KW)
        private set

    var production2015_MWH by mutableStateOf(production2015_MWH)
        private set

    var production2016_MWH by mutableStateOf(production2016_MWH)
        private set

    var production2017_MWH by mutableStateOf(production2017_MWH)
        private set

    var production2018_MWH by mutableStateOf(production2018_MWH)
        private set

    var totalProduction_MWH by mutableStateOf(totalProduction())
        private set

    var count by mutableStateOf(count)
        private set

    var communes by mutableStateOf(communes)
        private set

    var canton by mutableStateOf(canton)
        private set

    var longitude by mutableStateOf(longitude)
        private set

    var latitude by mutableStateOf(latitude)
        private set

    var imageUrl by mutableStateOf(imageUrl)
        private set

    var imageBitmap by mutableStateOf(defaultImageBitmap)
        private set

    var bitmapLoaded by mutableStateOf(false)
        private set

     fun updateName(newValue: String){
        name = newValue.ifBlank { null }
    }


    //TODO: Implementierungen ergänzen gemäss TestCase


    fun updateConstructionStart(valueAsText: String) = valueAsText.ifInt { constructionStart = it }

    fun updateCompletion(valueAsText: String)        = valueAsText.ifInt { completion = it }

    fun updateInstalledPower(valueAsText: String)    = valueAsText.ifInt { installedPower_KW = it }

    fun updateProduction2015(valueAsText: String)    = valueAsText.ifDouble { production2015_MWH = it }

    fun updateProduction2016(valueAsText: String)    = valueAsText.ifDouble { production2016_MWH = it }

    fun updateProduction2017(valueAsText: String)    = valueAsText.ifDouble { production2017_MWH = it}

    fun updateProduction2018(valueAsText: String)    = valueAsText.ifDouble { production2018_MWH = it}

    fun updateLongitude(valueAsText: String)         = valueAsText.ifDouble { longitude = it }

    fun updateLatitude(valueAsText: String)          = valueAsText.ifDouble { latitude = it }


    fun updateStatus(newValue: WindparkState){
        status = newValue
    }

    fun updateCommunes(valueAsText: String){
        communes = valueAsText.ifBlank { null }
    }

    fun updateCanton(valueAsText: String){
        canton = valueAsText.ifBlank { null }
    }

    fun updateImageURL(newValue: String){
        imageUrl = newValue
        imageBitmap = defaultImageBitmap
        bitmapLoaded = false
        loadImageBitmap()
    }

    fun loadImageBitmap(){
        //TODO: image asynchron laden
    }


    private fun totalProduction() : Double {
        var total = 0.0

        if(production2015_MWH != null){
            total += production2015_MWH!!
        }
        if(production2016_MWH != null){
            total += production2016_MWH!!
        }
        if(production2017_MWH != null){
            total += production2017_MWH!!
        }
        if(production2018_MWH != null){
            total += production2018_MWH!!
        }

        return total
    }


    // see: https://kotlinlang.org/docs/object-declarations.html#companion-objects
    companion object {
        val defaultImageBitmap = getImageBitmapFromResources("windpark-default.png")
    }

}



@OptIn(ExperimentalComposeUiApi::class)
private fun getImageBitmapFromResources(fileName: String) = ResourceLoader.Default.load(fileName).buffered().use(::loadImageBitmap)

private fun getImageBitmapFromUrl(url: String): ImageBitmap {
    return try {
        URL(url).openStream().buffered()
            .use { loadImageBitmap(it) }
    } catch (e: Exception) {
        Windpark.defaultImageBitmap
    }
}


private val ch = Locale("de", "CH")
private val chGroupingSeparator = DecimalFormatSymbols(ch).groupingSeparator

private val intCHRegex = Regex(pattern = """^\s*[+-]?[\d$chGroupingSeparator]{1,11}\s*$""")
private val floatCHRegex = Regex(pattern = """^\s*[+-]?[\d$chGroupingSeparator]{0,11}\.?\d*\s*$""")


private fun String.asDouble() = trim().replace("$chGroupingSeparator", "").toDoubleOrNull()
private fun String.asInt() = trim().replace("$chGroupingSeparator", "").toIntOrNull()


fun String.ifDouble(update: (Double?) -> Unit){
    if (this.isBlank() || floatCHRegex.matches(this)) {
        update(this.asDouble())
    } else {
        beep()
    }
}

fun String.ifInt(update: (Int?) -> Unit) =
    if (this.isBlank() || intCHRegex.matches(this)) {
        update(this.asInt())
    } else {
        beep()
    }

private fun beep() = Toolkit.getDefaultToolkit().beep()