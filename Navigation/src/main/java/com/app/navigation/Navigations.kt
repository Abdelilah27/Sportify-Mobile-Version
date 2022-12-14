package com.app.navigation

interface Navigations {
    fun navigate(graph: NavGraph, args: String = "")
    fun popBackStack()
}