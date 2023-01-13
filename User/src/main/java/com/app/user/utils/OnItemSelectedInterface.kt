package com.app.user.utils

interface OnItemSelectedInterface {
    fun onItemClick(position: String?)
}

interface OnItemSelectedInterfaceWithArguments {
    fun onItemClick(id: String?, price: String?, stadiumName:String?)
}