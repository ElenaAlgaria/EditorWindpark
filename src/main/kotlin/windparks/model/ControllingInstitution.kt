package windparks.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ControllingInstitution {
    lateinit var uiScope : CoroutineScope

    val title = "Windpark Supervisory Board"

    // anstelle einen Datenbank oder dem Einlesen der Daten von einem File, verwenden wir diese Liste von Windparks
    val allWindparks = mutableStateListOf(
            Windpark(1001, "Mont Crosin",        "in Betrieb", 1996, 2016, 37200, 56951.0, 57171.0, 74041.0, 66967.0, 16, "Saint-Imier, Villeret, Cormoret, Courtelary", "BE", 47.175833, 7.018889, "https://i.ytimg.com/vi/0zKqCNqzWcM/maxresdefault.jpg"),
            Windpark(1002, "Peuchapatte",        "in Betrieb", 2010, 2010,  6000, 14436.0, 13366.0, 13186.0, 12480.0,  3, "Le Peuchapatte",                              "JU", 47.202222, 6.962778, "https://live.staticflickr.com/5122/5369378709_0c4124ae3f_b.jpg"),
            Windpark(1003, "Saint-Brais",        "in Betrieb", 2009, 2009,  4000,  6677.0,  6142.0,  7002.0,  6813.0,  2, "Saint-Brais",                                 "JU", 47.303056, 7.103889, "http://www.uvek-gis.admin.ch/BFE/bilder/ch.bfe.windenergieanlagen/img_STB.jpg"),
            Windpark(1004, "Haldenstein",        "in Betrieb", 2012, 2013,  3000,  4278.0,  4372.0,  4137.0,  4920.0,  1, "Haldenstein",                                 "GR", 46.893611, 9.538333, "https://www.suisse-eole.ch/media/redactor/Nabben%20Benedikt%20-%20Haldenstein.jpg"),
            Windpark(1005, "Feldmoos",           "in Betrieb", 2005, 2011,  1850,  1196.0,  1081.0,  1240.0,   895.0,  2, "Entlebuch",                                   "LU", 46.988611, 8.086944, "https://www.thewindpower.net/images/image3635.jpg"),
            Windpark(1006, "Lutersarni",         "in Betrieb", 2013, 2013,  2300,  2740.0,  2787.0,  3190.0,  2462.0,  1, "Entlebuch",                                   "LU", 46.994722, 8.108056, "http://www.uvek-gis.admin.ch/BFE/bilder/ch.bfe.windenergieanlagen/img_LUT.jpg"),
            Windpark(1007, "Obergrenchenberg",   "in Betrieb", 1994, 1994,   150,   120.0,   119.0,   117.0,   100.0,  1, "Grenchen",                                    "SO", 47.232222, 7.400278, "https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Grenchenberg.jpg/800px-Grenchenberg.jpg"),
            Windpark(1008, "Gütsch",             "in Betrieb", 2002, 2012,  3300,  5060.0,  5071.0,  5046.0,  5053.0,  4, "Andermatt",                                   "UR", 46.655   , 8.615833, "https://www.suisse-eole.ch/media/redactor/Storz%20Wilfried%20-%20Gütsch_0340.jpg"),
            Windpark(1009, "Gotthard",           "in Bau",     2019, 2020, 11750,    null,    null,    null,    null,  5, "Airolo",                                      "TI",      null,     null, "https://img.nzz.ch/2016/9/7/adcece12-bba6-462f-9d2f-2a23f38b7f28.jpeg?width=400&height=224&fit=bounds&quality=75&auto=webp&crop=4256,2392,x0,y220"),
            Windpark(1010, "Charrat",            "in Betrieb", 2012, 2012,  3050,  6713.0,  7047.0,  7052.0,  6705.0,  1, "Charrat",                                     "VS", 46.131111, 7.145278, "http://www.uvek-gis.admin.ch/BFE/bilder/ch.bfe.windenergieanlagen/img_CHA.jpg"),
            Windpark(1011, "Collonges",          "in Betrieb", 2005, 2005,  2000,  4600.0,  4333.0,  4395.0,  4882.0,  1, "Collonges",                                   "VS", 46.159722, 7.037222, "http://www.uvek-gis.admin.ch/BFE/bilder/ch.bfe.windenergieanlagen/img_COL.jpg"),
            Windpark(1012, "Gries",              "in Betrieb", 2011, 2016,  9350,  1727.0,  1397.0,  7792.0,  5266.0,  4, "Obergoms",                                    "VS", 46.463056, 8.374444, "https://img.oastatic.com/img2/26510065/max/windpark-gries.jpg"),
            Windpark(1013, "Martigny",           "in Betrieb", 2008, 2008,  2000,  5040.0,  4597.0,  5195.0,  5034.0,  1, "Martigny",                                    "VS", 46.126667, 8.052222, "http://www.uvek-gis.admin.ch/BFE/bilder/ch.bfe.windenergieanlagen/img_MTG.jpg"),
            Windpark(1014, "Taggenberg 1 und 2", "in Betrieb", 2002, 2002,    13, 17533.0,    18.1,    21.5,    20.5,  2, "Winterthur",                                  "ZH", 47.51996 , 8.68391 , "http://www.uvek-gis.admin.ch/BFE/bilder/ch.bfe.windenergieanlagen/img_WIT.jpg"),
            Windpark(1015, "Grenchenberg",       "geplant",    2020, null, 16000,    null,    null,    null,    null,  6, "Grenchen",                                    "SO",      null,     null, "https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Grenchenberg.jpg/800px-Grenchenberg.jpg"),
            Windpark(1016, "Linthebene",         "geplant",    2020, null, 17500,    null,    null,    null,    null,  5, "Glarus Nord, Bilten",                         "GL",      null,     null, "https://static.wixstatic.com/media/0d4581_b70ab94a97f14101aa4def0a81380fba~mv2.jpg/v1/fill/w_572,h_238,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/Linthebene%202040%20(klein).jpg"),
            Windpark(1017, "Le Chalet-à-Gobet",  "geplant",    null, null,  null,    null,    null,    null,    null,  8 , "Lausanne",                                   "VD",      null,     null, "https://static.mycity.travel/manage/uploads/6/30/200281/1/chalet-a-gobet_2000.JPEG"),
                                         )

    // dieser WindPark soll im Editor angezeigt werden
    var windparkUnderControl : Windpark? by mutableStateOf(null)
        private set

    val explorerScrollState = LazyListState()

    fun updateWindparkUnderControl(windpark: Windpark?){
        windparkUnderControl = windpark
        windpark?.loadImageBitmap()
    }

    fun create(){
        val windpark = Windpark(System.currentTimeMillis().toInt())
        allWindparks.add(windpark)

        windparkUnderControl = windpark
        scrollTo(windpark)
    }

    fun delete(){
        if(null != windparkUnderControl){
            val idx = allWindparks.indexOf(windparkUnderControl)
            allWindparks.remove(windparkUnderControl)

            if(allWindparks.size > 0){
                val selectedIndex = idx.coerceAtMost(allWindparks.size - 1)
                windparkUnderControl = allWindparks[selectedIndex]
                scrollTo(selectedIndex)
            }
            else {
                windparkUnderControl = null
            }
        }
    }

    fun save(): String{
        println("save")
        scrollTo(allWindparks.size - 1)
        return "saved"
    }

    private fun scrollTo(windpark: Windpark){
        scrollTo(allWindparks.indexOf(windpark))
    }

    private fun scrollTo(idx: Int){
        uiScope.launch {
            explorerScrollState.animateScrollToItem(idx)
        }
    }

    fun isWindparkUnderControl(windpark: Windpark): Boolean{
        return windparkUnderControl == windpark
    }

}