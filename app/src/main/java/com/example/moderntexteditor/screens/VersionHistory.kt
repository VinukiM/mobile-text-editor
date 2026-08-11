package com.example.moderntexteditor.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun VersionHistory(){


    Column(
        modifier =
            Modifier
                .width(230.dp)
                .fillMaxHeight()
                .background(Color(0xff18212f))
                .padding(15.dp)
    ){


        Text(
            "VERSION HISTORY",
            color=Color.Cyan,
            fontSize=12.sp
        )


        Spacer(
            Modifier.height(20.dp)
        )


        VersionCard(
            "v3 (Current)",
            "Autosaved changes",
            "Just now"
        )


        VersionCard(
            "v2",
            "Refactored main logic",
            "2 hours ago"
        )


        VersionCard(
            "v1",
            "Initial draft",
            "Yesterday"
        )



        Spacer(
            Modifier.weight(1f)
        )


        Button(
            onClick={}
        ){

            Text(
                "Restore this version"
            )

        }


    }

}





@Composable
fun VersionCard(
    title:String,
    description:String,
    time:String
){


    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical=5.dp),
        colors=
            CardDefaults.cardColors(
                containerColor =
                    Color(0xff263244)
            )
    ){

        Column(
            Modifier.padding(12.dp)
        ){

            Text(
                title,
                color=Color.White
            )

            Text(
                description,
                color=Color.LightGray
            )


            Text(
                time,
                color=Color.Gray,
                fontSize=12.sp
            )

        }

    }


}