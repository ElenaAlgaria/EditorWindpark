package windparks.view

import java.awt.Cursor
import java.util.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MasterDetail(toolbar:  @Composable () -> Unit = {},
                 explorer: @Composable () -> Unit,
                 editor:   @Composable () -> Unit){
    val padding = 20.dp
    val elevation = 2.dp

    Column {
        TopAppBar(backgroundColor = Color.LightGray){
            toolbar()
        }

        Row(modifier = Modifier.fillMaxSize()
            .padding(padding)){

            // see https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#Card(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.foundation.BorderStroke,androidx.compose.ui.unit.Dp,androidx.compose.foundation.interaction.MutableInteractionSource,kotlin.Function0)
            Card(elevation = elevation,
                 modifier  =  Modifier.weight(0.4f)
                     .fillMaxSize()){
                explorer()
            }

            Spacer(Modifier.width(padding))

            Card(elevation = elevation,
                 modifier  =  Modifier.weight(1.0f)
                     .fillMaxSize()){
                editor()
            }
        }
    }
}


@Composable
fun<T> GenericExplorer(data: List<T>,
                       key: (T) -> Int,
                       scrollState: LazyListState,
                       onShow : (T) -> Unit,
                       listItem: @Composable (T) -> Unit) {
    Box(contentAlignment = Alignment.Center,
        modifier         = Modifier.padding(5.dp)) {
        LazyColumn(state    = scrollState,
                   modifier = Modifier.padding(end = 12.dp).fillMaxHeight()) {
            items(items = data,
                  key   = { key(it) }) {

                LaunchedEffect(key(it)){
                    onShow(it)
                }

                listItem(it)
                Divider()
            }

        }
        VerticalScrollbar(adapter = ScrollbarAdapter(scrollState), Modifier.align(Alignment.TopEnd))
    }
}

@Composable
fun<T> GenericEditor(item: T?,
                     selectText: String, backgroundImage: ImageBitmap,
                     headerContent: @Composable RowScope.(T) -> Unit,
                     formContent: @Composable ColumnScope.(T) -> Unit) {
    if (null == item) {
        NothingSelected(text            = selectText,
                        backgroundImage = backgroundImage)
    } else {
        Column(Modifier.fillMaxSize()
                       .padding(10.dp)) {
            Card(backgroundColor = Color(0xFFEEEEEE)){
                Row(modifier = Modifier.padding(10.dp)
                                       .height(IntrinsicSize.Max)
                                       .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    headerContent(item)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(Modifier.weight(1.0f)){
                val scrollState = rememberScrollState()

                Column(Modifier.verticalScroll(scrollState)
                               .align(Alignment.TopStart)
                               .padding(start = 10.dp, end = 22.dp)) {
                    formContent(item)
                }

                VerticalScrollbar(adapter = ScrollbarAdapter(scrollState), modifier = Modifier.align(Alignment.CenterEnd))
            }
        }
    }
}

@Composable
fun NothingSelected(text: String, backgroundImage: ImageBitmap){
    Box(modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Text(text     = text,
             color    = Color.Gray,
             fontSize = 32.sp)

        Image(bitmap             = backgroundImage,
              contentDescription = "Default windpark image",
              contentScale       = ContentScale.Crop,
              alpha              = 0.15f,
              modifier           = Modifier.fillMaxSize()
             )
    }
}


@Composable
fun TwoColumnRow(left:  @Composable (Modifier) -> Unit, right:  @Composable (Modifier) -> Unit){
    Row(modifier = Modifier.fillMaxWidth()){
        left(Modifier.weight(1.0f))
        Spacer(modifier = Modifier.width(60.dp))
        right(Modifier.weight(1.0f))
    }
}


@Composable
fun FormTextField(label: String, labelWidth: Dp = 100.dp, modifier: Modifier = Modifier, value: String, onValueChange : (String) -> Unit){
    FormField(label      = label,
              labelWidth = labelWidth,
              modifier   = modifier,
              control    = { modifier -> OutlinedTextField(value         = value,
                                                           onValueChange = { onValueChange(it) },
                                                           singleLine    = true,
                                                           modifier      = modifier) })
}




@Composable
fun FormField(label: String, labelWidth: Dp = 100.dp, modifier: Modifier = Modifier, control: @Composable (modifier: Modifier) -> Unit){
    Row(modifier          = modifier.padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically){

        Text(text     = label,
             modifier = Modifier.width(labelWidth))

        control(Modifier.weight(1.0f))
    }
}

val CH = Locale("de", "CH")

// this is an Extension Function for any kind of Number.
// see https://kotlinlang.org/docs/extensions.html
fun Number?.format(pattern: String, nullFormat: String = ""): String {
    return if (null == this) nullFormat else pattern.format(CH, this)
}

//other extension functions

fun String?.format(nullFormat: String = "") = this ?: nullFormat

fun Modifier.handCursor() = cursor(Cursor.HAND_CURSOR)

fun Modifier.cursor(cursorId: Int) : Modifier = pointerHoverIcon(PointerIcon(Cursor(cursorId)))

