package com.example.mytodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.ui.text.style.TextDecoration

data class itemTD(val name: String, val quantity: String, var isChecked: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoApp()
        }
    }
}

@Composable
fun ToDoApp() {
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    val toDoList = remember { mutableStateListOf<itemTD>() }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // Input Fields for Item Name and Quantity
        TextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Item Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField(
            value = itemQuantity,
            onValueChange = { itemQuantity = it },
            label = { Text("Quantity") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Add Item Button
        ElevatedButton(
            onClick = {
                if (itemName.isNotBlank() && itemQuantity.isNotBlank()) {
                    toDoList.add(itemTD(name = itemName, quantity = itemQuantity))
                    itemName = ""
                    itemQuantity = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton( onClick = {clearCompleted(toDoList)}) { Text("Clear Completed") }

        // Display List
        LazyColumn(modifier = Modifier.fillMaxHeight()) { //chatGPT suggestion
            items(toDoList.size) { index ->
                val item = toDoList[index]
                ItemTDRow(
                    item = item,
                    onCheckedChange = { isChecked ->
                        toDoList[index] = item.copy(isChecked = isChecked)
                    }
                )
            }
        }
    }
}

fun clearCompleted(toDoList: MutableList<itemTD>) {
    toDoList.retainAll { !it.isChecked } //gemini suggestion
}

@Composable
fun ItemTDRow(item: itemTD, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = item.name,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None
            )
            Text(text = "Quantity: ${item.quantity}")
        }
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}