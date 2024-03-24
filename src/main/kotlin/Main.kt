import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold {
            Row {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight().width(66.dp).background(Color(247, 242, 243))
                ) {
                    val titles = listOf("", "首页", "精选", "动态 ", "我的")
                    val icons = listOf(
                        Icons.Filled.ArrowBack,
                        Icons.Filled.Home,
                        Icons.Filled.Done,
                        Icons.Filled.Menu,
                        Icons.Filled.Face
                    )
                    titles.forEachIndexed { index, title ->
                        ListItem(icon = {
                            Column {
                                if (index == 0) {
                                    Icon(imageVector = icons[index], contentDescription = null)
                                } else {
                                    Icon(imageVector = icons[index], contentDescription = null)
                                    Text(title)
                                }
                            }
                        }, text = {
                            Text(title, color = Color.White)
                        }, modifier = Modifier.clickable {
                            println(title)
                        }, trailing = {
                            Text("ddd")
                        })
                    }
                }
                Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    tabDemo()
                }
            }
        }
    }
}

@Composable
fun radioButtonDemo() {
    var selected by remember { mutableStateOf(false) }
    RadioButton(onClick = {
        selected = !selected
    }, selected = selected)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun tabDemo() {
    var state by remember { mutableStateOf(0) }
    val titles = listOf("直播", "推荐", "热门", "追番", "影视")

    Column {
        TabRow(selectedTabIndex = state, backgroundColor = Color.White) {
            titles.forEachIndexed { index, title ->
                Tab(selected = state == index,
                    onClick = { state = index },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) })
            }
        }

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = ">Text Tab ${state + 1} selected.<",
        )

    }

}

@Composable
fun listDemo() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Gray),
        // 容器之间的Padding
//        contentPadding = PaddingValues(35.dp),
        // item之间的间距
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(50) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(), elevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(15.dp)
                ) {
                    Text("item-$index", style = MaterialTheme.typography.h5)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun dialogDemo() {
    var openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(onDismissRequest = {
            openDialog.value = false
        }, title = { Text(text = "e111") }, text = {
            Text(text = "Content")
        }, confirmButton = {
            TextButton(onClick = {
                openDialog.value = false
            }) {
                Text("确定")
            }
        }, dismissButton = {
            TextButton(onClick = {
                openDialog.value = false
            }) {
                Text("取消")
            }
        })
    }
}

@Composable
fun scaffoldDemo() {
    Scaffold(bottomBar = {
        myBottomBar()
    }, topBar = {
        myTopAppBar()
    }) {

    }
}

@Composable
private fun myTopAppBar() {
    TopAppBar(
        title = { Text("My App", textAlign = TextAlign.Center) },
        backgroundColor = MaterialTheme.colors.background,
    )
}

@Composable
fun myBottomBar() {
    BottomAppBar {

    }
}

/**
 *  布局demo
 */
@Composable
fun layoutDemo() {
    Row {
        Column(
            Modifier.background(color = Color.Red).then(Modifier.fillMaxHeight())
        ) {
            Row(
                Modifier.background(color = Color.Blue)
            ) {
                demo03()
                demo04()
            }

            Row(
                Modifier.background(color = Color.Gray)
            ) {
                demo03()
                demo04()
            }
        }

        Spacer(
            modifier = Modifier.padding(10.dp)
        )

        Column(
            Modifier.background(color = Color.Blue).then(Modifier.fillMaxHeight())
        ) {
            Row(
                Modifier.background(color = Color.Blue)
            ) {
                Text("Column2")
                demo04()
            }

            Row(
                Modifier.background(color = Color.Gray)
            ) {
                demo03()
                demo04()
            }
        }
    }
}

/**
 *  ExtendFloatingActionButtuon
 */
@Composable
fun demo04() {
    ExtendedFloatingActionButton(text = { Text("添加到我的喜欢") }, onClick = {

    }, icon = {
        Icon(Icons.Filled.Favorite, contentDescription = null)
    })
}

/**
 *  悬浮按钮
 */
@Composable
fun demo03() {
    FloatingActionButton(
        onClick = {

        },

        ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = null
        )
    }
}

@Composable
fun demo02(text: String) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val pressState = interactionSource.collectIsPressedAsState() // 是否为按下状态
    val hoveredState = interactionSource.collectIsHoveredAsState() // 获取焦点状态
    val focusedState = interactionSource.collectIsFocusedAsState() // 获取焦点状态
    val draggedState = interactionSource.collectIsDraggedAsState() // 是否为拖动状态

    val borderColor = if (pressState.value) {
        Color.Green
    } else if (hoveredState.value) {
        Color.Red
    } else if (focusedState.value) {
        Color.Blue
    } else if (draggedState.value) {
        Color.Gray
    } else {
        Color.White
    }
    Button(
        onClick = {

        },
        border = BorderStroke(2.dp, color = borderColor),
        interactionSource = interactionSource,
    ) {
        Text(text)
    }


}

@Composable
private fun demo01(text: String) {
    var text1 = text
    text1 = "点我"
    Button(onClick = {
        println("我被点击了")
    }) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(
            Modifier.width(ButtonDefaults.IconSpacing)
        )
        Text(text1)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Demo",
        transparent = false,
        undecorated = false,
    ) {
        App()
    }
}
