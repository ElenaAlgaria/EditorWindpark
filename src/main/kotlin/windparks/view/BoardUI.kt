package windparks.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import windparks.model.ControllingInstitution
import windparks.model.Windpark
import windparks.model.WindparkState

@Composable
fun ApplicationScope.SupervisoryBoardWindow(institution: ControllingInstitution) {
    Window(
        title = institution.title,
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            width = 1000.dp,
            height = 700.dp,
            position = WindowPosition(Alignment.Center)
        )
    ) {
        TheUI(institution)
    }
}

@Composable
private fun TheUI(institution: ControllingInstitution) {

    // some actions, e.g. scrolling,  must be executed in UI-scope, therefore model need to know this scope
    institution.uiScope = rememberCoroutineScope()

    MasterDetail(toolbar = { Toolbar(institution) },
        explorer = { Explorer(institution) },
        editor = { Editor(institution.windparkUnderControl) }
    )

}

@Composable
private fun Toolbar(institution: ControllingInstitution) {
    IconButton(
        onClick = { institution.create() },
        modifier = Modifier.handCursor()
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Create"
        )
    }
    IconButton(
        onClick = { institution.save() },
        modifier = Modifier.handCursor()
    ) {
        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = "Save"
        )
    }
    IconButton(
        onClick = { institution.delete() },
        modifier = Modifier.handCursor()
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete"
        )
    }
}


@Composable
private fun Explorer(institution: ControllingInstitution) {
    GenericExplorer(data = institution.allWindparks,
        key = { it.id },
        scrollState = institution.explorerScrollState,
        onShow = { it.loadImageBitmap() }
    ) {
        CardExplorer(it, institution)
    }
}

@Composable
private fun Editor(windpark: Windpark?) {

    GenericEditor(item = windpark,
        selectText = "Select a windpark",
        backgroundImage = windpark?.imageBitmap ?: Windpark.defaultImageBitmap,
        headerContent = { HeaderContent(windpark) },
        formContent = { FormContent(windpark) })
}


@Composable
private fun HeaderContent(windpark: Windpark?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight().padding(16.dp)
        ) {
            Text(text = windpark?.name.format(), fontSize = 35.sp, fontWeight = FontWeight.Light)
            Text(text = windpark?.canton.format(), fontSize = 25.sp, fontWeight = FontWeight.Thin)

            Spacer(modifier = Modifier.size(100.dp))

            Text(text = "${windpark?.installedPower_KW} kW", fontSize = 25.sp, fontWeight = FontWeight.Thin)
            Text(text = "${windpark?.totalProduction_MWH} MWH", fontSize = 25.sp, fontWeight = FontWeight.Thin)
        }
        Image(
            bitmap = windpark?.imageBitmap ?: Windpark.defaultImageBitmap,
            contentDescription = windpark?.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.width(230.dp).height(300.dp)
        )
    }
}

@Composable
private fun FormContent(windpark: Windpark?) {
    FormField(label = "Status") {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        ) {
            items(WindparkState.entries) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = it.stateDescription(windpark?.status?.description ?: ""),
                        onClick = { windpark?.updateStatus(it) },
                        modifier = Modifier
                    )
                    Text(text = it.description)
                }
            }
        }
    }

    FormTextField(label = "Name",
        value = windpark?.name ?: "No name",
        onValueChange = { windpark?.updateName(it) }
    )
    FormTextField(label = "Gemeinde",
        value = windpark?.communes ?: "No communes",
        onValueChange = { windpark?.updateCommunes(it) }
    )
    FormTextField(label = "Kanton",
        value = windpark?.canton ?: "No canton",
        onValueChange = { windpark?.updateCanton(it) }
    )

    TwoColumnRows(labelLeft = "Baustart",
        valueLeft = windpark?.constructionStart.toString(),
        onValueChangedLeft = { windpark?.updateConstructionStart(it) },
        labelRight = "Vollendung",
        valueRight = windpark?.completion.toString(),
        onValueChangedRight = { windpark?.updateCompletion(it) })

    FormTextField(label = "Installierter Strom (kW)",
        value = windpark?.installedPower_KW.toString() ?: "No power",
        onValueChange = { windpark?.updateInstalledPower(it) }
    )

    TwoColumnRows(labelLeft = "Produktion 2015 (MWH)",
        valueLeft = windpark?.production2015_MWH.toString(),
        onValueChangedLeft = { windpark?.updateProduction2015(it) },
        labelRight = "Produktion 2016 (MWH)",
        valueRight = windpark?.production2016_MWH.toString(),
        onValueChangedRight = { windpark?.updateProduction2016(it) })

    TwoColumnRows(labelLeft = "Produktion 2017 (MWH)",
        valueLeft = windpark?.production2017_MWH.toString(),
        onValueChangedLeft = { windpark?.updateProduction2017(it) },
        labelRight = "Produktion 2018 (MWH)",
        valueRight = windpark?.production2018_MWH.toString(),
        onValueChangedRight = { windpark?.updateProduction2018(it) })


    FormTextField(label = "Anzahl",
        value = windpark?.count.toString(),
        onValueChange = { windpark?.updateCount(it) }
    )

    TwoColumnRows(labelLeft = "Länge",
        valueLeft = windpark?.longitude.toString(),
        onValueChangedLeft = { windpark?.updateLongitude(it) },
        labelRight = "Breite",
        valueRight = windpark?.latitude.toString(),
        onValueChangedRight = { windpark?.updateLatitude(it) })


    FormTextField(label = "Bild URL",
        value = windpark?.imageUrl ?: "No Image",
        onValueChange = { windpark?.updateImageURL(it) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CardExplorer(windpark: Windpark, institution: ControllingInstitution) {
    Card(
        onClick = { institution.updateWindparkUnderControl(windpark) },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (institution.isWindparkUnderControl(windpark)) Color.LightGray else Color.White
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                bitmap = windpark.imageBitmap,
                contentDescription = windpark.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(50.dp).clip(CircleShape)
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(10.dp).weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start

            ) {
                Text(text = cutLongWord(windpark.name.format()), fontSize = 15.sp)
                Text(text = cutLongWord(windpark.communes.format()), fontSize = 12.sp)
                if (windpark.installedPower_KW != null) {
                    Text(text = "${windpark.installedPower_KW} kW", color = Color.Gray, fontSize = 13.sp)
                }
            }
            Column(modifier = Modifier.align(Alignment.Top).padding(0.dp, 0.dp, 10.dp, 0.dp)) {
                Text(text = windpark.canton.format(), fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun TwoColumnRows(
    labelLeft: String, labelRight: String,
    valueLeft: String, valueRight: String,
    onValueChangedLeft: (String) -> Unit,
    onValueChangedRight: (String) -> Unit
) {

    TwoColumnRow(left = {
        FormTextField(
            label = labelLeft,
            value = valueLeft,
            onValueChange = onValueChangedLeft,
            modifier = it
        )
    }) {
        FormTextField(
            label = labelRight,
            value = valueRight,
            onValueChange = onValueChangedRight,
            modifier = it
        )
    }
}

private fun cutLongWord(word: String): String {
    if (word.length > 20) {
        return "${word.take(12)} ..."
    }
    return word
}


