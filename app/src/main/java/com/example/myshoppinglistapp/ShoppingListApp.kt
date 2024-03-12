package com.example.myshoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id : Int,
                        var name : String ,
                        var quantity : Int ,
                        var isEditing : Boolean = false)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun ShoppingListApp(){
    var sitems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center){
        Button(onClick = {showDialog=true}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add Item")

        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            items(sitems){
                item ->  
                if(item.isEditing){
                    ShoppingItemEditor(item =item ,
                        onEditComplete = {EditedName , EditedQuantity ->
                            sitems = sitems.map { it.copy(isEditing = false)}
                            val editedItem = sitems.find { it.id == item.id }
                            editedItem?.let {
                                it.name=EditedName
                                it.quantity=EditedQuantity
                            }
                        } )
                }
                else{
                    ShoppingListItem(item = item , onEditClick = {
                        sitems=sitems.map{it.copy(isEditing = it.id==item.id)}
                    },
                        onDeleteClick = {
                            sitems = sitems - item
                        })
                }

            }

        }

    }
    if(showDialog){
        AlertDialog(onDismissRequest = {showDialog=false},
            confirmButton = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick = {
                        if(itemName.isNotBlank()){
                            val newItem = ShoppingItem(id = sitems.size+1,
                                name = itemName,
                                quantity = itemQuantity.toInt())
                            sitems = sitems + newItem
                            showDialog = false
                            itemName=""
                        }
                    }) {
                        Text(text = "Add")
                    }
                    Button(onClick = {showDialog=false}) {
                        Text(text = "Cancel")
                    }
                }
            },
            title = { Text(text = "Add Shopping item")},
            text = {
                Column {
                    OutlinedTextField(value = itemName ,
                        onValueChange = {itemName = it} ,
                        label = { Text(text = "Item Name")},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))
                    OutlinedTextField(value = itemQuantity ,
                        onValueChange = {itemQuantity = it} ,
                        label = { Text(text = "Item Quantity")},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))

                }

            })
    }
}
@Composable
fun ShoppingItemEditor(item: ShoppingItem , onEditComplete : (String,Int)->Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Row(modifier = Modifier
        .padding(8.dp)
        .background(Color.White)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly){
        Column {
            BasicTextField(value = editedName,
                onValueChange = {editedName=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
            BasicTextField(value = editedQuantity,
                onValueChange = {editedQuantity=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
        }
        Button(onClick = {
            isEditing=false
            onEditComplete(editedName,editedQuantity.toIntOrNull()?:1)
        }) {
            Text(text = "Save")
        }

    }


}



@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick : () -> Unit,
    onDeleteClick : () -> Unit
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .border(
            border = BorderStroke(2.dp, Color(0xFF6B6565)),
            shape = RoundedCornerShape(20)
        ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = "Item: ${item.name}" , modifier = Modifier.padding(top = 20.dp,start=10.dp))
        Text(text = "Qty: ${item.quantity.toString()}", modifier = Modifier.padding(top=20.dp))
        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }

        }

    }

}