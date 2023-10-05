package com.example.thinktanktrivia.Model

import java.io.Serializable

data class ResultsModel(
    var res:ArrayList<Res>,
    var id:String=""
):Serializable
{
    constructor() : this(ArrayList(),"" )
}
