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
    TopAppBar(modifier = Modifier,
        backgroundColor = Color.White,
        title = {},
        actions = {
            IconButton(onClick = { institution.create() }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create"
                )
            }
            IconButton(onClick = { institution.save() }) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "Save"
                )
            }
            IconButton(onClick = { institution.delete() }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    )
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
fun HeaderContent(windpark: Windpark?) {
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
fun FormContent(windpark: Windpark?) {
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
        onValueChange = { windpark?.updateName(it) }
    )
    TwoColumnRow(left = {
        FormTextField(
            label = "Baustart",
            value = windpark?.constructionStart.toString(),
            onValueChange = { windpark?.updateConstructionStart(it) },
            modifier = it
        )
    }) {
        FormTextField(
            label = "Vollendung",
            value = windpark?.completion.toString(),
            onValueChange = { windpark?.updateCompletion(it) },
            modifier = it
        )
    }

    FormTextField(label = "Installierter Strom (kW)",
        value = windpark?.installedPower_KW.toString() ?: "No power",
        onValueChange = { windpark?.updateInstalledPower(it) }
    )

    TwoColumnRow(left = {
        FormTextField(
            label = "Produktion 2015 (MWH)",
            value = windpark?.production2015_MWH.toString(),
            onValueChange = { windpark?.updateProduction2015(it) },
            modifier = it
        )
    }) {
        FormTextField(
            label = "Produktion 2016 (MWH)",
            value = windpark?.production2016_MWH.toString(),
            onValueChange = { windpark?.updateProduction2016(it) },
            modifier = it
        )
    }

    TwoColumnRow(left = {
        FormTextField(
            label = "Produktion 2017 (MWH)",
            value = windpark?.production2017_MWH.toString(),
            onValueChange = { windpark?.updateProduction2017(it) },
            modifier = it
        )
    }) {
        FormTextField(
            label = "Produktion 2018 (MWH)",
            value = windpark?.production2018_MWH.toString(),
            onValueChange = { windpark?.updateProduction2018(it) },
            modifier = it
        )
    }


    FormTextField(label = "Anzahl",
        value = windpark?.count.toString(),
        onValueChange = { windpark?.updateCount(it.toInt()) }
    )


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardExplorer(windpark: Windpark, institution: ControllingInstitution) {
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


private fun cutLongWord(word: String): String {
    if (word.length > 20) {
        return "${word.take(12)} ..."
    }
    return word
}


